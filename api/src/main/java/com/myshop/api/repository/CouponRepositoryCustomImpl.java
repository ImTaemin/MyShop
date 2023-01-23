package com.myshop.api.repository;

import com.myshop.api.domain.dto.request.CouponRequest;
import com.myshop.api.domain.dto.response.data.CouponData;
import com.myshop.api.domain.entity.Coupon;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.domain.entity.QCoupon;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class CouponRepositoryCustomImpl extends QuerydslRepositorySupport implements CouponRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    QCoupon qCoupon = QCoupon.coupon;

    public CouponRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Coupon.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<CouponData> getCouponList(Provider provider) {
        List<CouponData> resCartList = jpaQueryFactory
                .select(
                        Projections.bean(CouponData.class,
                                qCoupon.id,
                        qCoupon.code,
                        qCoupon.content,
                        qCoupon.expireDate,
                        Expressions.numberTemplate(
                                Integer.class, "{0}",
                                qCoupon.discount.multiply(100).intValue()).as("discount")))
                .from(qCoupon)
                .fetch();

        return resCartList;
    }

    @Override
    public void updateCoupon(Provider provider, CouponRequest couponRequest) {
        jpaQueryFactory.update(qCoupon)
                .set(qCoupon.content, couponRequest.getContent())
                .set(qCoupon.expireDate, LocalDateTime.parse(couponRequest.getExpireDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm.ss")))
                .set(qCoupon.discount, couponRequest.getDiscount())
                .where(qCoupon.provider().id.eq(provider.getId())
                        .and(qCoupon.code.eq(couponRequest.getCode())))
                .execute();
    }

    @Override
    public void deleteCoupon(Provider provider, String code) {
        jpaQueryFactory.delete(qCoupon)
                .where(qCoupon.provider().id.eq(provider.getId())
                        .and(qCoupon.code.eq(code)))
                .execute();
    }
}
