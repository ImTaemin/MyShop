package com.myshop.api.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("회원 정보가 없습니다.");
    }
}
