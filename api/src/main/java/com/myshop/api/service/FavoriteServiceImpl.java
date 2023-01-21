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

    @Override
    public PageImpl<ItemData.ItemSimple> getFavoriteItemList(Customer customer, Pageable pageable) {
        List<ItemData.ItemSimple> favoriteItemList = favoriteRepository.getFavoriteItemList(customer, pageable);

        return new PageImpl<>(favoriteItemList, pageable, favoriteItemList.size());
    }

    @Transactional
    @Override
    public void insertFavoriteItem(Customer customer, Long itemId) {
        Item insertItem = Item.builder()
                .id(itemId)
                .build();

        Favorite favorite = Favorite.builder()
                .item(insertItem)
                .customer(customer)
                .build();

        favoriteRepository.save(favorite);
    }

    @Transactional
    @Override
    public void deleteFavoriteItem(Customer customer, Long itemId) {
        FavoriteId favoriteId = FavoriteId.builder()
                .item(itemId)
                .customer(customer.getId())
                .build();

        favoriteRepository.deleteById(favoriteId);
    }
}
