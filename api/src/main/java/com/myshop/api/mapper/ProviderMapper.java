package com.myshop.api.mapper;

import com.myshop.api.domain.Provider;
import com.myshop.api.dto.provider.ProviderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProviderMapper extends GenericMapper<ProviderDto, Provider> {

    ProviderMapper INSTANCE = Mappers.getMapper(ProviderMapper.class);

}
