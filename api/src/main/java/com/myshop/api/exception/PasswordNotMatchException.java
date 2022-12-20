package com.myshop.api.exception;

public class PasswordNotMatchException extends RuntimeException{

    public PasswordNotMatchException() {
    }

    public PasswordNotMatchException(String message) {
        super(message);
    }
}
