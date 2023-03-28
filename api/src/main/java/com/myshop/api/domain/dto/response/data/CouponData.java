package com.myshop.api.domain.dto.response.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myshop.api.domain.entity.Coupon;
import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    @ApiModelProperty(value = ApiValueUtils.Coupon.EXPIRATION_DATE)
    private LocalDate expirationDate;

    @ApiModelProperty(value = ApiValueUtils.Coupon.DISCOUNT, example = "10")
    private int discount;

    public CouponData(Long id, String code, String content, LocalDate expirationDate, int discount) {
        this.id = id;
        this.code = code;
        this.content = content;
        this.expirationDate = expirationDate;
        this.discount = discount;
    }

    public CouponData(Coupon coupon) {
        this.id = coupon.getId();
        this.code = coupon.getCode();
        this.content = coupon.getContent();
        this.expirationDate = coupon.getExpirationDate();
        this.discount = (int) (coupon.getDiscount() * 100);
    }
}
