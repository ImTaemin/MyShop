package com.myshop.api.service;

import com.myshop.api.domain.entity.Customer;

public interface WishService {
    void insertWishItem(Customer customer, Long itemId);
    void deleteWishItem(Customer customer, Long itemId);
}
