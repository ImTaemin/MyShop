package com.myshop.api.exception;

public class EmptyPasswordException extends RuntimeException{

    public EmptyPasswordException() {
        super();
    }

    public EmptyPasswordException(String message) {
        super(message);
    }
}
