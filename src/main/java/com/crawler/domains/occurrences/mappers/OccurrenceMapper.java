package com.crawler.domains.occurrences.mappers;

import com.crawler.domains.regexp.models.mappers.RegexpMapper;
import com.crawler.domains.occurrences.models.Occurrence;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RegexpMapper.class})
public interface OccurrenceMapper {
    @Mapping(source = "regexpDTO", target = "regexp")
    Occurrence toEntity(OccurrenceDTO occurrenceDTO);

    @Mapping(source = "regexp", target = "regexpDTO")
    OccurrenceDTO toDto(Occurrence occurrence);
}