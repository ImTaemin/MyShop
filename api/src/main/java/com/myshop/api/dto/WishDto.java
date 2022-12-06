package com.myshop.api.dto;

import com.myshop.api.domain.Customer;
import com.myshop.api.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WishDto {

    private Long id;
    private List<Item> itemList;
    private Customer customer;

}
