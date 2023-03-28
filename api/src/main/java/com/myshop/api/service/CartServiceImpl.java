package com.myshop.api.service;

import com.myshop.api.domain.dto.request.CartRequest;
import com.myshop.api.domain.dto.response.data.CartData;
import com.myshop.api.domain.entity.Cart;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Item;
import com.myshop.api.domain.entity.id.CartId;
import com.myshop.api.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    public PageImpl<CartData> getCartItemList(Customer customer, Pageable pageable) {
        List<CartData> cartItemList = cartRepository.getCartItemList(customer, pageable);

        return new PageImpl<>(cartItemList, pageable, cartItemList.size());
    }

    @Transactional
    @Override
    public void insertCartItem(Customer customer, CartRequest cartRequest) {
        Item item = Item.builder()
                .id(cartRequest.getItemId())
                .build();

        Cart cart = Cart.builder()
                .item(item)
                .customer(customer)
                .updateDate(LocalDateTime.now())
                .quantity(cartRequest.getQuantity())
                .build();

        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void updateCartItemQuantity(Customer customer, CartRequest cartRequest) {
        cartRepository.updateCartItemQuantity(customer, cartRequest);
    }

    @Transactional
    @Override
    public void deleteCartItem(Customer customer, Long itemId) {
        CartId cartId = CartId.builder()
                .item(itemId)
                .customer(customer.getId())
                .build();

        cartRepository.deleteById(cartId);
    }
}
