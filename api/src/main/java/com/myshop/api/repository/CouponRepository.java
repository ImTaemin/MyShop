package com.myshop.api.repository;

import com.myshop.api.domain.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponRepositoryCustom{
    Boolean existsByCode(String code);
}
