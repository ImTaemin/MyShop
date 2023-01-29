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

    private ItemData.ItemSimple item;

    public OrderItemData(com.myshop.api.domain.entity.OrderItem item) {
        this.orderNo = item.getOrders().getId();
        this.quantity = item.getQuantity();
        this.payment = item.getPayment();
        this.orderStatus = item.getOrderStatus();
    }

}
