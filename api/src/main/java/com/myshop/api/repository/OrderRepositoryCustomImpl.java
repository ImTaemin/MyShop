package com.myshop.api.repository;

import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.dto.response.data.OrderItemData;
import com.myshop.api.domain.entity.*;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrderRepositoryCustomImpl extends QuerydslRepositorySupport implements OrderRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    QItem qItem = QItem.item;
    QOrders qOrders = QOrders.orders;
    QOrderItem qOrderItem = QOrderItem.orderItem;

    public OrderRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Orders.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<OrderItemData> selectByCustom(Customer customer, Pageable pageable) {
        
        List<OrderItemData> resOrderItemList = jpaQueryFactory
                .select(
                        Projections.bean(OrderItemData.class,
                                qOrderItem.orders().id.as("orderNo"),
                                qOrderItem.quantity,
                                qOrderItem.payment,
                                qOrderItem.orderStatus,
                                qOrderItem.orderDate,
                                Projections.bean(ItemData.ItemSimple.class,
                                        qItem.id,
                                        qItem.name,
                                        qItem.brandName,
                                        qItem.mainImage).as("item")))
                .from(qOrders)
                .innerJoin(qOrderItem)
                .on(qOrders.id.eq(qOrderItem.orders().id))
                .innerJoin(qItem)
                .on(qOrderItem.item().id.eq(qItem.id))

                .where(qOrders.customer().id.eq(customer.getId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return resOrderItemList;
    }

    @Override
    public String findLastOrderIdByPrefix(String orderPrefix) {
        return jpaQueryFactory.select(qOrders.id)
                .from(qOrders)
                .where(qOrders.id.like(orderPrefix))
                .orderBy(qOrders.id.desc())
                .fetchOne();
    }

    @Override
    public Orders findOrderWithOrderItem(String orderId) {
        // TODO: 페치 조인으로 사용했더니 값을 안가져옴.. 나중에 확인
        Orders order =  jpaQueryFactory.selectFrom(qOrders)
                .where(qOrders.id.eq(orderId))
                .fetchOne();

        List<OrderItem> orderItemList = jpaQueryFactory.selectFrom(qOrderItem)
                .where(qOrderItem.orders().id.eq(orderId))
                .fetch();

        order.setOrderItemList(orderItemList);

        return order;
    }

    @Override
    public void deleteOrderById(String orderId) {
        // OrderItem을 먼저 삭제해야함
        jpaQueryFactory.delete(qOrderItem)
                .where(qOrderItem.orders().id.eq(orderId))
                .execute();

        jpaQueryFactory.delete(qOrders)
                .where(qOrders.id.eq(orderId))
                .execute();
    }
}
