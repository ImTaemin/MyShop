package com.myshop.api.exception;

public class NotExistFavoriteException extends RuntimeException{
    public NotExistFavoriteException() {
        super("찜 목록에 존재하지 않는 상품입니다.");
    }
}
