package com.myshop.api.repository;

import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.response.data.OrderItemData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.OrderItem;
import com.myshop.api.domain.entity.Orders;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepositoryCustom {
    List<OrderItemData> selectByCustom(Customer customer, Pageable pageable);
    String findLastOrderIdByPrefix(String orderPrefix);
    Orders findOrderWithOrderItem(String orderId);
    void deleteOrderById(String orderId);
}
