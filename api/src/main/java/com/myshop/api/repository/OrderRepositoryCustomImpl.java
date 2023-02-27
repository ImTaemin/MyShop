package com.myshop.api.repository;

import com.myshop.api.domain.dto.request.OrderRequest;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.dto.response.data.OrderItemData;
import com.myshop.api.domain.entity.*;
import com.myshop.api.enumeration.OrderStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public List<OrderItemData> selectByCustomer(Customer customer, Pageable pageable) {

        return jpaQueryFactory
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
                                        qItem.code,
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
                .orderBy(qOrders.orderDate.desc())
                .fetch();
    }

    @Override
    public List<OrderItemData> selectByProvider(Provider provider, Pageable pageable, OrderStatus orderStatus) {

        return jpaQueryFactory
                .select(
                        Projections.bean(OrderItemData.class,
                                qOrderItem.cnt,
                                qOrderItem.orders().id.as("orderNo"),
                                qOrderItem.quantity,
                                qOrderItem.payment,
                                qOrderItem.orderStatus,
                                qOrderItem.orderDate,
                                Projections.bean(ItemData.ItemSimple.class,
                                        qItem.id,
                                        qItem.code,
                                        qItem.name,
                                        qItem.brandName,
                                        qItem.mainImage).as("item")))
                .from(qOrders)
                .innerJoin(qOrderItem)
                .on(qOrders.id.eq(qOrderItem.orders().id))
                .innerJoin(qItem)
                .on(qOrderItem.item().id.eq(qItem.id))

                .where(qItem.provider().id.eq(provider.getId())
                        .and(qOrderItem.orderStatus.eq(orderStatus)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qOrders.orderDate.desc())
                .fetch();
    }

    @Override
    public void changeOrders(List<OrderRequest.OrderNoCnt> orderNoCntList, OrderStatus orderStatus) {
        for (OrderRequest.OrderNoCnt orderNoCnt: orderNoCntList) {
            jpaQueryFactory.update(qOrderItem)
                    .set(qOrderItem.orderStatus, orderStatus)
                    .where(qOrderItem.orders().id.eq(orderNoCnt.getOrderNo())
                            .and(qOrderItem.cnt.eq(orderNoCnt.getCnt())))
                    .execute();
        }
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
