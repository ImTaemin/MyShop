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

    @ApiModelProperty(value = ApiValueUtils.Order.Address.ROAD_ADDRESS)
    private String roadName;

    @ApiModelProperty(value = ApiValueUtils.Order.Address.DETAIL_ADDRESS)
    private String detail;

    @ApiModelProperty(value = ApiValueUtils.Order.Address.POSTAL_CODE)
    private String postalCode;

    @ApiModelProperty(value = ApiValueUtils.Order.ORDER_ITEM_LIST)
    private List<OrderItemData> orderItemDataList;

    public OrderData(String orderNo, int totalPayment, LocalDateTime orderDate, LocalDateTime cancelDate, String roadName, String detail, String postalCode, List<OrderItemData> orderItemDataList) {
        this.orderNo = orderNo;
        this.totalPayment = totalPayment;
        this.orderDate = orderDate;
        this.cancelDate = cancelDate;
        this.roadName = roadName;
        this.detail = detail;
        this.postalCode = postalCode;
        this.orderItemDataList = orderItemDataList;
    }
}
