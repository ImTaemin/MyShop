package com.myshop.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myshop.api.domain.entity.id.OrderItemId;
import com.myshop.api.enumeration.OrderStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "OrderItem", indexes = {
        @Index(name = "idx_orderitem_order_id", columnList = "order_id")
})
@IdClass(OrderItemId.class)
public class OrderItem implements Persistable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private Orders orders;

    @Id
    private int cnt;

    @Column(nullable = false)
    private int quantity;

    /**
     * ※ 한 쿠폰은 한 상품에만 적용 가능 ※
     * <p>쿠폰 사용 상품 = (원가 - (원가 * 할인율)
     * <p>나머지 상품 = 원가
     */
    @Column(nullable = false)
    private int payment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime orderDate;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDateTime createDate;

    // 기본 inner join
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Override
    public Object getId() {
        return OrderItemId.builder()
                .orders(this.orders.getId())
                .cnt(this.cnt).build();
    }

    @Override
    public boolean isNew() {
        return createDate == null;
    }

    @Builder
    public OrderItem(Orders orders, int cnt, int quantity, int payment, Coupon coupon, OrderStatus orderStatus, LocalDateTime orderDate, LocalDateTime createDate, Item item) {
        this.orders = orders;
        this.cnt = cnt;
        this.quantity = quantity;
        this.payment = payment;
        this.coupon = coupon;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.createDate = createDate;
        this.item = item;
    }
}
