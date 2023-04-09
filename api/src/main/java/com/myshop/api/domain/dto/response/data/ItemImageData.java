package com.myshop.api.domain.dto.response.data;

import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ItemImageData {

    @ApiModel
    @Data
    @NoArgsConstructor
    public static class ItemImage {
        
        @ApiModelProperty(value = ApiValueUtils.ItemImage.ID)
        private Long id;

        @ApiModelProperty(value = ApiValueUtils.ItemImage.PATH)
        private String path;

        @ApiModelProperty(value = ApiValueUtils.ItemImage.SEQ)
        private int sequence;

        public ItemImage(com.myshop.api.domain.entity.ItemImage itemImage) {
            this.id = itemImage.getId();
            this.path = itemImage.getPath();
            this.sequence = itemImage.getSequence();
        }
    }
}
