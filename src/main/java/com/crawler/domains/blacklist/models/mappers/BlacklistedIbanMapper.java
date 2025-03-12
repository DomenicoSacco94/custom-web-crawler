package com.crawler.domains.blacklist.models.mappers;

import com.crawler.domains.blacklist.models.BlacklistedIban;
import com.crawler.domains.blacklist.models.BlacklistedIbanDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BlacklistedIbanMapper {
    BlacklistedIbanMapper INSTANCE = Mappers.getMapper(BlacklistedIbanMapper.class);

    BlacklistedIbanDTO toDto(BlacklistedIban blacklistedIban);

    BlacklistedIban toEntity(BlacklistedIbanDTO blacklistedIbanDTO);
}