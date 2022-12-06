package com.myshop.api.dto;

import com.myshop.api.domain.Cart;
import com.myshop.api.domain.ItemImage;
import com.myshop.api.domain.Provider;
import com.myshop.api.domain.Wish;
import com.myshop.api.enumeration.GenderType;
import com.myshop.api.enumeration.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDto {

    private Long id;
    private String name;
    private int price;
    private String content;
    private ItemType itemType;
    private int amount;
    private GenderType genderType;
    private List<ItemImage> itemImageList;
    private Provider provider;
    private Cart cart;
    private Wish wish;

}
