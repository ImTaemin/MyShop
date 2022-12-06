package com.myshop.api.domain;

import com.myshop.api.enumeration.GenderType;
import com.myshop.api.enumeration.ItemType;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemType itemType;

    @Column(nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenderType genderType;

    // 즉시 로딩 : 연관 이미지들도 로딩
    @OneToMany(mappedBy = "item")
    @OrderBy("sequence") // 이미지 순서대로 가져오기
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

    @Builder
    public Item(Long id, String name, int price, String content, ItemType itemType, int amount, GenderType genderType, List<ItemImage> itemImageList, Provider provider, Cart cart, Wish wish) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.content = content;
        this.itemType = itemType;
        this.amount = amount;
        this.genderType = genderType;
        this.itemImageList = itemImageList;
        this.provider = provider;
        this.cart = cart;
        this.wish = wish;
    }
}
