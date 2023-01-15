package com.myshop.api.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "Cart", indexes = {
        @Index(name = "idx_cart_customer_id", columnList = "customer_id")
})
public class Cart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @ElementCollection
    @MapKeyColumn(name = "item_id")
    @Column(name = "quantity")
    @CollectionTable(name = "cart_items",
            joinColumns = @JoinColumn(name = "cart_id"))
    private Map<Long, Integer> itemList;

    @Builder
    public Cart(Long id, Customer customer, Map<Long, Integer> itemList) {
        this.id = id;
        this.customer = customer;
        this.itemList = itemList;
    }
}
