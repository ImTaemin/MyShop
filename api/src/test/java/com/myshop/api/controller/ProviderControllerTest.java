package com.myshop.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myshop.api.dto.SignInResultDto;
import com.myshop.api.dto.SignUpResultDto;
import com.myshop.api.dto.provider.ProviderDto;
import com.myshop.api.dto.provider.ProviderUpdateParam;
import com.myshop.api.enumeration.CommonResponse;
import com.myshop.api.service.ProviderService;
import com.myshop.api.service.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProviderController.class)
public class ProviderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProviderService providerService;

    @MockBean
    SignService signService;

    ProviderDto provider;

    SignUpResultDto signUpResultDto;
    SignInResultDto signInResultDto;

    ProviderDto registeredProvider;

    @Retention(RetentionPolicy.RUNTIME)
    @WithMockUser(username = "테스트_최고관리자", roles = "SUPER")
    public @interface WithUser {
    }

    @BeforeEach
    public void initEach() {
        signUpResultDto = new SignUpResultDto(true, CommonResponse.SUCCESS.getCode(), CommonResponse.SUCCESS.getMsg());

        signInResultDto = SignInResultDto.builder()
                .success(true)
                .code(CommonResponse.SUCCESS.getCode())
                .msg(CommonResponse.SUCCESS.getMsg())
                .token("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0YWVtaW4iLCJpYXQiOjE2NzE0NDA0NzAsImV4cCI6MTY3MTQ0NDA3MH0.AFvbCHzDXowGpBqfjBbcWfphe0Bv0o-UMijgOA2jnnQ")
                .build();

        provider = ProviderDto.builder()
                .loginId("taemin")
                .password("1234")
                .phone("010-1234-5678")
                .brandName("커버낫")
                .build();

        registeredProvider = ProviderDto.builder()
                .loginId("taemin")
                .password("$2a$12$Liq1iPQn58mqSt8Efe.mn.bQt7W4uuVNypg8N2IAHG.cEPqLqyMZ6")
                .phone("010-1234-5678")
                .brandName("커버낫")
                .createDate(LocalDateTime.now())
                .build();
    }

    @Test
    @WithUser
    @DisplayName("판매자 입점 테스트")
    public void signUpTest() throws Exception {
        //given
        given(signService.signUp(any(ProviderDto.class)))
                .willReturn(signUpResultDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(provider);

        //when
        mockMvc.perform(
                        post("/auth/provider/sign-up")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").isBoolean())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.msg").exists())
                .andDo(print());

        //then
        verify(signService).signUp(any(ProviderDto.class));
    }

    @Test
    @WithUser
    @DisplayName("판매자 로그인 테스트. 200 반환 시 토큰이 있음.")
    public void signInTest() throws Exception {
        //given
        given(signService.signIn(anyString(), anyString()))
                .willReturn(signInResultDto);

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("loginId", "taemin");
        paramMap.put("password", "1234");

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(paramMap);

        //when
        mockMvc.perform(
                        post("/auth/provider/sign-in")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.token").exists())
                .andDo(print());

        //then
        verify(signService).signIn(anyString(), anyString());
    }

    @Test
    @WithUser
    @DisplayName("브랜드명 중복 확인")
    public void checkBrandNameTest() throws Exception {
        //given
        given(providerService.checkBrandName(anyString()))
                .willReturn(true);

        //when
        mockMvc.perform(
                        get("/auth/provider/커버낫/exists")
                                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        verify(providerService).checkBrandName(anyString());
    }

    @Test
    @WithUser
    @DisplayName("판매자 수정")
    public void modifyTest() throws Exception {
        //given
        given(providerService.modify(any(ProviderUpdateParam.class)))
                .willReturn(true);

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("loginId", "taemin");
        paramMap.put("password", "1234");
        paramMap.put("phone", "010-5678-1234");

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(paramMap);

        //when
        mockMvc.perform(
                        put("/auth/provider/")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        verify(providerService).modify(any(ProviderUpdateParam.class));
    }

    @Test
    @WithUser
    @DisplayName("판매자 퇴점")
    public void withdrawalTest() throws Exception {
        //given
        given(providerService.withdrawal(anyString(), anyString()))
                .willReturn(true);

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("loginId", provider.getLoginId());
        paramMap.put("password", provider.getPassword());

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(paramMap);

        //when
        mockMvc.perform(
                post("/auth/provider/delete")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        verify(providerService).withdrawal(anyString(), anyString());
    }

}