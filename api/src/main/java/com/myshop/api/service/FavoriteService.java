package com.myshop.api.service;

import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.Customer;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteService {
    List<ItemData.ItemSimple> getFavoriteItemList(Customer customer);
    void updateFavoriteItem(Customer customer, Long itemId);
}
