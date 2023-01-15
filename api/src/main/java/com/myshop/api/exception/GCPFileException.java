package com.myshop.api.exception;

public class GCPFileException extends RuntimeException{
    public GCPFileException() {
        super("클라우드 파일 처리 중 오류가 발생했습니다.");
    }
}
