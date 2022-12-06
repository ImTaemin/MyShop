package com.myshop.api.dto;

import com.myshop.api.domain.Coupon;
import com.myshop.api.domain.Item;
import com.myshop.api.domain.Orders;
import com.myshop.api.enumeration.OrderState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemDto {

    private Long id;
    private int amount;
    private OrderState orderState;
    private Coupon coupon;
    private Item item;
    private Orders orders;

}
