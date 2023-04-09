package com.myshop.api.exception;

public class IllegalOrderException extends RuntimeException{
    public IllegalOrderException() {
        super("주문번호에 해당하는 상품이 없습니다.");
    }
}
