package com.myshop.api.service;

import com.myshop.api.domain.dto.request.UserUpdateRequest;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.enumeration.UserRole;
import com.myshop.api.repository.CustomerRepository;
import com.myshop.api.repository.ItemRepository;
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
public class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    ItemRepository itemRepository;

    CustomerService customerService;

    Customer customer;

    @BeforeEach
    public void init() {
        customerService = new CustomerServiceImpl(customerRepository);

        customer = Customer.builder()
                .userId("taemin")
                .password("$2a$12$Liq1iPQn58mqSt8Efe.mn.bQt7W4uuVNypg8N2IAHG.cEPqLqyMZ6")
                .phone("010-1234-5678")
                .name("김태민")
                .roles(List.of(UserRole.CUSTOMER.toString()))
                .build();
    }

    @Test
    @DisplayName("로그인 정보 조회")
    void getCustomerByUserId() throws Exception {
        //given
        given(customerRepository.findByUserId(anyString())).willReturn(Optional.of(customer));

        //when
        UserDetails user = customerService.getCustomerByUserId("taemin");

        //then
        Assertions.assertNotNull(user);
        verify(customerRepository).findByUserId(anyString());
    }

    @Test
    @DisplayName("사용자 아이디 중복 확인")
    void checkUserId() {
        //given
        given(customerRepository.existsByUserId(anyString())).willReturn(true);

        //when
        boolean isAvailable = !customerService.checkUserId("taemin");

        //then
        Assertions.assertTrue(isAvailable);
        verify(customerRepository).existsByUserId(anyString());
    }

    @Test
    @DisplayName("구매자 정보 수정. db의 비밀번호와 파라미터 비밀번호가 같을 경우 수정")
    void modify() {
        //given
        given(customerRepository.findByUserId(anyString())).willReturn(Optional.of(customer));

        UserUpdateRequest updateParam = UserUpdateRequest.builder()
                .userId("taemin")
                .password("1234")
                .modifyPassword("5678")
                .phone("010-5678-1234")
                .build();

        //when
        boolean isPasswordMatch = PasswordEncryptor.isMatchBcrypt(updateParam.getPassword(), customer.getPassword());
        boolean isModifySuccess = customerService.modify(updateParam);

        //then
        Assertions.assertTrue(isPasswordMatch);
        Assertions.assertTrue(isModifySuccess);
        verify(customerRepository).findByUserId(anyString());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("구매자 정보 삭제. db의 비밀번호와 파라미터 비밀번호가 같을 경우 삭제")
    void withdrawal() {
        //given
        given(customerRepository.findByUserId(anyString())).willReturn(Optional.of(customer));

        String userId = "taemin";
        String password = "1234";

        //when
        boolean isPasswordMatch = PasswordEncryptor.isMatchBcrypt(password, customer.getPassword());
        boolean isDeleteSuccess = customerService.withdrawal(userId, password);

        //then
        Assertions.assertTrue(isPasswordMatch);
        Assertions.assertTrue(isDeleteSuccess);
        verify(customerRepository).findByUserId(anyString());
        verify(customerRepository).delete(any(Customer.class));
    }
}
