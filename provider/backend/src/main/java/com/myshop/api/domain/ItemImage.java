package com.myshop.api.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ItemImage", indexes = {
        @Index(name = "idx_itemimage_item_id", columnList = "item_id")
})
public class ItemImage {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int sequence;

}
