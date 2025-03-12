package com.crawler.domains.regexps.models.mappers;

import com.crawler.domains.regexps.models.Regexp;
import com.crawler.domains.regexps.models.RegexpDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RegexpMapper {
    RegexpMapper INSTANCE = Mappers.getMapper(RegexpMapper.class);

    RegexpDTO toDto(Regexp regexp);

    Regexp toEntity(RegexpDTO regexpDTO);
}