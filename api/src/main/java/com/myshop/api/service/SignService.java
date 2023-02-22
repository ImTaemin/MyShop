package com.myshop.api.service;

import com.myshop.api.domain.dto.request.UserDto;
import com.myshop.api.domain.dto.response.data.SignData;

import javax.security.auth.login.AccountNotFoundException;

public interface SignService {
    SignData.SignUpResponse signUp(UserDto signUpParam);
    SignData.SignInResponse signInProvider(String userId, String password) throws AccountNotFoundException;
    SignData.SignInResponse signInCustomer(String userId, String password) throws AccountNotFoundException;
    SignData.SignInResponse reissueAccessToken(String refreshToken) ;

}
