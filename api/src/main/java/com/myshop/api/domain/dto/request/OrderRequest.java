package com.myshop.api.domain.dto.request;

import com.myshop.api.enumeration.OrderStatus;
import com.myshop.api.enumeration.PayMethod;
import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class OrderRequest {

    @ApiModel
    @Data
    @NoArgsConstructor
    public static class Order {
        @ApiModelProperty(value = ApiValueUtils.Order.Address.LOAD_NAME, required = true)
        private String loadName;

        @ApiModelProperty(value = ApiValueUtils.Order.Address.DETAIL, required = true)
        private String detail;

        @ApiModelProperty(value = ApiValueUtils.Order.Address.POSTAL_CODE, required = true)
        private String postalCode;

        @ApiModelProperty(value = ApiValueUtils.Order.PAY_METHOD, required = true)
        private PayMethod payMethod;

        @ApiModelProperty(value = ApiValueUtils.Order.ORDER_ITEM_LIST, required = true)
        private List<OrderItem> orderItemList;
    }

    @ApiModel
    @Data
    @NoArgsConstructor
    public static class OrderItem {

        /**
         * itemId로 직접 상품 가격을 조회 후 가격 정하는 방향으로 개발
         */
        @ApiModelProperty(value = ApiValueUtils.Item.ID, required = true, example = "1")
        private Long itemId;

        @ApiModelProperty(value = ApiValueUtils.OrderItem.QUANTITY, required = true, example = "3")
        private int quantity;

        @ApiModelProperty(value = ApiValueUtils.OrderItem.COUPON)
        private String couponCode;
    }


    @ApiModel
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderNoCnt {
        @ApiModelProperty(value = ApiValueUtils.Order.ID, required = true)
        private String orderNo;

        @ApiModelProperty(value = ApiValueUtils.Order.CNT, required = true)
        private int cnt;
    }

    @ApiModel
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderChange {
        private List<OrderNoCnt> orderNoCntList;
        @ApiModelProperty(value = ApiValueUtils.OrderItem.ORDER_STATUS, required = true)
        private OrderStatus orderStatus;
    }

}
