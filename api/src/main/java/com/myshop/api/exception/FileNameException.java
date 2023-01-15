package com.myshop.api.exception;

public class FileNameException extends RuntimeException{
    public FileNameException() {
        super("파일명이 존재하지 않습니다.");
    }
}
