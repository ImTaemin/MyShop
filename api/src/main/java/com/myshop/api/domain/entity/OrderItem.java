package com.myshop.api.domain.entity;

import com.myshop.api.enumeration.OrderState;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "OrderItem", indexes = {
        @Index(name = "idx_orderitem_order_id", columnList = "order_id")
})
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @Column(nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderState orderState;

    @OneToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders orders;

    @Builder
    public OrderItem(Long id, int quantity, OrderState orderState, Coupon coupon, Item item, Orders orders) {
        this.id = id;
        this.quantity = quantity;
        this.orderState = orderState;
        this.coupon = coupon;
        this.item = item;
        this.orders = orders;
    }
}
