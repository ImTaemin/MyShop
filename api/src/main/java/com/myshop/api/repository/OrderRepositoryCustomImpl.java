package com.myshop.api.repository;

import com.myshop.api.domain.dto.request.OrderRequest;
import com.myshop.api.domain.dto.response.data.CouponData;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.dto.response.data.OrderData;
import com.myshop.api.domain.dto.response.data.OrderItemData;
import com.myshop.api.domain.entity.*;
import com.myshop.api.enumeration.OrderStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    QCoupon qCoupon = QCoupon.coupon;

    public OrderRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Orders.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public OrderData selectOrdersByOrderId(Customer customer, String orderId) {
        // 주문 번호에 해당하는 주문 상세 조회
        OrderData orderData = jpaQueryFactory
                .select(Projections.bean(OrderData.class,
                        qOrders.id.as("orderNo"),
                        qOrders.totalPayment,
                        qOrders.orderDate,
                        qOrders.cancelDate,
                        qOrders.address().roadName,
                        qOrders.address().detail,
                        qOrders.address().postalCode
                    )
                )
                .from(qOrders)
                .where(qOrders.id.eq(orderId)
                        .and(qOrders.customer().id.eq(customer.getId())))
                .fetchOne();

        List<OrderItemData> orderItemDataList = jpaQueryFactory
                .select(
                    Projections.constructor(OrderItemData.class,
                            qOrderItem.cnt,
                            qOrderItem.orders().id.as("orderNo"),
                            qOrderItem.quantity,
                            qOrderItem.payment,
                            qOrderItem.orderStatus,
                            qOrderItem.orders().orderDate,
                            Projections.constructor(ItemData.ItemSimple.class,
                                    qItem.id,
                                    qItem.code,
                                    qItem.name,
                                    qItem.brandName,
                                    qItem.price,
                                    qItem.mainImage
                            ),
                            Projections.constructor(CouponData.class,
                                    qCoupon.id,
                                    qCoupon.code,
                                    qCoupon.content,
                                    qCoupon.expirationDate,
                                    Expressions.numberTemplate(
                                            Integer.class, "{0}",
                                            qCoupon.discount.multiply(100).intValue())
                            )
                    )
                )
                .from(qOrderItem)
                .leftJoin(qOrderItem.item(), qItem)
                .leftJoin(qOrderItem.coupon(), qCoupon)
                .where(qOrderItem.orders().id.eq(orderId))
                .fetch();

        orderData.setOrderItemDataList(orderItemDataList);

        return orderData;
    }

    @Override
    public Page<OrderItemData> selectByCustomer(Customer customer, Pageable pageable) {

        List<OrderItemData> customerOrderList = jpaQueryFactory
                .select(
                        Projections.bean(OrderItemData.class,
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

                .where(qOrders.customer().id.eq(customer.getId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qOrders.orderDate.desc())
                .fetch();

        Long cnt = jpaQueryFactory.select(qOrderItem.count())
                .from(qOrders)
                .innerJoin(qOrderItem)
                .on(qOrders.id.eq(qOrderItem.orders().id))
                .innerJoin(qItem)
                .on(qOrderItem.item().id.eq(qItem.id))
                .where(qOrders.customer().id.eq(customer.getId()))
                .fetchOne();

        return new PageImpl<>(customerOrderList, pageable, cnt);
    }

    @Override
    public Page<OrderItemData> selectByProvider(Provider provider, Pageable pageable, OrderStatus orderStatus) {

        List<OrderItemData> providerOrderList = jpaQueryFactory
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

        Long cnt = jpaQueryFactory.select(qOrderItem.count())
                .from(qOrderItem)
                .where(qItem.provider().id.eq(provider.getId())
                        .and(qOrderItem.orderStatus.eq(orderStatus)))
                .fetchOne();

        return new PageImpl<>(providerOrderList, pageable, cnt);
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
