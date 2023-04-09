package com.myshop.api.service;

import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Favorite;
import com.myshop.api.domain.entity.Item;
import com.myshop.api.domain.entity.id.FavoriteId;
import com.myshop.api.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ItemData.ItemSimple> getFavoriteItemList(Customer customer) {
        return favoriteRepository.getFavoriteItemList(customer);
    }

    @Transactional
    @Override
    public void updateFavoriteItem(Customer customer, Long itemId) {
        Favorite findFavorite = favoriteRepository.findFavoriteByCustomerAndItemId(customer, itemId);
        favoriteRepository.updateFavoriteItem(findFavorite, customer, itemId);
    }
}
