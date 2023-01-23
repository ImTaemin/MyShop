package com.myshop.api.repository;

import com.myshop.api.domain.dto.request.CouponRequest;
import com.myshop.api.domain.dto.response.data.CouponData;
import com.myshop.api.domain.entity.Provider;

import java.util.List;

public interface CouponRepositoryCustom {
    List<CouponData> getCouponList(Provider provider);
    void updateCoupon(Provider provider, CouponRequest couponRequest);
    void deleteCoupon(Provider provider, String code);
}
