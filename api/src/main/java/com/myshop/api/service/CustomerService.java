package com.myshop.api.service;

import com.myshop.api.dto.customer.CustomerUpdateParam;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomerService {

    UserDetails getCustomerByUserId(String userId) throws UsernameNotFoundException;
    Boolean checkUserId(String userId);
    Boolean modify(CustomerUpdateParam updateParam);
    Boolean withdrawal(String userId, String password);

}
