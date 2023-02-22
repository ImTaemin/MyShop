package com.myshop.api.service;

import com.myshop.api.domain.dto.request.UserUpdateRequest;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.enumeration.UserRole;
import com.myshop.api.repository.ProviderRepository;
import com.myshop.api.util.PasswordEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProviderServiceTest {

    @Mock
    ProviderRepository providerRepository;

    ProviderService providerService;

    Provider provider;

    @BeforeEach
    public void init() {
        providerService = new ProviderServiceImpl(providerRepository);

        provider = Provider.builder()
                .userId("taemin")
                .password("$2a$12$Liq1iPQn58mqSt8Efe.mn.bQt7W4uuVNypg8N2IAHG.cEPqLqyMZ6")
                .phone("010-1234-5678")
                .brandName("브랜드명")
                .roles(List.of(UserRole.PROVIDER.toString()))
                .build();
    }

    @Test
    @DisplayName("로그인 정보 조회")
    void getProviderByUserId() throws Exception {
        //given
        given(providerRepository.findByUserId(anyString())).willReturn(Optional.of(provider));

        //when
        UserDetails user = providerService.getProviderByUserId("taemin");

        //then
        Assertions.assertNotNull(user);
        verify(providerRepository).findByUserId(anyString());
    }

    @Test
    @DisplayName("브랜드명 중복 확인")
    void checkBrandName() {
        //given
        given(providerRepository.existsByBrandName(anyString())).willReturn(true);

        //when
        boolean isAvailable = !providerService.checkBrandName("브랜드명");

        //then
        Assertions.assertTrue(isAvailable);
        verify(providerRepository).existsByBrandName(anyString());
    }

    @Test
    @DisplayName("판매자 정보 수정. db의 비밀번호와 파라미터 비밀번호가 같을 경우 수정")
    void modify() {
        //given
        given(providerRepository.findByUserId(anyString())).willReturn(Optional.of(provider));

        UserUpdateRequest updateParam = UserUpdateRequest.builder()
                .userId("taemin")
                .password("1234")
                .modifyPassword("5678")
                .phone("010-5678-1234")
                .build();

        //when
        boolean isPasswordMatch = PasswordEncryptor.isMatchBcrypt(updateParam.getPassword(), provider.getPassword());
        boolean isModifySuccess = providerService.modify(updateParam);

        //then
        Assertions.assertTrue(isPasswordMatch);
        Assertions.assertTrue(isModifySuccess);
        verify(providerRepository).findByUserId(anyString());
        verify(providerRepository).save(any(Provider.class));
    }

    @Test
    @DisplayName("판매자 정보 삭제. db의 비밀번호와 파라미터 비밀번호가 같을 경우 삭제")
    void withdrawal() {
        //given
        given(providerRepository.findByUserId(anyString())).willReturn(Optional.of(provider));

        String userId = "taemin";
        String password = "1234";

        //when
        boolean isPasswordMatch = PasswordEncryptor.isMatchBcrypt(password, provider.getPassword());
        boolean isDeleteSuccess = providerService.withdrawal(userId, password);

        //then
        Assertions.assertTrue(isPasswordMatch);
        Assertions.assertTrue(isDeleteSuccess);
        verify(providerRepository).findByUserId(anyString());
        verify(providerRepository).delete(any(Provider.class));
    }
}