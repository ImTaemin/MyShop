package com.myshop.api.mapper;

import com.myshop.api.domain.Customer;
import com.myshop.api.dto.CustomerDto;
import com.myshop.api.mapper.common.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper extends GenericMapper<CustomerDto, Customer> {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Override
    @Mapping(source = "password", target = "password", qualifiedByName = "encryptPassword")
    Customer toEntity(CustomerDto customerDto);

}
