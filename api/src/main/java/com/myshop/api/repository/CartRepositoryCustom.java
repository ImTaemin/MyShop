package com.myshop.api.repository;

import com.myshop.api.domain.dto.request.CartRequest;
import com.myshop.api.domain.dto.response.data.CartData;
import com.myshop.api.domain.entity.Customer;

import java.util.List;

public interface CartRepositoryCustom {
    List<CartData> getCartItemList(Customer customer);
    List<CartData> getSelectCartItemList(Customer customer, List<Long> itemIdList);
    void updateCartItemQuantity(Customer customer, CartRequest cartRequest);
    void deleteCartItems(Customer customer, List<Long> itemIdList);
}
