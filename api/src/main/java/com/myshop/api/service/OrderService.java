package com.myshop.api.service;

import com.myshop.api.domain.dto.pay.kakao.ReadyResponse;
import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.request.OrderRequest;
import com.myshop.api.domain.dto.response.data.OrderItemData;
import com.myshop.api.domain.entity.Customer;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    PageImpl<OrderItemData> getOrderByCustomer(Customer customer, Pageable pageable);
    ReadyResponse readyToKakaoPay(Customer customer, OrderRequest.Order orderRequest);
    void approveToKakaoPay(String pgToken, String orderId);
    void cancelToKakaoPay(String orderId);
    void failToKakaoPay(String orderId);
}