package com.myshop.api.domain.dto.response.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myshop.api.domain.entity.Orders;
import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
public class OrderData {

    @ApiModelProperty(value = ApiValueUtils.Order.ID)
    private String orderNo;

    @ApiModelProperty(value = ApiValueUtils.Order.TOTAL_PAYMENT)
    private int totalPayment;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    @ApiModelProperty(value = ApiValueUtils.Order.ORDER_DATE)
    private LocalDateTime orderDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    @ApiModelProperty(value = ApiValueUtils.Order.CANCEL_DATE)
    private LocalDateTime cancelDate;

    @ApiModelProperty(value = ApiValueUtils.Order.Address.LOAD_NAME)
    private String loadName;

    @ApiModelProperty(value = ApiValueUtils.Order.Address.DETAIL)
    private String detail;

    @ApiModelProperty(value = ApiValueUtils.Order.Address.POSTAL_CODE)
    private String postalCode;

    private List<ItemData.ItemSimple> item;

    public OrderData(Orders order) {
        this.orderNo = order.getId();
        this.totalPayment = order.getTotalPayment();
        this.orderDate = order.getOrderDate();
        this.cancelDate = order.getCancelDate();
        this.loadName = order.getAddress().getLoadName();
        this.detail = order.getAddress().getDetail();
        this.postalCode = order.getAddress().getPostalCode();
    }
}
