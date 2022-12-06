package com.myshop.api.mapper;

import com.myshop.api.domain.Provider;
import com.myshop.api.dto.ProviderDto;
import com.myshop.api.mapper.common.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProviderMapper extends GenericMapper<ProviderDto, Provider> {

    ProviderMapper INSTANCE = Mappers.getMapper(ProviderMapper.class);

    @Override
    @Mapping(source = "password", target = "password", qualifiedByName = "encryptPassword")
    Provider toEntity(ProviderDto providerDto);

}
