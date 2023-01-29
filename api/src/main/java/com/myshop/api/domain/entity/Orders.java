package com.myshop.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myshop.api.domain.entity.embedded.Address;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DynamicUpdate
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Orders implements Persistable {

    @Id
    @Column(name = "order_id", unique = true)
    private String id;

    @Setter
    @Column
    private String tid;

    @Setter
    @Column
    private int totalPayment;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime cancelDate;

    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Setter
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItemList = new ArrayList<>();

    @Override
    public boolean isNew() {
        return createDate == null;
    }

    @Builder
    public Orders(String id, String tid, int totalPayment, LocalDateTime orderDate, LocalDateTime createDate, LocalDateTime cancelDate, Address address, Customer customer, List<OrderItem> orderItemList) {
        this.id = id;
        this.tid = tid;
        this.totalPayment = totalPayment;
        this.orderDate = orderDate;
        this.createDate = createDate;
        this.cancelDate = cancelDate;
        this.address = address;
        this.customer = customer;
        this.orderItemList = orderItemList;
    }
}
