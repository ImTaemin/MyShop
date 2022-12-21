package com.myshop.api.service;

import com.myshop.api.dto.SignInResultDto;
import com.myshop.api.dto.SignUpResultDto;
import com.myshop.api.dto.provider.ProviderDto;

import javax.security.auth.login.AccountNotFoundException;

public interface SignService {
    SignUpResultDto signUp(ProviderDto providerParam);
    SignInResultDto signIn(String loginId, String password) throws AccountNotFoundException;

}
