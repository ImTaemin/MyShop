package com.myshop.api.domain;

import com.myshop.api.enumeration.GenderType;
import com.myshop.api.enumeration.ItemType;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Item", indexes = {
        @Index(name = "idx_item_provider_id_item_id", columnList = "provider_id, item_id")
})
public class Item {

    @Id
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Lob
    @Column(nullable = false)
    private String content;

    @Enumerated
    @Column(nullable = false)
    private ItemType itemType;

    @Column(nullable = false)
    private int amount;

    @Enumerated
    @Column(nullable = false)
    private GenderType genderType;

    // 즉시 로딩 : 아이템 검색 시 이미지들도 검색 가능
    @OneToMany(mappedBy = "item")
    private List<ItemImage> itemImageList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wish_id")
    private Wish wish;

}
