package com.myshop.api.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Cart", indexes = {
        @Index(name = "idx_cart_customer_id", columnList = "customer_id")
})
public class Cart {

    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @OneToMany(mappedBy = "cart")
    private List<Item> itemList = new ArrayList<>();

    @Column(nullable = false)
    private int amount;

}
