package com.myshop.api.repository;

import com.myshop.api.domain.dto.request.CartRequest;
import com.myshop.api.domain.dto.response.data.CartData;
import com.myshop.api.domain.entity.Cart;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.QCart;
import com.myshop.api.domain.entity.QItem;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartRepositoryCustomImpl extends QuerydslRepositorySupport implements CartRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QCart qCart = QCart.cart;
    QItem qItem = QItem.item;

    public CartRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Cart.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<CartData> getCartItemList(Customer customer) {
        return jpaQueryFactory
                .select(
                        Projections.bean(CartData.class,
                                qItem.id,
                                qItem.name,
                                qItem.brandName,
                                qItem.price,
                                qItem.mainImage,
                                qCart.quantity))
                .from(qCart)
                .join(qCart.item(), qItem)
                .where(qCart.customer().id.eq(customer.getId()))
                .fetch();
    }

    @Override
    public void updateCartItemQuantity(Customer customer, CartRequest cartRequest) {
        jpaQueryFactory.update(qCart)
                .set(qCart.quantity, cartRequest.getQuantity())
                .where(qCart.customer().id.eq(customer.getId())
                        .and(qCart.item().id.eq(cartRequest.getItemId())))
                .execute();
    }

}
