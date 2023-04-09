package com.myshop.api.repository;

import com.myshop.api.domain.entity.UsedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsedCouponRepository extends JpaRepository<UsedCoupon, Long> {
}
