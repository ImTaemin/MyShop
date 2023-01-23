package com.myshop.api.service;

import com.myshop.api.annotation.mock.WithMockProvider;
import com.myshop.api.domain.dto.request.CouponRequest;
import com.myshop.api.domain.dto.response.data.CouponData;
import com.myshop.api.domain.entity.Coupon;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.enumeration.UserRole;
import com.myshop.api.repository.CouponRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    CouponService couponService;

    @Mock
    CouponRepository couponRepository;

    Provider provider;
    Coupon coupon;

    CouponData couponData = new CouponData();

    @BeforeEach
    public void init() {
        provider = Provider.builder()
                .id(1L)
                .userId("taemin")
                .password("1234")
                .phone("010-1234-5678")
                .brandName("브랜드명")
                .roles(Set.of(UserRole.PROVIDER.toString()))
                .build();

        coupon = Coupon.builder()
                .id(1L)
                .provider(provider)
                .code("ABCD-EFGH")
                .content("1주년 감사 쿠폰")
                .expireDate(LocalDateTime.now().plusDays(5))
                .discount(10)
                .build();

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
        given(couponRepository.getCouponList(any(Provider.class)))
                .willReturn(List.of(couponData, couponData));

        //when
        List<CouponData> couponDataList = couponRepository.getCouponList(provider);

        //then
        Assertions.assertEquals(couponDataList.size(), 2);
        verify(couponRepository).getCouponList(any(Provider.class));
    }
    
    
    @Test
    @WithMockProvider
    @DisplayName("쿠폰 등록")
    public void insertCoupon() throws Exception {
        //given

        //when
        couponRepository.save(new Coupon());

        //then
        verify(couponRepository).save(any(Coupon.class));
    }

    @Test
    @WithMockProvider
    @DisplayName("쿠폰 코드 중복 확인 - 사용 불가")
    public void checkAvailableCouponCode() throws Exception {
        //given
        given(!couponRepository.existsByCode(anyString())).willReturn(false);

        //when
        boolean exist = couponRepository.existsByCode("ABCD-EFGH");

        //then
        Assertions.assertFalse(exist);
        verify(couponRepository).existsByCode(anyString());
    }

    @Test
    @WithMockProvider
    @DisplayName("쿠폰 코드 중복 확인 - 사용 가능")
    public void checkUnavailableCouponCode() throws Exception {
        //given
        given(couponRepository.existsByCode(anyString())).willReturn(true);

        //when
        boolean exist = couponRepository.existsByCode("ABCD-EFGH");

        //then
        Assertions.assertTrue(exist);
        verify(couponRepository).existsByCode(anyString());
    }

    @Test
    @WithMockProvider
    @DisplayName("쿠폰 수정")
    public void updateCoupon() throws Exception {
        //given

        //when
        couponRepository.updateCoupon(provider, new CouponRequest());

        //then
        verify(couponRepository).updateCoupon(any(Provider.class), any(CouponRequest.class));
    }

    @Test
    @WithMockProvider
    @DisplayName("쿠폰 삭제")
    public void deleteCoupon() throws Exception {
        //given

        //when
        couponRepository.deleteCoupon(provider, "ABCD-EFGH");

        //then
        verify(couponRepository).deleteCoupon(any(Provider.class), anyString());
    }
}
