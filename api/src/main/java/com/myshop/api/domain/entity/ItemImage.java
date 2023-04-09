package com.myshop.api.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "ItemImage", indexes = {
        @Index(name = "idx_itemimage_item_id", columnList = "item_id")
})
public class ItemImage {

    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private String path;

    @Column
    private int sequence;

    @Builder
    public ItemImage(Long id, Item item, String path, int sequence) {
        this.id = id;
        this.item = item;
        this.path = path;
        this.sequence = sequence;
    }
}
