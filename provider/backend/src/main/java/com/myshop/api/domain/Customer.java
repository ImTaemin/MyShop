package com.myshop.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Customer {

    @Id
    @Column(name = "customer_id")
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "customer")
    private List<Orders> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Cart> cartList = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Wish> wishList = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Coupon> usedCouponList = new ArrayList<>();

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDateTime createDate;
}
