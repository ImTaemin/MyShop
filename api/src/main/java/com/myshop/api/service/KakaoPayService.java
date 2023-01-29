package com.myshop.api.service;

import com.myshop.api.domain.dto.pay.kakao.ReadyResponse;
import com.myshop.api.domain.dto.request.OrderRequest;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Orders;

public interface KakaoPayService {
    ReadyResponse ready(Customer customer, String orderId, OrderRequest.Order orderRequest);
    void approve(String pgToken, Orders order);
}
