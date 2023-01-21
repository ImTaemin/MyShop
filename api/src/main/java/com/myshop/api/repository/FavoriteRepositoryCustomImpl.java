package com.myshop.api.repository;

import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Favorite;
import com.myshop.api.domain.entity.QFavorite;
import com.myshop.api.domain.entity.QItem;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FavoriteRepositoryCustomImpl extends QuerydslRepositorySupport implements FavoriteRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    QFavorite qFavorite = QFavorite.favorite;
    QItem qItem = QItem.item;

    FavoriteRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Favorite.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<ItemData.ItemSimple> getFavoriteItemList(Customer customer, Pageable pageable) {
        List<ItemData.ItemSimple> resFavoriteList = jpaQueryFactory
                .select(
                        Projections.bean(ItemData.ItemSimple.class,
                                qItem.id,
                                qItem.name,
                                qItem.brandName,
                                qItem.price,
                                qItem.mainImage))
                .from(qFavorite)
                .join(qFavorite.item(), qItem)
                .where(qFavorite.customer().id.eq(customer.getId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return resFavoriteList;
    }
}
