package com.myshop.api.repository;

import com.myshop.api.domain.dto.request.CouponRequest;
import com.myshop.api.domain.dto.response.data.CouponData;
import com.myshop.api.domain.entity.Coupon;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Provider;

import java.util.List;
import java.util.Optional;

public interface CouponRepositoryCustom {
    List<CouponData> getCouponList(Provider provider);
    void updateCoupon(Provider provider, CouponRequest couponRequest);
    void deleteCoupon(Provider provider, String code);
    Optional<Coupon> findByCodeAndItemId(String code, Long itemId);
    Boolean existsByCouponCode(Long providerId, String code);
    CouponData searchCoupon(Customer customer, String couponCode, Long itemId);}
