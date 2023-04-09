package com.myshop.api.repository;

import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Favorite;
import com.myshop.api.domain.entity.QFavorite;
import com.myshop.api.domain.entity.QItem;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class FavoriteRepositoryCustomImpl extends QuerydslRepositorySupport implements FavoriteRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    private final JPAQueryFactory jpaQueryFactory;

    QFavorite qFavorite = QFavorite.favorite;
    QItem qItem = QItem.item;

    FavoriteRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Favorite.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<ItemData.ItemSimple> getFavoriteItemList(Customer customer) {
        return jpaQueryFactory
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
                .fetch();
    }

    @Override
    public void updateFavoriteItem(Favorite findFavorite, Customer customer, Long itemId) {

        if(findFavorite == null) {
            // item을 구해서 Favorite 엔티티를 만드는 건 비효율적이라고 판단
            String sql = "INSERT INTO favorite (customer_id, item_id, create_date) VALUES (?,?,?)";
            entityManager.createNativeQuery(sql)
                            .setParameter(1, customer.getId())
                            .setParameter(2, itemId)
                            .setParameter(3, LocalDateTime.now())
                    .executeUpdate();
            return;
        }

        jpaQueryFactory.delete(qFavorite)
                .where(qFavorite.item().id.eq(itemId)
                        .and(qFavorite.customer().id.eq(customer.getId())))
                .execute();
    }
}
