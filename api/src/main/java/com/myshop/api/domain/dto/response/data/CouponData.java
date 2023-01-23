package com.myshop.api.domain.dto.response.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myshop.api.domain.entity.Coupon;
import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@ApiModel
@Data
@NoArgsConstructor
public class CouponData {

    @ApiModelProperty(value = ApiValueUtils.Coupon.ID)
    private Long id;

    @ApiModelProperty(value = ApiValueUtils.Coupon.CODE)
    private String code;

    @ApiModelProperty(value = ApiValueUtils.Coupon.CONTENT)
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(value = ApiValueUtils.Coupon.EXPIRE_DATE)
    private LocalDateTime expireDate;

    @ApiModelProperty(value = ApiValueUtils.Coupon.DISCOUNT, example = "10")
    private int discount;

    public CouponData(Coupon coupon) {
        this.id = coupon.getId();
        this.code = coupon.getCode();
        this.content = coupon.getContent();
        this.expireDate = coupon.getExpireDate();
        this.discount = (int) (coupon.getDiscount() * 100);
    }
}
