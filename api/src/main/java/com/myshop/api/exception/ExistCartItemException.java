package com.myshop.api.exception;

public class ExistCartItemException extends RuntimeException{
    public ExistCartItemException() {
        super("장바구니에 이미 상품이 있습니다.");
    }
}
