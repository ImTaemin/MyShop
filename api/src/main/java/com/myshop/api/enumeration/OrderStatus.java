package com.myshop.api.enumeration;


public enum OrderStatus {
    REQUESTED,      // 주문 요청
    PAY_SUCCESS,    // 결제 완료
    RECEIVED,       // 주문 접수
    CANCELED,       // 주문 취소
    DELIVERING,     // 배송 중
    DELIVERED,      // 배송 완료
}
