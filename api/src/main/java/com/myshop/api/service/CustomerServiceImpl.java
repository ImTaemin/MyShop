package com.myshop.api.service;

import com.myshop.api.domain.dto.response.data.CustomerData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.dto.account.CustomerAccount;
import com.myshop.api.domain.dto.request.UserUpdateRequest;
import com.myshop.api.exception.UserNotFoundException;
import com.myshop.api.repository.CustomerRepository;
import com.myshop.api.util.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails getCustomerByUserId(String userId) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);

        return new CustomerAccount(customer);
    }

    @Override
    public CustomerData.Customer getInfo(Customer customer) {
        if(customer == null) throw new UserNotFoundException();

        return new CustomerData.Customer(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean checkUserId(String userId) {
        return !customerRepository.existsByUserId(userId);
    }

    @Transactional
    @Override
    public Boolean modify(UserUpdateRequest updateParam) {
        Customer dbCustomer = customerRepository.findByUserId(updateParam.getUserId()).orElseThrow(UserNotFoundException::new);

        if(PasswordEncryptor.isMatchBcrypt(updateParam.getPassword(), dbCustomer.getPassword())) {
            String encryptedPassword = PasswordEncryptor.bcrypt(updateParam.getModifyPassword());

            dbCustomer.setPassword(encryptedPassword);
            dbCustomer.setPhone(updateParam.getPhone());

            customerRepository.save(dbCustomer);
            return true;
        }

        return false;
    }

    @Transactional
    @Override
    public Boolean withdrawal(String userId, String password) {
        Customer dbCustomer = customerRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);

        if(PasswordEncryptor.isMatchBcrypt(password, dbCustomer.getPassword())) {
            customerRepository.delete(dbCustomer);

            return true;
        }

        return false;
    }

}
