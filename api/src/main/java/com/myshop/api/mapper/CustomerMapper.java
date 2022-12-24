package com.myshop.api.mapper;

import com.myshop.api.domain.Customer;
import com.myshop.api.dto.customer.CustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper extends GenericMapper<CustomerDto, Customer> {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

}
