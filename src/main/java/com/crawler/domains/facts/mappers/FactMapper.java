package com.crawler.domains.facts.mappers;

import com.crawler.domains.facts.models.Fact;
import com.crawler.domains.facts.models.FactDTO;
import com.crawler.domains.occurrences.mappers.OccurrenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OccurrenceMapper.class})
public interface FactMapper {
    @Mapping(source = "occurrence", target = "occurrenceDTO")
    FactDTO toDto(Fact fact);

    @Mapping(source = "occurrenceDTO", target = "occurrence")
    Fact toEntity(FactDTO factDTO);
}