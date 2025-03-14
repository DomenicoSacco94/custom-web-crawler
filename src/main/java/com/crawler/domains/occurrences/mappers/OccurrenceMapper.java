package com.crawler.domains.occurrences.mappers;

import com.crawler.domains.regexp.models.mappers.RegexpMapper;
import com.crawler.domains.occurrences.models.Occurrence;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {RegexpMapper.class})
public interface OccurrenceMapper {
    OccurrenceMapper INSTANCE = Mappers.getMapper(OccurrenceMapper.class);

    @Mapping(source = "regexp.pattern", target = "pattern")
    @Mapping(source = "regexp.description", target = "description")
    Occurrence toEntity(OccurrenceDTO occurrenceDTO);

    @Mapping(source = "pattern", target = "regexp.pattern")
    @Mapping(source = "description", target = "regexp.description")
    OccurrenceDTO toDto(Occurrence occurrence);
}