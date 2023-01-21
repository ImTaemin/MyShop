package com.myshop.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myshop.api.domain.entity.id.FavoriteId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@IdClass(FavoriteId.class)
public class Favorite {

    @Id
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Id
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDateTime createDate;

    @Builder
    public Favorite(Customer customer, Item item) {
        this.customer = customer;
        this.item = item;
    }
}
