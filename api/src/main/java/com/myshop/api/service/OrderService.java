package com.myshop.api.service;

import com.myshop.api.domain.dto.pay.kakao.ReadyResponse;
import com.myshop.api.domain.dto.request.OrderRequest;
import com.myshop.api.domain.dto.response.data.OrderData;
import com.myshop.api.domain.dto.response.data.OrderItemData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Orders;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.enumeration.OrderStatus;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderData getOrdersByOrderId(Customer customer, String orderId);
    PageImpl<OrderItemData> getOrdersByCustomer(Customer customer, Pageable pageable);
    PageImpl<OrderItemData> getOrdersByProvider(Provider provider, Pageable pageable, OrderStatus orderStatus);
    void changeOrders(OrderRequest.OrderChange orderChangeRequest);
    ReadyResponse readyToKakaoPay(Customer customer, OrderRequest.Order orderRequest);
    void approveToKakaoPay(String pgToken, String orderId);
    void cancelToKakaoPay(String orderId);
    void failToKakaoPay(String orderId);
}
