package com.myshop.api.annotation.mock;

import com.myshop.api.domain.dto.account.CustomerAccount;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.enumeration.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockCustomerSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomer> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomer annotation) {
        SecurityContext secContext = SecurityContextHolder.createEmptyContext();

        Customer customer = Customer.builder()
                .userId("taemin")
                .password("1234")
                .phone("010-1234-5678")
                .name("김태민")
                .roles(List.of(UserRole.CUSTOMER.toString()))
                .build();
        CustomerAccount customerAccount = new CustomerAccount(customer);

        Authentication auth = new UsernamePasswordAuthenticationToken(customerAccount, "1234", customerAccount.getCustomer().getAuthorities());
        secContext.setAuthentication(auth);

        return secContext;
    }
}
