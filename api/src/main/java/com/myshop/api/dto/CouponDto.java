package com.myshop.api.dto;

import com.myshop.api.domain.Customer;
import com.myshop.api.domain.Provider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CouponDto {

    private Long id;
    private Provider provider;
    private String code;
    private String content;
    private LocalDate expireDate;
    private float discount;
    private Customer customer;

}
