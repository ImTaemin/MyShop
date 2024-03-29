package com.myshop.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myshop.api.annotation.mock.WithMockProvider;
import com.myshop.api.domain.dto.request.ProviderRequest;
import com.myshop.api.domain.dto.request.UserUpdateRequest;
import com.myshop.api.domain.dto.response.data.SignData;
import com.myshop.api.enumeration.CommonResponse;
import com.myshop.api.enumeration.UserRole;
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

import java.util.HashMap;
import java.util.List;
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

    @SuppressWarnings("all")
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProviderService providerService;

    @MockBean
    SignService signService;

    ProviderRequest providerRequest;

    SignData.SignUpResponse signUpResponse = new SignData.SignUpResponse();
    SignData.SignInResponse signInResponse = new SignData.SignInResponse();

    @BeforeEach
    public void initEach() {
        signUpResponse.setStatus(CommonResponse.SUCCESS.getStatus());
        signUpResponse.setMsg(CommonResponse.SUCCESS.getMsg());

        signInResponse.setStatus(CommonResponse.SUCCESS.getStatus());
        signInResponse.setMsg(CommonResponse.SUCCESS.getMsg());
        signInResponse.setAccessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0YWVtaW4iLCJpYXQiOjE2NzE0NDA0NzAsImV4cCI6MTY3MTQ0NDA3MH0.AFvbCHzDXowGpBqfjBbcWfphe0Bv0o-UMijgOA2jnnQ");
        signInResponse.setRefreshToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0YWVtaW4iLCJpYXQiOjE2NzE0NDA0NzAsImV4cCI6MTY3MTQ0NDA3MH0.AFvbCHzDXowGpBqfjBbcWfphe0Bv0o-UMijgOA2jnnQ");

        providerRequest = ProviderRequest.builder()
                .userId("taemin")
                .password("1234")
                .phone("010-1234-5678")
                .brandName("브랜드명")
                .build();
    }

    @Test
    @WithMockUser
    @DisplayName("판매자 입점 테스트")
    public void signUpTest() throws Exception {
        //given
        given(signService.signUp(any(ProviderRequest.class)))
                .willReturn(signUpResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(providerRequest);

        //when
        mockMvc.perform(
                        post("/provider/sign-up")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").isBoolean())
                .andExpect(jsonPath("$.msg").exists())
                .andDo(print());

        //then
        verify(signService).signUp(any(ProviderRequest.class));
    }

    @Test
    @WithMockUser
    @DisplayName("판매자 로그인 테스트. 200 반환 시 토큰이 있음.")
    public void signInTest() throws Exception {
        //given
        given(signService.signInProvider(anyString(), anyString()))
                .willReturn(signInResponse);

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", "taemin");
        paramMap.put("password", "1234");

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(paramMap);

        //when
        mockMvc.perform(
                        post("/provider/sign-in")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").isBoolean())
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.token").exists())
                .andDo(print());

        //then
        verify(signService).signInProvider(anyString(), anyString());
    }

    @Test
    @WithMockUser
    @DisplayName("판매자 아이디 중복 확인")
    public void checkUserIdTest() throws Exception {
        //given
        given(providerService.checkUserId(anyString()))
                .willReturn(true);

        //when
        mockMvc.perform(
                        get("/provider/exists/id/taemin")
                                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        verify(providerService).checkUserId(anyString());
    }

    @Test
    @WithMockUser
    @DisplayName("브랜드명 중복 확인")
    public void checkBrandNameTest() throws Exception {
        //given
        given(providerService.checkBrandName(anyString()))
                .willReturn(true);

        //when
        mockMvc.perform(
                        get("/provider/exists/brand/브랜드명")
                                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        verify(providerService).checkBrandName(anyString());
    }

    @Test
    @WithMockProvider
    @DisplayName("판매자 정보 수정")
    public void modifyTest() throws Exception {
        //given
        given(providerService.modify(any(UserUpdateRequest.class)))
                .willReturn(true);

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", "taemin");
        paramMap.put("password", "1234");
        paramMap.put("phone", "010-5678-1234");

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(paramMap);

        //when
        mockMvc.perform(
                        put("/provider/")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        verify(providerService).modify(any(UserUpdateRequest.class));
    }

    @Test
    @WithMockProvider
    @DisplayName("판매자 퇴점")
    public void withdrawalTest() throws Exception {
        //given
        given(providerService.withdrawal(anyString(), anyString()))
                .willReturn(true);

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", providerRequest.getUserId());
        paramMap.put("password", providerRequest.getPassword());

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(paramMap);

        //when
        mockMvc.perform(
                delete("/provider/")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        verify(providerService).withdrawal(anyString(), anyString());
    }

}