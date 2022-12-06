package com.myshop.api.mapper;

import com.myshop.api.domain.Customer;
import com.myshop.api.dto.CustomerDto;
import com.myshop.api.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerMapperTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    @Transactional
    public void dtoToEntity(){
        CustomerDto dto = CustomerDto.builder()
                .id("id")
                .name("name")
                .password("1234")
                .phone("010-1234-5678")
                .build();

        Customer entity = CustomerMapper.INSTANCE.toEntity(dto);

        customerRepository.save(entity);

        CustomerDto customer = CustomerMapper.INSTANCE.toDto(customerRepository.findById("userid").orElse(null));
        System.out.println(customer.toString());

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());

    }

    @Test
    public void entityToDto(){
        Customer entity = Customer.builder()
                .id("userid")
                .name("user1")
                .password("1234")
                .phone("010-1234-5678")
                .build();

        CustomerDto dto = CartMapper.INSTANCE.toDto(entity);

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }
}