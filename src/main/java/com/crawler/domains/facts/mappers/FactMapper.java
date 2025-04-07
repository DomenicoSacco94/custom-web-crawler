package com.crawler.domains.facts.mappers;

import com.crawler.domains.facts.models.Fact;
import com.crawler.domains.facts.models.FactDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FactMapper {
    FactDTO toDto(Fact fact);
    Fact toEntity(FactDTO factDTO);
}