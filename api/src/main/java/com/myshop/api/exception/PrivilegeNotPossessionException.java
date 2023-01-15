package com.myshop.api.exception;

public class PrivilegeNotPossessionException extends RuntimeException{

    public PrivilegeNotPossessionException() {
        super("권한이 없습니다.");
    }
}
