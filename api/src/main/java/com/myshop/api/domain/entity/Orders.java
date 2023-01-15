package com.myshop.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Orders", indexes = {
        @Index(name = "idx_orders_customer_id", columnList = "customer_id")
})
public class Orders {

    @Id
    @Column(name = "order_id")
    private String id;

    @Column(nullable = false)
    private String tid;

    @Column(nullable = false)
    private float discountPrice;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDateTime orderDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(nullable = false)
    private LocalDateTime cancelDate;

    @Column(nullable = false)
    private String shippingLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItemList = new ArrayList<>();

    @Builder
    public Orders(String id, String tid, float discountPrice, int totalPrice, LocalDateTime orderDate, LocalDateTime cancelDate, String shippingLocation, Customer customer, List<OrderItem> orderItemList) {
        this.id = id;
        this.tid = tid;
        this.discountPrice = discountPrice;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.cancelDate = cancelDate;
        this.shippingLocation = shippingLocation;
        this.customer = customer;
        this.orderItemList = orderItemList;
    }
}
