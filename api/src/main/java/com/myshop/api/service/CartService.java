package com.myshop.api.service;

import com.myshop.api.domain.dto.request.CartRequest;
import com.myshop.api.domain.dto.response.data.CartData;
import com.myshop.api.domain.entity.Customer;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface CartService {
    PageImpl<CartData> getCartItemList(Customer customer, Pageable pageable);
    void insertCartItem(Customer customer, CartRequest cartRequest);
    void updateCartItemQuantity(Customer customer, CartRequest cartRequest);
    void deleteCartItem(Customer customer, Long itemId);
}
