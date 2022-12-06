package com.myshop.api.dto;

import com.myshop.api.domain.Coupon;
import com.myshop.api.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProviderDto {

    private String id;
    private String password;
    private String phone;
    private String brandName;
    private LocalDateTime createDate;
    private List<Item> items;
    private List<Coupon> coupons;

}
