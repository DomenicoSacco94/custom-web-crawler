package com.crawler.domains.regexp.models.mappers;

import com.crawler.domains.regexp.models.Regexp;
import com.crawler.domains.regexp.models.RegexpDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegexpMapper {
    Regexp toEntity(RegexpDTO regexpDTO);
    RegexpDTO toDto(Regexp regexp);
}