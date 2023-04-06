package com.myshop.api.service;

import com.myshop.api.domain.dto.request.CartRequest;
import com.myshop.api.domain.dto.response.data.CartData;
import com.myshop.api.domain.entity.Cart;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Item;
import com.myshop.api.domain.entity.id.CartId;
import com.myshop.api.exception.ExistCartItemException;
import com.myshop.api.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CartData> getCartItemList(Customer customer) {
        return cartRepository.getCartItemList(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CartData> getSelectCartItemList(Customer customer, List<Long> itemIdList) {
        return cartRepository.getSelectCartItemList(customer, itemIdList);
    }

    @Transactional
    @Override
    public void insertCartItem(Customer customer, CartRequest cartRequest) {

        CartId cartId = CartId.builder()
                .item(cartRequest.getItemId())
                .customer(customer.getId())
                .build();

        // 장바구니에 해당 아이템이 있는 경우
        Optional<Cart> existCart = cartRepository.findById(cartId);
        if(existCart.isPresent()) {
            throw new ExistCartItemException();
        }

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
    public void deleteCartItem(Customer customer, List<Long> itemIdList) {
        cartRepository.deleteCartItems(customer, itemIdList);
    }
}
