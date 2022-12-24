package com.myshop.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myshop.api.dto.SignInResultDto;
import com.myshop.api.dto.SignUpResultDto;
import com.myshop.api.dto.customer.CustomerDto;
import com.myshop.api.dto.customer.CustomerUpdateParam;
import com.myshop.api.enumeration.CommonResponse;
import com.myshop.api.service.CustomerService;
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
import java.util.Collections;
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
@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @MockBean
    SignService signService;

    CustomerDto customer;

    SignUpResultDto signUpResultDto;
    SignInResultDto signInResultDto;

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

        customer = CustomerDto.builder()
                .userId("taemin")
                .password("1234")
                .phone("010-1234-5678")
                .name("김태민")
                .roles(Collections.singletonList("ROLE_CUSTOMER"))
                .build();
    }

    @Test
    @WithUser
    @DisplayName("구매자 회원 가입 테스트")
    public void signUpTest() throws Exception {
        //given
        given(signService.signUp(any(CustomerDto.class)))
                .willReturn(signUpResultDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(customer);

        //when
        mockMvc.perform(
                        post("/auth/customer/sign-up")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").isBoolean())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.msg").exists())
                .andDo(print());

        //then
        verify(signService).signUp(any(CustomerDto.class));
    }

    @Test
    @ProviderControllerTest.WithUser
    @DisplayName("판매자 로그인 테스트. 200 반환 시 토큰이 있음.")
    public void signInTest() throws Exception {
        //given
        given(signService.signInCustomer(anyString(), anyString()))
                .willReturn(signInResultDto);

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", "taemin");
        paramMap.put("password", "1234");

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(paramMap);

        //when
        mockMvc.perform(
                        post("/auth/customer/sign-in")
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
        verify(signService).signInCustomer(anyString(), anyString());
    }

    @Test
    @WithUser
    @DisplayName("구매자 아이디 중복 확인")
    public void checkUserIdTest() throws Exception {
        //given
        given(customerService.checkUserId(anyString()))
                .willReturn(true);

        //when
        mockMvc.perform(
                        get("/auth/customer/exists/id/taemin")
                                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        verify(customerService).checkUserId(anyString());
    }

    @Test
    @ProviderControllerTest.WithUser
    @DisplayName("구매자 정보 수정")
    public void modifyTest() throws Exception {
        //given
        given(customerService.modify(any(CustomerUpdateParam.class)))
                .willReturn(true);

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", "taemin");
        paramMap.put("password", "1234");
        paramMap.put("phone", "010-5678-1234");

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(paramMap);

        //when
        mockMvc.perform(
                        put("/auth/customer/")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        verify(customerService).modify(any(CustomerUpdateParam.class));
    }

    @Test
    @ProviderControllerTest.WithUser
    @DisplayName("구매자 회원 탈퇴")
    public void withdrawalTest() throws Exception {
        //given
        given(customerService.withdrawal(anyString(), anyString()))
                .willReturn(true);

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", customer.getUserId());
        paramMap.put("password", customer.getPassword());

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(paramMap);

        //when
        mockMvc.perform(
                        delete("/auth/customer/")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        verify(customerService).withdrawal(anyString(), anyString());
    }

}