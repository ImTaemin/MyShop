package com.myshop.api.dto;

import com.myshop.api.domain.Customer;
import com.myshop.api.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDto {

    private Long id;
    private String customerId;
    private List<Item> itemList;
    private int amount;

}
