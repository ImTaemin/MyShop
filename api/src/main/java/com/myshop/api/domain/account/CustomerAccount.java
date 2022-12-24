package com.myshop.api.domain.account;

import com.myshop.api.domain.Customer;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class CustomerAccount extends User {

    Customer customer;

    public CustomerAccount(Customer customer) {
        super(customer.getUserId(), customer.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
        this.customer = customer;
    }
}
