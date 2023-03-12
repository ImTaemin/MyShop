package com.myshop.api.service;

import com.myshop.api.domain.dto.request.CouponRequest;
import com.myshop.api.domain.dto.response.data.CouponData;
import com.myshop.api.domain.entity.Provider;

import java.util.List;

public interface CouponService {

    List<CouponData> getCouponList(Provider provider);
    void insertCoupon(Provider provider, CouponRequest couponRequest);
    Boolean checkCouponCode(Long providerId, String code);
    void updateCoupon(Provider provider, CouponRequest couponRequest);
    void deleteCoupon(Provider provider, String code);
}
