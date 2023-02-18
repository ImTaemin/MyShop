package com.myshop.api.repository;

import com.myshop.api.domain.dto.response.data.OrderItemData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Orders;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.enumeration.OrderStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepositoryCustom {
    List<OrderItemData> selectByCustomer(Customer customer, Pageable pageable);
    List<OrderItemData> selectByProvider(Provider provider, Pageable pageable, OrderStatus orderStatus);
    void changeOrders(List<String> orderNoList, OrderStatus orderStatus);
    String findLastOrderIdByPrefix(String orderPrefix);
    Orders findOrderWithOrderItem(String orderId);
    void deleteOrderById(String orderId);
}
