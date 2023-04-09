package com.myshop.api.repository;

import com.myshop.api.domain.dto.request.OrderRequest;
import com.myshop.api.domain.dto.response.data.OrderData;
import com.myshop.api.domain.dto.response.data.OrderItemData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Orders;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.enumeration.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepositoryCustom {
    OrderData selectOrdersByOrderId(Customer customer, String orderId);
    Page<OrderItemData> selectByCustomer(Customer customer, Pageable pageable);
    Page<OrderItemData> selectByProvider(Provider provider, Pageable pageable, OrderStatus orderStatus);
    void changeOrders(List<OrderRequest.OrderNoCnt> orderNoCntList, OrderStatus orderStatus);
    String findLastOrderIdByPrefix(String orderPrefix);
    Orders findOrderWithOrderItem(String orderId);
    void deleteOrderById(String orderId);
}
