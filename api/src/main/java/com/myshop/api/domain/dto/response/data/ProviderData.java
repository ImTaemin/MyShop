package com.myshop.api.domain.dto.response.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

public class ProviderData {

    @ApiModel
    @Data
    @NoArgsConstructor
    public static class Provider {
        @ApiModelProperty(value = ApiValueUtils.User.USER_ID)
        private String userId;

        @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$")
        @ApiModelProperty(value = ApiValueUtils.User.PHONE)
        private String phone;

        @ApiModelProperty(value = ApiValueUtils.User.Provider.BRAND_NAME)
        private String brandName;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")        @ApiModelProperty(value = ApiValueUtils.User.CREATE_DATE)
        private LocalDateTime createDate;

        public Provider(com.myshop.api.domain.entity.Provider provider) {
            this.userId = provider.getUserId();
            this.phone = provider.getPhone();
            this.brandName = provider.getBrandName();
            this.createDate = provider.getCreateDate();
        }
    }
}
