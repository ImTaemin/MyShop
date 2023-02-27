package com.myshop.api.service;

import com.myshop.api.config.security.JwtTokenProvider;
import com.myshop.api.domain.dto.request.CustomerRequest;
import com.myshop.api.domain.dto.request.ProviderRequest;
import com.myshop.api.domain.dto.request.UserDto;
import com.myshop.api.domain.dto.response.data.SignData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.enumeration.CommonResponse;
import com.myshop.api.exception.InvalidTokenException;
import com.myshop.api.exception.UserNotFoundException;
import com.myshop.api.exception.PasswordNotMatchException;
import com.myshop.api.repository.CustomerRepository;
import com.myshop.api.repository.ProviderRepository;
import com.myshop.api.util.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public SignData.SignInResponse signInProvider(String userId, String password) {
        Provider dbProvider = providerRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);

        return signIn(password, dbProvider);
    }

    @Override
    public SignData.SignInResponse signInCustomer(String userId, String password) {
        Customer dbCustomer = customerRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);

        return signIn(password, dbCustomer);
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
                    .roles(List.of("CUSTOMER"))
                    .build();

            return customerRepository.save(signUpCustomer);
        }

        // 판매자
        ProviderRequest providerRequest = (ProviderRequest) userDto;
        Provider signUpProvider = Provider.builder()
//                .cid(this.getCid())
                .cid("TC0ONETIME")
                .userId(providerRequest.getUserId())
                .password(providerRequest.getPassword())
                .phone(providerRequest.getPhone())
                .brandName(providerRequest.getBrandName())
                .roles(List.of("PROVIDER"))
                .build();

        return providerRepository.save(signUpProvider);
    }

    // 판매자, 구매자 구별 없는 로그인 메서드
    private SignData.SignInResponse signIn(String password, UserDetails userDetails) {
        if(!PasswordEncryptor.isMatchBcrypt(password, userDetails.getPassword())) {
            LOGGER.info("패스워드 불일치");
            throw new PasswordNotMatchException();
        }

        // === DB에 사용자가 있고 패스워드도 일치한 상태 ===

        String accessToken = jwtTokenProvider.createAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.createRefreshToken(userDetails);

        SignData.SignInResponse signInResponse = new SignData.SignInResponse();
        setSuccessResult(signInResponse);
        signInResponse.setAccessToken(accessToken);
        signInResponse.setRefreshToken(refreshToken);

        changeRefreshToken(refreshToken, userDetails);

        LOGGER.info("로그인 성공");
        return signInResponse;
    }

    private void changeRefreshToken(String refreshToken, UserDetails userDetails) {
        if(userDetails instanceof Provider) {
            // 판매자
            Provider provider = (Provider) userDetails;
            provider.setRefreshToken(refreshToken);

            providerRepository.save(provider);

        } else if(userDetails instanceof Customer) {
            // 구매자
            Customer customer = (Customer) userDetails;
            customer.setRefreshToken(refreshToken);

            customerRepository.save(customer);
        }
    }

    @Transactional
    @Override
    public SignData.SignInResponse reissueAccessToken(String refreshToken) {
        refreshToken = resolveToken(refreshToken);
        jwtTokenProvider.validateRefreshToken(refreshToken);

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // DB(refresh token)와 요청(refresh token)이 같은지 비교
        if(userDetails instanceof Provider) {
            if(!((Provider) userDetails).getRefreshToken().equals(refreshToken)) {
                throw new InvalidTokenException();
            }
        } else if (userDetails instanceof Customer) {
            if(!((Customer) userDetails).getRefreshToken().equals(refreshToken)) {
                throw new InvalidTokenException();
            }
        }

        // 정상 상태 (전체 재발행)
        String newAccessToken = jwtTokenProvider.createAccessToken(userDetails);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userDetails);

        // 결과
        SignData.SignInResponse signInResponse = new SignData.SignInResponse();
        setSuccessResult(signInResponse);
        signInResponse.setAccessToken(newAccessToken);
        signInResponse.setRefreshToken(newRefreshToken);

        // DB refresh token 재설정
        changeRefreshToken(newRefreshToken, userDetails);

        return signInResponse;
    }

    private String resolveToken(String token){
        if(token.startsWith("Bearer "))
            return token.substring(7);
        throw new InvalidTokenException();
    }

    private void setSuccessResult(SignData.SignUpResponse signUpResponse) {
        signUpResponse.setStatus(CommonResponse.SUCCESS.getStatus());
        signUpResponse.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setFailResult(SignData.SignUpResponse signUpResponse) {
        signUpResponse.setStatus(CommonResponse.FAIL.getStatus());
        signUpResponse.setMsg(CommonResponse.FAIL.getMsg());
    }
}
