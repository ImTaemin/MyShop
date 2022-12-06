package com.myshop.api.mapper;

import com.myshop.api.domain.Cart;
import com.myshop.api.dto.CartDto;
import com.myshop.api.mapper.common.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper extends GenericMapper<CartDto, Cart> {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Override
    CartDto toDto(Cart cart);

    @Override
    @Mapping(target = "id", ignore = true)
    Cart toEntity(CartDto cartDto);

}