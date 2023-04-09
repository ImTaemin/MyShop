package com.myshop.api.service;

import com.myshop.api.domain.dto.request.CouponRequest;
import com.myshop.api.domain.dto.response.data.CouponData;
import com.myshop.api.domain.entity.Coupon;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.exception.DuplicateCouponException;
import com.myshop.api.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CouponServiceImpl implements CouponService{

    private final CouponRepository couponRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CouponData> getCouponList(Provider provider) {
        return couponRepository.getCouponList(provider);
    }

    @Transactional
    @Override
    public void insertCoupon(Provider provider, CouponRequest couponRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        Coupon coupon = Coupon.builder()
                .code(couponRequest.getCode())
                .content(couponRequest.getContent())
                .expirationDate(LocalDate.parse(couponRequest.getExpirationDate(), formatter))
                .discount(couponRequest.getDiscount() / 100)
                .provider(provider)
                .build();

        try {
            couponRepository.save(coupon);
        } catch (Exception e) {
            throw new DuplicateCouponException();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean checkCouponCode(Long providerId, String code) {
        return !couponRepository.existsByCouponCode(providerId, code);
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

    @Transactional(readOnly = true)
    @Override
    public CouponData searchCoupon(Customer customer, String couponCode, Long itemId) {
        return couponRepository.searchCoupon(customer, couponCode, itemId);
    }
}
