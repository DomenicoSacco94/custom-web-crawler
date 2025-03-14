package com.crawler.domains.regexp.models.mappers;

import com.crawler.domains.regexp.models.Regexp;
import com.crawler.domains.regexp.models.RegexpDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RegexpMapper {
    RegexpMapper INSTANCE = Mappers.getMapper(RegexpMapper.class);

    RegexpDTO toDto(Regexp regexp);

    Regexp toEntity(RegexpDTO regexpDTO);
}