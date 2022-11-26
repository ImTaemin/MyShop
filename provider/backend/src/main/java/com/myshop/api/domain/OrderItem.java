package com.myshop.api.domain;

import lombok.Data;

import javax.persistence.*;

@Data
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
    private int amount;

    @OneToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders orders;

}
