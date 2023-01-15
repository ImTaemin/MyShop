package com.myshop.api.domain.dto.response.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

public class CustomerData {

    @ApiModel
    @Data
    @NoArgsConstructor
    public static class Customer {
        @ApiModelProperty(value = ApiValueUtils.USER_ID)
        private String userId;

        @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$")
        @ApiModelProperty(value = ApiValueUtils.PHONE)
        private String phone;

        @ApiModelProperty(value = ApiValueUtils.Customer.NAME)
        private String name;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @ApiModelProperty(value = ApiValueUtils.CREATE_DATE)
        private LocalDateTime createDate;

        @Builder
        public Customer(com.myshop.api.domain.entity.Customer customer) {
            this.userId = customer.getUserId();
            this.phone = customer.getPhone();
            this.name = customer.getName();
            this.createDate = customer.getCreateDate();
        }
    }

}
