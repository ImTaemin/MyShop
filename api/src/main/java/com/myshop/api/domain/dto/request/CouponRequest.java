package com.myshop.api.domain.dto.request;

import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@ApiModel
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CouponRequest {

    @NotBlank
    @ApiModelProperty(value = ApiValueUtils.Coupon.CODE, required = true, example = "ABCD-EFGH")
    private String code;

    @NotBlank
    @ApiModelProperty(value = ApiValueUtils.Coupon.CONTENT, required = true, example = "1주년 감사 쿠폰")
    private String content;

    @NotBlank
    @Future
    @Pattern(regexp = "^\\d{4}.\\d{2}.\\d{2}$")
    @ApiModelProperty(value = ApiValueUtils.Coupon.EXPIRATION_DATE, required = true, example = "2022.12.31")
    private String expirationDate;

    // % 단위 입력받음
    @Min(0) @Max(100)
    @NotBlank
    @ApiModelProperty(value = ApiValueUtils.Coupon.DISCOUNT, required = true, example = "10")
    private float discount;

}
