package com.myshop.api.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CouponRequest {

    @ApiModelProperty(value = ApiValueUtils.Coupon.CODE, required = true, example = "ABCD-EFGH")
    private String code;

    @ApiModelProperty(value = ApiValueUtils.Coupon.CONTENT, required = true, example = "1주년 감사 쿠폰")
    private String content;

    @ApiModelProperty(value = ApiValueUtils.Coupon.EXPIRE_DATE, required = true, example = "2022.12.31 11:00:00")
    private String expireDate;

    // % 단위 입력받음
    @ApiModelProperty(value = ApiValueUtils.Coupon.DISCOUNT, required = true, example = "10")
    private float discount;
}
