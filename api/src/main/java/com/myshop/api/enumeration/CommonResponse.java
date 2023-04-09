package com.myshop.api.enumeration;

public enum CommonResponse {

    SUCCESS(true, "success"), FAIL(false, "fail");

    boolean status;
    String msg;

    CommonResponse(boolean status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public boolean getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
