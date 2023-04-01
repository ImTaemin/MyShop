package com.myshop.api.service;

import com.myshop.api.domain.dto.request.CartRequest;
import com.myshop.api.domain.dto.response.data.CartData;
import com.myshop.api.domain.entity.Customer;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartService {
    List<CartData> getCartItemList(Customer customer);
    List<CartData> getSelectCartItemList(Customer customer, List<Long> itemIdList);
    void insertCartItem(Customer customer, CartRequest cartRequest);
    void updateCartItemQuantity(Customer customer, CartRequest cartRequest);
    void deleteCartItem(Customer customer, List<Long> itemId);
}
