package com.myshop.api.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Wish", indexes = {
        @Index(name = "idx_wish_customer_id", columnList = "customer_id")
})
public class Wish {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "item_id", unique = true)
    private Item item;

    @Builder
    public Wish(Long id, Customer customer, Item item) {
        this.id = id;
        this.customer = customer;
        this.item = item;
    }
}
