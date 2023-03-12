package com.myshop.api.repository;

import com.myshop.api.domain.dto.request.CouponRequest;
import com.myshop.api.domain.dto.response.data.CouponData;
import com.myshop.api.domain.entity.Coupon;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.domain.entity.QCoupon;
import com.myshop.api.domain.entity.QItem;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
public class CouponRepositoryCustomImpl extends QuerydslRepositorySupport implements CouponRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    QCoupon qCoupon = QCoupon.coupon;
    QItem qItem = QItem.item;

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
                        qCoupon.expirationDate,
                        Expressions.numberTemplate(
                                Integer.class, "{0}",
                                qCoupon.discount.multiply(100).intValue()).as("discount")))
                .from(qCoupon)
                .orderBy(qCoupon.createDate.desc())
                .fetch();

        return resCartList;
    }

    @Override
    public void updateCoupon(Provider provider, CouponRequest couponRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        jpaQueryFactory.update(qCoupon)
                .set(qCoupon.content, couponRequest.getContent())
                .set(qCoupon.expirationDate, LocalDate.parse(couponRequest.getExpirationDate(), formatter))
                .set(qCoupon.discount, couponRequest.getDiscount() / 100)
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

    @Override
    public Optional<Coupon> findByCodeAndItemId(String code, Long itemId) {
        /*
         * select * from coupon c, item
         * where coupon.providerId = item.providerId
         * and coupon.code = :code
         * and item.id = :itemId
         */
        return Optional.ofNullable(jpaQueryFactory.select(qCoupon)
                .from(qCoupon)
                .innerJoin(qItem)
                .on(qCoupon.provider().id.eq(qItem.provider().id))
                .where(qCoupon.code.eq(code)
                        .and(qItem.id.eq(itemId)))
                .fetchOne());
    }

    @Override
    public Boolean existsByCouponCode(Long providerId, String code) {
        String findCouponCode = jpaQueryFactory.select(qCoupon.code)
                .from(qCoupon)
                .where(qCoupon.code.eq(code)
                        .and(qCoupon.provider().id.eq(providerId)))
                .fetchFirst();

        return findCouponCode != null;
    }
}
