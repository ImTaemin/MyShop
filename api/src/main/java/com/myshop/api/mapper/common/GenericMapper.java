package com.myshop.api.mapper.common;

import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public interface GenericMapper<D, E> {

    D toDto(E e);
    E toEntity(D d);

    List<D> toDtoList(List<E> entityList);
    List<E> toEntityList(List<D> dtoList);

    @Named("encryptPassword")
    default String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}