package com.myshop.api.scheduler;

import com.myshop.api.domain.entity.QCoupon;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;

@Component
public class CouponScheduler {

    @PersistenceContext
    private EntityManager entityManager;

    QCoupon qCoupon = QCoupon.coupon;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 매일 00시
    public void deleteCouponExpirationDates() {
        LocalDate today = LocalDate.now();
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        jpaQueryFactory.delete(qCoupon)
                .where(qCoupon.expirationDate.loe(today))
                .execute();
    }

}
