package com.myshop.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
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

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false)
    private float discount;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @Builder
    public Coupon(Long id, Provider provider, String code, String content, LocalDate expirationDate, float discount, LocalDateTime createDate, Customer customer) {
        this.id = id;
        this.provider = provider;
        this.code = code;
        this.content = content;
        this.expirationDate = expirationDate;
        this.discount = discount;
        this.createDate = createDate;
        this.customer = customer;
    }
}
