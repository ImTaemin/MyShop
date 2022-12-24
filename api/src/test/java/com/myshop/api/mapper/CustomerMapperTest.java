package com.myshop.api.mapper;

import com.myshop.api.domain.Customer;
import com.myshop.api.dto.customer.CustomerDto;
import com.myshop.api.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebAppConfiguration
@SpringBootTest
public class CustomerMapperTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    @Transactional
    public void dtoToEntity(){
        CustomerDto dto = CustomerDto.builder()
                .userId("id")
                .name("name")
                .password("1234")
                .phone("010-1234-5678")
                .build();

        Customer entity = CustomerMapper.INSTANCE.toEntity(dto);

        customerRepository.save(entity);

        CustomerDto customer = CustomerMapper.INSTANCE.toDto(customerRepository.findById("userid").orElse(null));
        System.out.println(customer.toString());

        assertEquals(dto.getUserId(), entity.getUserId());
        assertEquals(dto.getName(), entity.getName());

    }

    @Test
    public void entityToDto(){
        Customer entity = Customer.builder()
                .userId("userid")
                .name("user1")
                .password("1234")
                .phone("010-1234-5678")
                .build();

        CustomerDto dto = CustomerMapper.INSTANCE.toDto(entity);

        assertEquals(dto.getUserId(), entity.getUserId());
        assertEquals(dto.getName(), entity.getName());
    }
}