package com.myshop.api.domain.dto.response.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myshop.api.enumeration.OrderStatus;
import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@ApiModel
@Data
@NoArgsConstructor
public class OrderItemData {

    @ApiModelProperty(value = ApiValueUtils.OrderItem.CNT)
    private int cnt;

    @ApiModelProperty(value = ApiValueUtils.Order.ID)
    private String orderNo;

    @ApiModelProperty(value = ApiValueUtils.OrderItem.QUANTITY)
    private int quantity;

    @ApiModelProperty(value = ApiValueUtils.OrderItem.PAYMENT)
    private int payment;

    @ApiModelProperty(value = ApiValueUtils.OrderItem.ORDER_STATUS, allowableValues = ApiValueUtils.Order.ORDER_STATUS_ENUM)
    private OrderStatus orderStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    @ApiModelProperty(value = ApiValueUtils.Order.ORDER_DATE)
    private LocalDateTime orderDate;

    @ApiModelProperty(value = ApiValueUtils.OrderItem.ITEM)
    private ItemData.ItemSimple item;

    @ApiModelProperty(value = ApiValueUtils.OrderItem.COUPON)
    private CouponData coupon;

    public OrderItemData(int cnt, String orderNo, int quantity, int payment, OrderStatus orderStatus, LocalDateTime orderDate, ItemData.ItemSimple item, CouponData coupon) {
        this.cnt = cnt;
        this.orderNo = orderNo;
        this.quantity = quantity;
        this.payment = payment;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.item = item;
        this.coupon = coupon;
    }
}
