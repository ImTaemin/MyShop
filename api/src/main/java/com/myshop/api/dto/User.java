package com.myshop.api.dto;


// 패스워드 암호화를 위한 인터페이스. 없애려면 PasswordEncryptor에 String 반환하는 방식으로 변경하면 됨.
public interface User {

    String getPassword();
    void setPassword(String password);
}
