package com.myshop.api.exception;

public class FavoriteNotFoundException extends RuntimeException{
    public FavoriteNotFoundException() {
        super("찜 목록에 존재하지 않는 상품입니다.");
    }
}
