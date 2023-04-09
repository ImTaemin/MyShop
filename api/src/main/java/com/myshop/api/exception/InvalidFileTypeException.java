package com.myshop.api.exception;

public class InvalidFileTypeException extends RuntimeException{
    public InvalidFileTypeException() {
        super("이미지 파일만 업로드 가능합니다.");
    }
}
