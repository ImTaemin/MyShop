package com.myshop.api.service;

import com.myshop.api.domain.dto.request.CouponRequest;
import com.myshop.api.domain.dto.response.data.CouponData;
import com.myshop.api.domain.entity.Coupon;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.exception.DuplicateCouponException;
import com.myshop.api.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CouponServiceImpl implements CouponService{

    private final CouponRepository couponRepository;

    @Transactional
    @Override
    public List<CouponData> getCouponList(Provider provider) {
        return couponRepository.getCouponList(provider);
    }

    @Transactional
    @Override
    public void insertCoupon(Provider provider, CouponRequest couponRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        LocalDateTime expireDate = LocalDateTime.parse(couponRequest.getExpireDate(), formatter);

        Coupon coupon = Coupon.builder()
                .code(couponRequest.getCode())
                .content(couponRequest.getContent())
                .expireDate(expireDate)
                .discount(couponRequest.getDiscount() / 100)
                .provider(provider)
                .build();

        try {
            couponRepository.save(coupon);
        } catch (Exception e) {
            throw new DuplicateCouponException();
        }
    }

    @Transactional
    @Override
    public Boolean checkCouponCode(String code) {
        return !couponRepository.existsByCode(code);
    }

    @Transactional
    @Override
    public void updateCoupon(Provider provider, CouponRequest couponRequest) {
        couponRepository.updateCoupon(provider, couponRequest);
    }

    @Transactional
    @Override
    public void deleteCoupon(Provider provider, String code) {
        couponRepository.deleteCoupon(provider, code);
    }
}
