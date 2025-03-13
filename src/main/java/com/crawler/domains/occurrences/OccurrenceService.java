package com.crawler.domains.occurrences;

import com.crawler.domains.occurrences.mappers.OccurrenceMapper;
import com.crawler.domains.occurrences.models.Occurrence;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OccurrenceService {
    private final OccurrenceRepository repository;
    private final OccurrenceMapper occurrenceMapper = OccurrenceMapper.INSTANCE;

    public List<Occurrence> findAll() {
        return repository.findAll();
    }

    public void save(OccurrenceDTO occurrenceDTO) {
        Occurrence occurrence = occurrenceMapper.toEntity(occurrenceDTO);
        repository.save(occurrence);
    }
}