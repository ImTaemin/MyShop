package com.myshop.api.repository;

import com.myshop.api.domain.dto.request.CartRequest;
import com.myshop.api.domain.dto.response.data.CartData;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.Customer;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartRepositoryCustom {
    List<CartData> getCartItemList(Customer customer, Pageable pageable);
    void directOrderToSave(Customer customer, Long itemId, int quantity);
    void updateCartItemQuantity(Customer customer, CartRequest cartRequest);
}
