package com.myshop.api.domain.dto.response.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myshop.api.domain.entity.Orders;
import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
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

}
