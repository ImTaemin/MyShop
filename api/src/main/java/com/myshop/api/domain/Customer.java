package com.myshop.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 * NoArgsConstructor(access = AccessLevel.PROTECTED) : 무분별한 객체 생성에 대해 한번 더 체크
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Customer {

    @Id
    @Column(name = "customer_id")
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "customer")
    private List<Orders> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Cart> cartList = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Wish> wishList = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Coupon> usedCouponList = new ArrayList<>();

    /**
     * AllArgsConstructor 는 클래스에 존재하는 모든 필드에 대한 생성자를 자동으로 생성하는데,
     * 인스턴스 멤버의 선언 순서에 영향을 받기 때문에
     * 변수의 순서를 바꾸면 생성자의 입력 값 순서도 바뀌게 되어 검출되지 않는 오류를 발생할 수 있다.
     * 따라서 생성자에 Builder 어노테이션을 작성
     */
    @Builder
    public Customer(String id, String password, String phone, String name, LocalDateTime createDate, List<Orders> orderList, List<Cart> cartList, List<Wish> wishList, List<Coupon> usedCouponList) {
        this.id = id;
        this.password = password;
        this.phone = phone;
        this.name = name;
        this.createDate = createDate;
        this.orderList = orderList;
        this.cartList = cartList;
        this.wishList = wishList;
        this.usedCouponList = usedCouponList;
    }
}
