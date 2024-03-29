package com.myshop.api.domain.dto.response.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myshop.api.enumeration.GenderType;
import com.myshop.api.enumeration.ItemType;
import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ItemData {

    @ApiModel
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemSimple {

        @ApiModelProperty(value = ApiValueUtils.Item.ID)
        private Long id;

        @ApiModelProperty(value = ApiValueUtils.Item.CODE)
        private String code;

        @ApiModelProperty(value = ApiValueUtils.Item.NAME)
        private String name;

        @ApiModelProperty(value = ApiValueUtils.Item.BRAND_NAME)
        private String brandName;

        @ApiModelProperty(value = ApiValueUtils.Item.PRICE)
        private int price;

        @ApiModelProperty(value = ApiValueUtils.Item.MAIN_IMAGE)
        private String mainImage;

        public ItemSimple(com.myshop.api.domain.entity.Item item) {
            this.id = item.getId();
            this.code = item.getCode();
            this.name = item.getName();
            this.brandName = item.getBrandName();
            this.price = item.getPrice();
            this.mainImage = item.getMainImage();
        }

    }

    @ApiModel
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item extends ItemSimple {

        @ApiModelProperty(value = ApiValueUtils.Item.QUANTITY)
        private int quantity;

        @ApiModelProperty(value = ApiValueUtils.Item.ITEM_TYPE, allowableValues = ApiValueUtils.Item.ITEM_TYPE_ENUM)
        private ItemType itemType;

        @ApiModelProperty(value = ApiValueUtils.Item.GENDER_TYPE, allowableValues = ApiValueUtils.User.GENDER_TYPE)
        private GenderType genderType;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
        @ApiModelProperty(value = ApiValueUtils.Item.CREATE_DATE)
        private LocalDateTime createDate;

        @ApiModelProperty(value = ApiValueUtils.Item.IMAGE_LIST)
        private List<ItemImageData.ItemImage> imageDetailList = new ArrayList<>();

        @ApiModelProperty(value = ApiValueUtils.Favorite.IS_FAVORITE)
        private Boolean isFavorite;

        public Item(Long id, String code, String name, String brandName, int price, String mainImage, int quantity, ItemType itemType, GenderType genderType, LocalDateTime createDate, List<ItemImageData.ItemImage> imageDetailList, Boolean isFavorite) {
            super(id, code, name, brandName, price, mainImage);
            this.quantity = quantity;
            this.itemType = itemType;
            this.genderType = genderType;
            this.createDate = createDate;
            this.imageDetailList = imageDetailList;
            this.isFavorite = isFavorite;
        }

        public Item(com.myshop.api.domain.entity.Item item) {
            super(item);
            this.quantity = item.getQuantity();
            this.itemType = item.getItemType();
            this.genderType = item.getGenderType();
            this.createDate = item.getCreateDate();
            item.getItemImageList().forEach(itemImage -> {
                this.imageDetailList.add(new ItemImageData.ItemImage(itemImage));
            });
        }

    }
}
