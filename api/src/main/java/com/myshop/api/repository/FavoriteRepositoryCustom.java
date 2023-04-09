package com.myshop.api.repository;

import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Favorite;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteRepositoryCustom {
    List<ItemData.ItemSimple> getFavoriteItemList(Customer customer);
    void updateFavoriteItem(Favorite findFavorite, Customer customer, Long itemId);
}
