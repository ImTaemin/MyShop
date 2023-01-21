package com.myshop.api.domain.dto.request;

import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartRequest {
    @ApiModelProperty(value = ApiValueUtils.Cart.ITEM_ID, required = true)
    private Long itemId;

    @ApiModelProperty(value = ApiValueUtils.Cart.QUANTITY, required = true)
    private int quantity;
}
