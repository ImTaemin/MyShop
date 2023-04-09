package com.myshop.api.exception;

public class EmptyPasswordException extends RuntimeException{
    public EmptyPasswordException() {
        super("패스워드가 비어있습니다.");
    }
}
