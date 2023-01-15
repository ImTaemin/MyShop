package com.myshop.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myshop.api.domain.dto.request.ItemRequest;
import com.myshop.api.enumeration.GenderType;
import com.myshop.api.enumeration.ItemType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@DynamicUpdate
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Item", indexes = {
        @Index(name = "idx_item_provider_id", columnList = "provider_id")
})
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    // 상품을 조회할 때 브랜드명을 얻기 위해 매번 판매자와 조인을 하는 것은 성능상 좋지 않음.
    @Column(nullable = false)
    private String brandName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int quantity;

    @Setter
    @Column(nullable = false)
    private String mainImage;

    @Lob
    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemType itemType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenderType genderType;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    @CreationTimestamp
    private LocalDate uploadDate;

    @Setter
    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "item",
            cascade = {CascadeType.REMOVE},
            orphanRemoval = true
    )
    @OrderBy("sequence asc")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ItemImage> itemImageList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", insertable = false, updatable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @Builder
    public Item(Long id, String code, String name, String brandName, int price, int quantity, String mainImage, String content, ItemType itemType, GenderType genderType, LocalDate uploadDate, List<ItemImage> itemImageList, Provider provider, Cart cart, Customer customer) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.brandName = brandName;
        this.price = price;
        this.quantity = quantity;
        this.mainImage = mainImage;
        this.content = content;
        this.itemType = itemType;
        this.genderType = genderType;
        this.uploadDate = uploadDate;
        this.itemImageList = itemImageList;
        this.provider = provider;
        this.cart = cart;
        this.customer = customer;
    }
}
