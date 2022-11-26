package com.myshop.api.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
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

}
