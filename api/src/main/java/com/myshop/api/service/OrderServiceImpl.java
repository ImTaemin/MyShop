package com.myshop.api.service;

import com.myshop.api.domain.dto.pay.kakao.ReadyResponse;
import com.myshop.api.domain.dto.request.OrderRequest;
import com.myshop.api.domain.dto.response.data.OrderData;
import com.myshop.api.domain.dto.response.data.OrderItemData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Orders;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.enumeration.OrderStatus;
import com.myshop.api.repository.CartRepository;
import com.myshop.api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    private final KakaoPayService kakaoPayService;

    @Override
    public OrderData getOrdersByOrderId(Customer customer, String orderId) {
        return orderRepository.selectOrdersByOrderId(customer, orderId);
    }

    @Transactional
    @Override
    public Page<OrderItemData> getOrdersByCustomer(Customer customer, Pageable pageable) {
        Page<OrderItemData> orderItemList = orderRepository.selectByCustomer(customer, pageable);

        return orderItemList;
    }

    @Transactional
    @Override
    public Page<OrderItemData> getOrdersByProvider(Provider provider, Pageable pageable , OrderStatus orderStatus) {
        Page<OrderItemData> orderItemList = orderRepository.selectByProvider(provider, pageable, orderStatus);

        return orderItemList;
    }

    @Transactional
    @Override
    public void changeOrders(OrderRequest.OrderChange orderChangeRequest) {
        orderRepository.changeOrders(orderChangeRequest.getOrderNoCntList(), orderChangeRequest.getOrderStatus());
    }

    @Transactional
    @Override
    public ReadyResponse readyToKakaoPay(Customer customer, OrderRequest.Order orderRequest) {
        String orderId = this.createOrderId();

        ReadyResponse readyResponse = null;
        // 주문한 상품들 DB에 저장 및 주문번호, redirectUrl 가져옴
        if(orderRequest.getPayMethod().equals("KAKAOPAY")) {
            readyResponse = kakaoPayService.ready(customer, orderId, orderRequest);
        }

        List<Long> itemIdList = orderRequest.getOrderItemList()
                .stream()
                .map(OrderRequest.OrderItem::getItemId)
                .collect(Collectors.toList());

        orderRepository.save(readyResponse.getOrder());
        cartRepository.deleteByCustomerAndItemIdIn(customer, itemIdList);
        readyResponse.setOrder(null);

        return readyResponse;
    }

    @Transactional
    @Override
    public void approveToKakaoPay(String pgToken, String orderId) {
        Orders order = orderRepository.findOrderWithOrderItem(orderId);

        kakaoPayService.approve(pgToken, order);
    }

    @Transactional
    @Override
    public void cancelToKakaoPay(String orderId) {
        orderRepository.deleteOrderById(orderId);
    }

    @Transactional
    @Override
    public void failToKakaoPay(String orderId) {
        this.cancelToKakaoPay(orderId);
    }

    private String createOrderId() {
        LocalDateTime now = LocalDateTime.now();
        String orderPrefix = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String lastOrderId = orderRepository.findLastOrderIdByPrefix(orderPrefix);

        return this.orderIdGenerator(orderPrefix, lastOrderId);
    }

    private String orderIdGenerator(String prefix, String lastOrderId) {
        // 현재 시간(초까지 포함)에 해당하는 주문 순서
        int nextSequence = lastOrderId != null
                ? Integer.parseInt(lastOrderId.substring(prefix.length())) + 1
                : 1;

        // 연월일시분초+000X. ex) 202301231130550001
        DecimalFormat decimalFormat = new DecimalFormat("0000");
        return prefix + decimalFormat.format(nextSequence);
    }
}
