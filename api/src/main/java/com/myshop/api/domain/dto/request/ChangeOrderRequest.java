package com.myshop.api.domain.dto.request;

import com.myshop.api.enumeration.OrderStatus;

import java.util.List;

public class ChangeOrderRequest {
    private List<String> orderNoList;
    private OrderStatus orderStatus;

    public List<String> getOrderNoList() {
        return orderNoList;
    }

    public void setOrderNoList(List<String> orderNoList) {
        this.orderNoList = orderNoList;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}