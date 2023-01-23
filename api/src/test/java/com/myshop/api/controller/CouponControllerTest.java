package com.myshop.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myshop.api.annotation.mock.WithMockProvider;
import com.myshop.api.domain.dto.request.CouponRequest;
import com.myshop.api.domain.dto.response.data.CouponData;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.service.CouponService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
@WebMvcTest(controllers = CouponController.class)
public class CouponControllerTest {

    @SuppressWarnings("all")
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CouponService couponService;

    ObjectMapper objectMapper = new ObjectMapper();

    CouponRequest couponRequest = new CouponRequest();

    CouponData couponData = new CouponData();

    @BeforeEach
    public void initEach() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        couponRequest.setCode("ABCD-EFGH");
        couponRequest.setContent("1주년 감사 쿠폰");
        couponRequest.setExpireDate(LocalDateTime.now().plusDays(5).toString());
        couponRequest.setDiscount(10);

        couponData.setId(1L);
        couponData.setCode("ABCD-EFGH");
        couponData.setContent("1주년 감사 쿠폰");
        couponData.setExpireDate(LocalDateTime.now().plusDays(5));
        couponData.setDiscount(10);
    }
    
    @Test
    @WithMockProvider
    @DisplayName("쿠폰 목록 조회 - 판매자")
    public void getCouponList() throws Exception {
        //given
        given(couponService.getCouponList(any(Provider.class)))
                .willReturn(List.of(couponData));

        //when
        mockMvc.perform(
                        get("/coupon/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andDo(print());
        
        //then
        verify(couponService).getCouponList(any(Provider.class));
    }
    
    @Test
    @WithMockProvider
    @DisplayName("쿠폰 등록")
    public void insertCouponTest() throws Exception {
        //given
        String content = objectMapper.writeValueAsString(couponRequest);

        //when
        mockMvc.perform(
                        post("/coupon/")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").isBoolean())
                .andExpect(jsonPath("$.msg").isString())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());

        //then
        verify(couponService).insertCoupon(any(Provider.class), any(CouponRequest.class));
    }

    @Test
    @WithMockUser
    @DisplayName("쿠폰 코드 중복 확인 - 사용 불가")
    public void checkAvailableCouponCode() throws Exception {
        //given
        given(couponService.checkCouponCode(anyString())).willReturn(false);
        String content = objectMapper.writeValueAsString(Map.of("code", "ABCD-EFGH"));

        //when
        mockMvc.perform(
                        post("/coupon/exists/")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.msg").isString())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());

        //then
        verify(couponService).checkCouponCode(anyString());
    }

    @Test
    @WithMockUser
    @DisplayName("쿠폰 코드 중복 확인 - 사용 가능")
    public void checkUnavailableCouponCode() throws Exception {
        //given
        given(couponService.checkCouponCode(anyString())).willReturn(true);
        String content = objectMapper.writeValueAsString(Map.of("code", "AAAA-CCCC"));

        //when
        mockMvc.perform(
                        post("/coupon/exists/")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.msg").isString())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());

        //then
        verify(couponService).checkCouponCode(anyString());
    }

    @Test
    @WithMockProvider
    @DisplayName("쿠폰 수정")
    public void updateCouponTest() throws Exception {
        //given
        String content = objectMapper.writeValueAsString(couponRequest);

        //when
        mockMvc.perform(
                        put("/coupon/")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").isBoolean())
                .andExpect(jsonPath("$.msg").isString())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());

        //then
        verify(couponService).updateCoupon(any(Provider.class), any(CouponRequest.class));
    }

    @Test
    @WithMockProvider
    @DisplayName("쿠폰 삭제")
    public void deleteCouponTest() throws Exception {
        //given

        //when
        mockMvc.perform(
                        delete("/coupon/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").isBoolean())
                .andExpect(jsonPath("$.msg").isString())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());

        //then
        verify(couponService).deleteCoupon(any(Provider.class), anyString());
    }
}
