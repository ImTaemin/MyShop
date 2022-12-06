package com.myshop.api.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "Cart", indexes = {
        @Index(name = "idx_cart_customer_id", columnList = "customer_id")
})
public class Cart {

    @Id
    @GeneratedValue()
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @OneToMany(mappedBy = "cart")
    private List<Item> itemList = new ArrayList<>();

    @Column(nullable = false)
    private int amount;

    @Builder
    public Cart(Long id, Customer customer, List<Item> itemList, int amount) {
        this.id = id;
        this.customer = customer;
        this.itemList = itemList;
        this.amount = amount;
    }
}
