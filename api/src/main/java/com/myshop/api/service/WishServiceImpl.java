package com.myshop.api.service;

import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Item;
import com.myshop.api.domain.entity.Wish;
import com.myshop.api.exception.NotExistWishException;
import com.myshop.api.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class WishServiceImpl implements WishService{

    private final WishRepository wishRepository;

    @Transactional
    @Override
    public void insertWishItem(Customer customer, Long itemId) {
        Item item = Item.builder()
                .id(itemId)
                .build();

        Wish wish = Wish.builder()
                .item(item)
                .customer(customer)
                .build();

        wishRepository.save(wish);
    }

    @Transactional
    @Override
    public void deleteWishItem(Customer customer, Long itemId) {
        Wish wish = wishRepository.findByCustomerAndItemId(customer, itemId)
                .orElseThrow(NotExistWishException::new);

        wishRepository.delete(wish);
    }
}
