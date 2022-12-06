package com.myshop.api.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Wish", indexes = {
        @Index(name = "idx_wish_customer_id", columnList = "customer_id")
})
public class Wish {

    @Id
    @GeneratedValue
    @Column(name = "wish_id")
    private Long id;

    @OneToMany(mappedBy = "wish")
    private List<Item> itemList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @Builder
    public Wish(Long id, List<Item> itemList, Customer customer) {
        this.id = id;
        this.itemList = itemList;
        this.customer = customer;
    }
}
