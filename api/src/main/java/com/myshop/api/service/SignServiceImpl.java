package com.myshop.api.service;

import com.myshop.api.config.security.JwtTokenProvider;
import com.myshop.api.domain.Customer;
import com.myshop.api.domain.Provider;
import com.myshop.api.dto.SignInResultDto;
import com.myshop.api.dto.SignUpResultDto;
import com.myshop.api.dto.UserDto;
import com.myshop.api.dto.customer.CustomerDto;
import com.myshop.api.dto.provider.ProviderDto;
import com.myshop.api.enumeration.CommonResponse;
import com.myshop.api.exception.PasswordNotMatchException;
import com.myshop.api.mapper.CustomerMapper;
import com.myshop.api.mapper.ProviderMapper;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class SignServiceImpl implements SignService {

    private final Logger LOGGER = LoggerFactory.getLogger(SignServiceImpl.class);

    private final ProviderRepository providerRepository;
    private final CustomerRepository customerRepository;
    private final ProviderMapper providerMapper;
    private final CustomerMapper customerMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public SignUpResultDto signUp(UserDto signUpParam) {
        PasswordEncryptor.bcrypt(signUpParam);

        UserDetails savedUser = this.saveUser(signUpParam);

        SignUpResultDto signUpResultDto = new SignUpResultDto();
        if(!savedUser.getUsername().isEmpty()) {
            setSuccessResult(signUpResultDto);
            LOGGER.info("회원가입 성공");
        } else {
            setFailResult(signUpResultDto);
            LOGGER.info("회원가입 실패");
        }

        return signUpResultDto;
    }

    @Override
    public SignInResultDto signInProvider(String userId, String password) throws AccountNotFoundException {
        Provider dbProvider = providerRepository.findByUserId(userId).orElseThrow(AccountNotFoundException::new);

        return signIn(userId, password, dbProvider.getPassword(), dbProvider.getRoles());
    }

    @Override
    public SignInResultDto signInCustomer(String userId, String password) throws AccountNotFoundException {
        Customer dbCustomer = customerRepository.findByUserId(userId).orElseThrow(AccountNotFoundException::new);

        return signIn(userId, password, dbCustomer.getPassword(), dbCustomer.getRoles());
    }


    // 판매자, 구매자 구별 없는 회원가입 메서드
    private UserDetails saveUser(UserDto userDto) {
        if(userDto instanceof CustomerDto) {
            // 구매자
            Customer signUpCustomer = customerMapper.toEntity((CustomerDto) userDto);
            return customerRepository.save(signUpCustomer);
        }

        // 판매자
        Provider signUpProvider = providerMapper.toEntity((ProviderDto) userDto);
        return providerRepository.save(signUpProvider);
    }

    // 판매자, 구매자 구별 없는 로그인 메서드
    private SignInResultDto signIn(String userId, String password, String dbPassword, List<String> roles) {
        if(!PasswordEncryptor.isMatchBcrypt(password, dbPassword)) {
            LOGGER.info("패스워드 불일치");
            throw new PasswordNotMatchException("패스워드가 일치하지 않습니다.");
        }

        SignInResultDto signInResultDto = SignInResultDto.builder()
                .token(jwtTokenProvider.createToken(userId, roles))
                .build();

        setSuccessResult(signInResultDto);
        LOGGER.info("로그인 성공");
        return signInResultDto;
    }

    private void setSuccessResult(SignUpResultDto signUpResultDto) {
        signUpResultDto.setSuccess(true);
        signUpResultDto.setCode(CommonResponse.SUCCESS.getCode());
        signUpResultDto.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setFailResult(SignUpResultDto signUpResultDto) {
        signUpResultDto.setSuccess(false);
        signUpResultDto.setCode(CommonResponse.FAIL.getCode());
        signUpResultDto.setMsg(CommonResponse.FAIL.getMsg());
    }
}
