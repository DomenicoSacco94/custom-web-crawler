package com.crawler.domains.occurrences.mappers;

import com.crawler.domains.regexp.models.mappers.RegexpMapper;
import com.crawler.domains.occurrences.models.Occurrence;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RegexpMapper.class})
public interface OccurrenceMapper {
    Occurrence toEntity(OccurrenceDTO occurrenceDTO);
    OccurrenceDTO toDto(Occurrence occurrence);
}