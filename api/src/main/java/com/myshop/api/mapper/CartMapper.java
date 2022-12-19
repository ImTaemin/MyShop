package com.myshop.api.mapper;

import com.myshop.api.domain.Cart;
import com.myshop.api.dto.CartDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CartMapper extends GenericMapper<CartDto, Cart> {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

}