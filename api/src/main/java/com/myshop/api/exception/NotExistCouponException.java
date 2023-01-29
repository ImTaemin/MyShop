package com.myshop.api.exception;

public class NotExistCouponException extends RuntimeException{
    public NotExistCouponException() {
        super("사용할 수 없는 쿠폰입니다.");
    }
}
