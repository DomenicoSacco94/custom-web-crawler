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

    Occurrence toEntity(OccurrenceDTO occurrenceDTO);

    OccurrenceDTO toDto(Occurrence occurrence);
}