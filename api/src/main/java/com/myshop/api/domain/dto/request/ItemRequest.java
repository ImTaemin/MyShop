package com.myshop.api.domain.dto.request;

import com.myshop.api.enumeration.GenderType;
import com.myshop.api.enumeration.ItemType;
import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ItemRequest {

    @ApiModel
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemIdList {
        @ApiModelProperty(value = ApiValueUtils.Item.ID_LIST, required = true)
        List<Long> itemIdList;
    }

    @ApiModel
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceAndQuantity {
        @ApiModelProperty(value = ApiValueUtils.Item.ID, required = true, example = "1")
        private Long id;

        @ApiModelProperty(value = ApiValueUtils.Item.PRICE, required = true, example = "30000")
        private int price;

        @ApiModelProperty(value = ApiValueUtils.Item.QUANTITY, required = true, example = "2")
        private int quantity;
    }

    @ApiModel
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {

        @ApiModelProperty(value = ApiValueUtils.Item.ID, example = "1")
        private Long id;

        @ApiModelProperty(value = ApiValueUtils.Item.CODE, required = true)
        private String code;

        @ApiModelProperty(value = ApiValueUtils.Item.NAME, required = true)
        private String name;

        @ApiModelProperty(value = ApiValueUtils.Item.BRAND_NAME, required = true)
        private String brandName;

        @ApiModelProperty(value = ApiValueUtils.Item.PRICE, required = true, example = "1")
        private int price;

        @ApiModelProperty(value = ApiValueUtils.Item.QUANTITY, required = true, example = "5")
        private int quantity;

        @ApiModelProperty(
                value = ApiValueUtils.Item.ITEM_TYPE,
                required = true,
                allowableValues = ApiValueUtils.Item.ITEM_TYPE_ENUM)
        private ItemType itemType;

        @ApiModelProperty(
                value = ApiValueUtils.Item.GENDER_TYPE,
                required = true,
                allowableValues = "UNISEX, MEN, WOMEN")
        private GenderType genderType;

        @ApiModelProperty(value = ApiValueUtils.Item.IMAGE_LIST)
        private List<MultipartFile> imageList;
    }

    @ApiModel
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemList {
        @ApiModelProperty(value = ApiValueUtils.Item.ITEM_LIST)
        List<Item> itemList;
    }

    @ApiModel
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceAndQuantityList {
        @ApiModelProperty(value = ApiValueUtils.Item.PRICE_QUANTITY)
        List<PriceAndQuantity> priceAndQuantityList;
    }

}
