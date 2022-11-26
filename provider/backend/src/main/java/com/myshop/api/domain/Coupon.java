package com.myshop.api.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "Coupon", indexes = {
        @Index(name = "idx_coupon_provider_id", columnList = "provider_id"),
        @Index(name = "idx_coupon_code", columnList = "code"),
        @Index(name = "idx_coupon_customer_id", columnList = "customer_id")
})
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDate expireDate;

    @Column(nullable = false)
    private float discount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

}
