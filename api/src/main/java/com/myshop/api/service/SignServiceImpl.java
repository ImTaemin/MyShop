package com.myshop.api.service;

import com.myshop.api.config.security.JwtTokenProvider;
import com.myshop.api.domain.dto.request.CustomerRequest;
import com.myshop.api.domain.dto.request.ProviderRequest;
import com.myshop.api.domain.dto.request.UserDto;
import com.myshop.api.domain.dto.response.data.SignData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.enumeration.CommonResponse;
import com.myshop.api.exception.PasswordNotMatchException;
import com.myshop.api.repository.CustomerRepository;
import com.myshop.api.repository.ProviderRepository;
import com.myshop.api.util.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SignServiceImpl implements SignService {

    private final Logger LOGGER = LoggerFactory.getLogger(SignServiceImpl.class);

    private final ProviderRepository providerRepository;
    private final CustomerRepository customerRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public SignData.SignUpResponse signUp(UserDto userRequest) {
        PasswordEncryptor.bcrypt(userRequest);

        UserDetails savedUser = saveUser(userRequest);

        SignData.SignUpResponse signUpResponse = new SignData.SignUpResponse();
        if(!savedUser.getUsername().isEmpty()) {
            setSuccessResult(signUpResponse);
            LOGGER.info("회원가입 성공");
        } else {
            setFailResult(signUpResponse);
            LOGGER.info("회원가입 실패");
        }

        return signUpResponse;
    }

    @Override
    public SignData.SignInResponse signInProvider(String userId, String password) throws AccountNotFoundException {
        Provider dbProvider = providerRepository.findByUserId(userId).orElseThrow(AccountNotFoundException::new);

        return signIn(userId, password, dbProvider.getPassword(), dbProvider.getRoles());
    }

    @Override
    public SignData.SignInResponse signInCustomer(String userId, String password) throws AccountNotFoundException {
        Customer dbCustomer = customerRepository.findByUserId(userId).orElseThrow(AccountNotFoundException::new);

        return signIn(userId, password, dbCustomer.getPassword(), dbCustomer.getRoles());
    }


    // 판매자, 구매자 구별 없는 회원가입 메서드
    private UserDetails saveUser(UserDto userDto) {
        if(userDto instanceof CustomerRequest) {
            // 구매자
            CustomerRequest customerRequest = (CustomerRequest) userDto;
            Customer signUpCustomer = Customer.builder()
                    .userId(customerRequest.getUserId())
                    .password(customerRequest.getPassword())
                    .phone(customerRequest.getPhone())
                    .name(customerRequest.getName())
                    .roles(customerRequest.getRoles())
                    .build();

            return customerRepository.save(signUpCustomer);
        }

        // 판매자
        ProviderRequest providerRequest = (ProviderRequest) userDto;
        Provider signUpProvider = Provider.builder()
                .userId(providerRequest.getUserId())
                .password(providerRequest.getPassword())
                .phone(providerRequest.getPhone())
                .brandName(providerRequest.getBrandName())
                .roles(providerRequest.getRoles())
                .build();

        return providerRepository.save(signUpProvider);
    }

    // 판매자, 구매자 구별 없는 로그인 메서드
    private SignData.SignInResponse signIn(String userId, String password, String dbPassword, Set<String> roles) {
        if(!PasswordEncryptor.isMatchBcrypt(password, dbPassword)) {
            LOGGER.info("패스워드 불일치");
            throw new PasswordNotMatchException();
        }

        SignData.SignInResponse signInResult = new SignData.SignInResponse();
        setSuccessResult(signInResult);
        signInResult.setToken(jwtTokenProvider.createToken(userId, roles));

        LOGGER.info("로그인 성공");
        return signInResult;
    }

    private void setSuccessResult(SignData.SignUpResponse signUpResponse) {
        signUpResponse.setSuccess(true);
        signUpResponse.setCode(CommonResponse.SUCCESS.getCode());
        signUpResponse.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setFailResult(SignData.SignUpResponse signUpResponse) {
        signUpResponse.setSuccess(false);
        signUpResponse.setCode(CommonResponse.FAIL.getCode());
        signUpResponse.setMsg(CommonResponse.FAIL.getMsg());
    }
}
