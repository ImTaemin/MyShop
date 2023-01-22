package com.myshop.api.service;

import com.myshop.api.domain.dto.response.data.CustomerData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.dto.account.CustomerAccount;
import com.myshop.api.domain.dto.request.UserUpdateRequest;
import com.myshop.api.domain.entity.Item;
import com.myshop.api.exception.ItemNotFoundException;
import com.myshop.api.exception.NotExistUserException;
import com.myshop.api.repository.CustomerRepository;
import com.myshop.api.repository.ItemRepository;
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
    private final ItemRepository itemRepository;

    @Override
    public UserDetails getCustomerByUserId(String userId) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUserId(userId).orElseThrow(NotExistUserException::new);

        return new CustomerAccount(customer);
    }

    @Override
    public CustomerData.Customer getInfo(Customer customer) {
        if(customer == null) throw new NotExistUserException();

        return new CustomerData.Customer(customer);
    }

    @Transactional
    @Override
    public Boolean checkUserId(String userId) {
        return !customerRepository.existsByUserId(userId);
    }

    @Transactional
    @Override
    public Boolean modify(UserUpdateRequest updateParam) {
        Customer dbCustomer = customerRepository.findByUserId(updateParam.getUserId()).orElseThrow(NotExistUserException::new);

        if(PasswordEncryptor.isMatchBcrypt(updateParam.getPassword(), dbCustomer.getPassword())) {
            dbCustomer.setPassword(updateParam.getPassword());
            dbCustomer.setPhone(updateParam.getPhone());

            customerRepository.save(dbCustomer);
            return true;
        }

        return false;
    }

    @Transactional
    @Override
    public Boolean withdrawal(String userId, String password) {
        Customer dbCustomer = customerRepository.findByUserId(userId).orElseThrow(NotExistUserException::new);

        if(PasswordEncryptor.isMatchBcrypt(password, dbCustomer.getPassword())) {
            customerRepository.delete(dbCustomer);

            return true;
        }

        return false;
    }

}
