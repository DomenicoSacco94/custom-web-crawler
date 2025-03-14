package com.crawler.domains.inferences.mappers;

import com.crawler.domains.inferences.models.Inference;
import com.crawler.domains.inferences.models.InferenceDTO;
import com.crawler.domains.occurrences.mappers.OccurrenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = OccurrenceMapper.class)
public interface InferenceMapper {
    InferenceMapper INSTANCE = Mappers.getMapper(InferenceMapper.class);

    @Mapping(source = "occurrence.id", target = "occurrenceId")
    InferenceDTO toDto(Inference inference);

    @Mapping(source = "occurrenceId", target = "occurrence.id")
    Inference toEntity(InferenceDTO inferenceDTO);
}