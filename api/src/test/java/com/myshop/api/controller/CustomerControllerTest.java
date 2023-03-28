package com.myshop.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myshop.api.annotation.mock.WithMockCustomer;
import com.myshop.api.domain.dto.request.CustomerRequest;
import com.myshop.api.domain.dto.request.UserUpdateRequest;
import com.myshop.api.domain.dto.response.BaseResponse;
import com.myshop.api.domain.dto.response.data.SignData;
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

    @SuppressWarnings("all")
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @MockBean
    SignService signService;

    CustomerRequest customerRequest;

    SignData.SignUpResponse signUpResponse = new SignData.SignUpResponse();
    SignData.SignInResponse signInResponse = new SignData.SignInResponse();

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void initEach() {
        signUpResponse.setStatus(CommonResponse.SUCCESS.getStatus());
        signUpResponse.setMsg(CommonResponse.SUCCESS.getMsg());

        signInResponse.setStatus(CommonResponse.SUCCESS.getStatus());
        signInResponse.setMsg(CommonResponse.SUCCESS.getMsg());
        signInResponse.setAccessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0YWVtaW4iLCJpYXQiOjE2NzE0NDA0NzAsImV4cCI6MTY3MTQ0NDA3MH0.AFvbCHzDXowGpBqfjBbcWfphe0Bv0o-UMijgOA2jnnQ");
        signInResponse.setRefreshToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0YWVtaW4iLCJpYXQiOjE2NzE0NDA0NzAsImV4cCI6MTY3MTQ0NDA3MH0.AFvbCHzDXowGpBqfjBbcWfphe0Bv0o-UMijgOA2jnnQ");


        customerRequest = CustomerRequest.builder()
                .userId("taemin")
                .password("1234")
                .phone("010-1234-5678")
                .name("김태민")
                .build();
    }

    @Test
    @WithMockUser
    @DisplayName("구매자 회원 가입 테스트")
    public void signUpTest() throws Exception {
        //given
        given(signService.signUp(any(CustomerRequest.class)))
                .willReturn(signUpResponse);

        String content = objectMapper.writeValueAsString(customerRequest);

        //when
        mockMvc.perform(
                        post("/customer/sign-up")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.msg").exists())
                .andDo(print());

        //then
        verify(signService).signUp(any(CustomerRequest.class));
    }

    @Test
    @WithMockUser
    @DisplayName("구매자 로그인 테스트.")
    public void signInTest() throws Exception {
        //given
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(CommonResponse.SUCCESS.getStatus());
        baseResponse.setMsg(CommonResponse.SUCCESS.getMsg());

        given(signService.signInCustomer(anyString(), anyString()))
                .willReturn(signInResponse);

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", "taemin");
        paramMap.put("password", "1234");

        String content = objectMapper.writeValueAsString(paramMap);

        //when
        mockMvc.perform(
                        post("/customer/sign-in")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.msg").exists())
                .andDo(print());

        //then
        verify(signService).signInCustomer(anyString(), anyString());
    }

    @Test
    @WithMockUser
    @DisplayName("구매자 아이디 중복 확인")
    public void checkUserIdTest() throws Exception {
        //given
        given(customerService.checkUserId(anyString()))
                .willReturn(true);

        //when
        mockMvc.perform(
                        get("/customer/exists/id/taemin")
                                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        verify(customerService).checkUserId(anyString());
    }

    @Test
    @WithMockCustomer
    @DisplayName("구매자 정보 수정")
    public void modifyTest() throws Exception {
        //given
        given(customerService.modify(any(UserUpdateRequest.class)))
                .willReturn(true);

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", "taemin");
        paramMap.put("password", "1234");
        paramMap.put("phone", "010-5678-1234");

        String content = objectMapper.writeValueAsString(paramMap);

        //when
        mockMvc.perform(
                        put("/customer/")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        verify(customerService).modify(any(UserUpdateRequest.class));
    }

    @Test
    @WithMockCustomer
    @DisplayName("구매자 회원 탈퇴")
    public void withdrawalTest() throws Exception {
        //given
        given(customerService.withdrawal(anyString(), anyString()))
                .willReturn(true);

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", customerRequest.getUserId());
        paramMap.put("password", customerRequest.getPassword());

        String content = objectMapper.writeValueAsString(paramMap);

        //when
        mockMvc.perform(
                        delete("/customer/")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        verify(customerService).withdrawal(anyString(), anyString());
    }

}