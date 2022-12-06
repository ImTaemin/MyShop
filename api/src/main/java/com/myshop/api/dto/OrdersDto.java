package com.myshop.api.dto;

import com.myshop.api.domain.Customer;
import com.myshop.api.domain.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrdersDto {

    private String id;
    private String tid;
    private float discountPrice;
    private int totalPrice;
    private LocalDateTime orderDate;
    private LocalDateTime cancelDate;
    private String shippingLocation;
    private Customer customer;
    private List<OrderItem> orderItemList;

}
