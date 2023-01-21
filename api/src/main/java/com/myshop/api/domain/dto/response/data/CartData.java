package com.myshop.api.domain.dto.response.data;

import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@NoArgsConstructor
public class CartData {

    @ApiModelProperty(value = ApiValueUtils.Item.ID)
    private Long id;

    @ApiModelProperty(value = ApiValueUtils.Item.NAME)
    private String name;

    @ApiModelProperty(value = ApiValueUtils.Item.BRAND_NAME)
    private String brandName;

    @ApiModelProperty(value = ApiValueUtils.Item.PRICE)
    private int price;

    @ApiModelProperty(value = ApiValueUtils.Item.MAIN_IMAGE)
    private String mainImage;

    @ApiModelProperty(value = ApiValueUtils.Cart.QUANTITY)
    private int quantity;

}
