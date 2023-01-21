package com.myshop.api.domain.entity.id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartId implements Serializable {
    private Long customer;
    private Long item;
}
