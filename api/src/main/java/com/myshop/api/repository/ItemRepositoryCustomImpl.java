package com.myshop.api.repository;

import com.myshop.api.domain.dto.request.ItemRequest;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.*;
import com.myshop.api.enumeration.ItemType;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// ItemRepositoryCustom에 정의한 메서드 내용(실제 쿼리) 작성
@Component
public class ItemRepositoryCustomImpl extends QuerydslRepositorySupport implements ItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @PersistenceContext
    EntityManager entityManager;

    QItem qItem = QItem.item;
    QProvider qProvider = QProvider.provider;

    public ItemRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Item.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<ItemData.ItemSimple> selectByItemIdList(List<Long> itemIdList) {
        return jpaQueryFactory.select(
                Projections.bean(ItemData.ItemSimple.class,
                        qItem.id,
                        qItem.code,
                        qItem.name,
                        qItem.brandName,
                        qItem.price,
                        qItem.mainImage))
                .from(qItem)
                .where(qItem.id.in(itemIdList))
                .fetch();
    }

    @Override
    public List<ItemData.ItemSimple> selectByBrandName(String brandName, Pageable pageable) {

        return jpaQueryFactory.select(
                Projections.bean(ItemData.ItemSimple.class,
                                qItem.id,
                                qItem.code,
                                qItem.name,
                                qItem.brandName,
                                qItem.price,
                                qItem.mainImage))
                .from(qItem)
                .where(qItem.provider().brandName.eq(brandName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qItem.createDate.desc())
                .fetch();
    }

    @Override
    public List<ItemData.ItemSimple> selectByItemType(ItemType itemType, Pageable pageable) {
        return jpaQueryFactory.select(
                        Projections.bean(ItemData.ItemSimple.class,
                                qItem.id,
                                qItem.code,
                                qItem.name,
                                qItem.brandName,
                                qItem.price,
                                qItem.mainImage))
                .from(qItem)
                .where(qItem.itemType.eq(itemType))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qItem.createDate.desc())
                .fetch();
    }

    @Override
    public List<ItemData.Item> selectByProvider(Provider provider, Pageable pageable) {

        return jpaQueryFactory.select(
                Projections.bean(ItemData.Item.class,
                        qItem.id,
                        qItem.code,
                        qItem.name,
                        qItem.brandName,
                        qItem.price,
                        qItem.mainImage,
                        qItem.quantity,
                        qItem.itemType,
                        qItem.genderType,
                        qItem.createDate))
                .from(qItem)
                .where(qProvider.id.eq(provider.getId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qItem.createDate.desc())
                .fetch();
    }

    @Override
    public void modifyByProviderItems(Provider provider, ItemRequest.Item item, List<ItemImage> itemImageList) {

        jpaQueryFactory.update(qItem)
                .set(qItem.code, item.getCode())
                .set(qItem.name, item.getName())
                .set(qItem.price, item.getPrice())
                .set(qItem.quantity, item.getQuantity())
                .set(qItem.itemType, item.getItemType())
                .set(qItem.genderType, item.getGenderType())
                .where(qItem.provider().id.eq(provider.getId())
                        .and(qItem.id.eq(Long.parseLong(item.getId()))))
                .execute();

        itemImageList.forEach(entityManager::persist);
    }

    @Override
    public void modifyByPriceAndQuantity(Provider provider, ItemRequest.PriceAndQuantity priceAndQuantity) {
        jpaQueryFactory.update(qItem)
                .set(qItem.price, priceAndQuantity.getPrice())
                .set(qItem.quantity, priceAndQuantity.getQuantity())
                .where(qItem.provider().id.eq(provider.getId())
                        .and(qItem.id.eq(Long.parseLong(priceAndQuantity.getId()))))
                .execute();
    }

    @Override
    public Long deleteByProviderItems(Long providerId, List<Long> itemIds) {
        return jpaQueryFactory.delete(qItem)
                .where(qItem.id.in(itemIds)
                        .and(qItem.provider().id.eq(providerId)))
                .execute();
    }

    @Override
    public Boolean existsByItemCode(String brandName, String itemCode) {
        String findItemCode = jpaQueryFactory.select(qItem.code)
                .from(qItem)
                .where(qItem.code.eq(itemCode)
                        .and(qItem.brandName.eq(brandName)))
                .fetchFirst();

        return findItemCode != null;
    }
}
