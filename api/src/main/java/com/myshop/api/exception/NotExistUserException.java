package com.myshop.api.exception;

public class NotExistUserException extends RuntimeException{
    public NotExistUserException() {
        super("회원 정보가 없습니다.");
    }
}
