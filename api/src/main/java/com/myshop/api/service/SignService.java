package com.myshop.api.service;

import com.myshop.api.dto.SignInResultDto;
import com.myshop.api.dto.SignUpResultDto;
import com.myshop.api.dto.UserDto;

import javax.security.auth.login.AccountNotFoundException;

public interface SignService {
    // 아래 UserDto 파라미터 받는 것으로 리팩토링 함
//    SignUpResultDto signUp(ProviderDto providerParam);
    SignUpResultDto signUp(UserDto signUpParam);
    SignInResultDto signInProvider(String userId, String password) throws AccountNotFoundException;
    SignInResultDto signInCustomer(String userId, String password) throws AccountNotFoundException;

}
