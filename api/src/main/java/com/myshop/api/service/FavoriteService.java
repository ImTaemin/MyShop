package com.myshop.api.service;

import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.Customer;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface FavoriteService {
    PageImpl<ItemData.ItemSimple> getFavoriteItemList(Customer customer, Pageable pageable);
    void insertFavoriteItem(Customer customer, Long itemId);
    void deleteFavoriteItem(Customer customer, Long itemId);
}
