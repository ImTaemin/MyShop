package com.myshop.api.dto;

import com.myshop.api.domain.*;
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
public class CustomerDto {

    private String id;
    private String password;
    private String phone;
    private String name;
    private LocalDateTime createDate;
    private List<Orders> orderList;
    private List<Cart> cartList;
    private List<Wish> wishList;
    private List<Coupon> usedCouponList;

}
