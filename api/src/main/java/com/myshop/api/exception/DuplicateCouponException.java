package com.myshop.api.exception;

public class DuplicateCouponException extends RuntimeException{
    public DuplicateCouponException() {
        super("쿠폰 코드가 중복입니다.");
    }
}
