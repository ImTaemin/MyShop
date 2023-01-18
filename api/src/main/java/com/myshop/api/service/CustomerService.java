package com.myshop.api.service;

import com.myshop.api.domain.dto.account.CustomerAccount;
import com.myshop.api.domain.dto.request.UserUpdateRequest;
import com.myshop.api.domain.dto.response.data.CustomerData;
import com.myshop.api.domain.entity.Customer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomerService {

    UserDetails getCustomerByUserId(String userId) throws UsernameNotFoundException;
    CustomerData.Customer getInfo(Customer customer);
    Boolean checkUserId(String userId);
    Boolean modify(UserUpdateRequest updateParam);
    Boolean withdrawal(String userId, String password);

}
