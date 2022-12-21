package com.myshop.api.service;

import com.myshop.api.config.security.JwtTokenProvider;
import com.myshop.api.domain.Provider;
import com.myshop.api.dto.SignInResultDto;
import com.myshop.api.dto.SignUpResultDto;
import com.myshop.api.dto.provider.ProviderDto;
import com.myshop.api.enumeration.CommonResponse;
import com.myshop.api.exception.PasswordNotMatchException;
import com.myshop.api.mapper.ProviderMapper;
import com.myshop.api.repository.ProviderRepository;
import com.myshop.api.util.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;

@Service
@RequiredArgsConstructor
public class SignServiceImpl implements SignService {

    private final Logger LOGGER = LoggerFactory.getLogger(SignServiceImpl.class);

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public SignUpResultDto signUp(ProviderDto providerParam) {
        PasswordEncryptor.bcrypt(providerParam);

        Provider signUpProvider = providerMapper.toEntity(providerParam);
        Provider savedUser = providerRepository.save(signUpProvider);

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
    public SignInResultDto signIn(String loginId, String password) throws PasswordNotMatchException, AccountNotFoundException {
        Provider dbProvider = providerRepository.findByLoginId(loginId).orElseThrow(AccountNotFoundException::new);

        if(!PasswordEncryptor.isMatchBcrypt(password, dbProvider.getPassword())) {
            LOGGER.info("패스워드 불일치");
            throw new PasswordNotMatchException("패스워드가 일치하지 않습니다.");
        }

        LOGGER.info("로그인 성공");
        
        SignInResultDto signInResultDto = SignInResultDto.builder()
                .token(jwtTokenProvider.createToken(loginId))
                .build();

        setSuccessResult(signInResultDto);

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
