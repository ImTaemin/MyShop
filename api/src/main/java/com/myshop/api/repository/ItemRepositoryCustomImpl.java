package com.myshop.api.repository;

import com.myshop.api.domain.dto.request.ItemRequest;
import com.myshop.api.domain.entity.Item;
import com.myshop.api.domain.entity.ItemImage;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.domain.entity.QItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

// ItemRepositoryCustom에 정의한 메서드 내용(실제 쿼리) 작성
@Component
public class ItemRepositoryCustomImpl extends QuerydslRepositorySupport implements ItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    QItem qItem = QItem.item;

    public ItemRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory, EntityManager entityManager) {
        super(Item.class);
        this.jpaQueryFactory = jpaQueryFactory;
        this.entityManager = entityManager;
    }

    @Override
    public List<Item> selectByBrandName(String brandName, Pageable pageable) {

        return jpaQueryFactory.select(qItem)
                .from(qItem)
                .where(qItem.provider().brandName.eq(brandName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public void modifyByProviderItems(Provider provider, ItemRequest.Item item, List<ItemImage> itemImageList) {

        jpaQueryFactory.update(qItem)
                .set(qItem.code, item.getCode())
                .set(qItem.name, item.getName())
                .set(qItem.price, item.getPrice())
                .set(qItem.quantity, item.getQuantity())
                .set(qItem.content, item.getContent())
                .set(qItem.itemType, item.getItemType())
                .set(qItem.genderType, item.getGenderType())
                .where(qItem.provider().id.eq(provider.getId())
                        .and(qItem.id.eq(item.getId())))
                .execute();

        itemImageList.forEach(entityManager::persist);
    }

    @Override
    public void modifyByPriceAndQuantity(Provider provider, ItemRequest.PriceAndQuantity priceAndQuantity) {
        jpaQueryFactory.update(qItem)
                .set(qItem.price, priceAndQuantity.getPrice())
                .set(qItem.quantity, priceAndQuantity.getQuantity())
                .where(qItem.provider().id.eq(provider.getId())
                        .and(qItem.id.eq(priceAndQuantity.getId())))
                .execute();
    }

    @Override
    public Long deleteByProviderItems(Long providerId, List<Long> itemIds) {
        return jpaQueryFactory.delete(qItem)
                .where(qItem.id.in(itemIds)
                        .and(qItem.provider().id.eq(providerId)))
                .execute();
    }
}
