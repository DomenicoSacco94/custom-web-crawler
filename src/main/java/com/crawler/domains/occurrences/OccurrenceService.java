package com.crawler.domains.occurrences;

import com.crawler.domains.occurrences.mappers.OccurrenceMapper;
import com.crawler.domains.occurrences.models.Occurrence;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OccurrenceService {
    private final OccurrenceRepository repository;
    private final OccurrenceMapper occurrenceMapper = OccurrenceMapper.INSTANCE;
    private final KafkaTemplate<String, OccurrenceDTO> occurrenceKafkaTemplate;
    public static final String OCCURRENCE_INFERENCE_TOPIC = "occurrence-inference-topic";

    public List<Occurrence> findAll() {
        return repository.findAll();
    }

    public void save(OccurrenceDTO occurrenceDTO) {
        Occurrence occurrence = occurrenceMapper.toEntity(occurrenceDTO);
        repository.save(occurrence);
    }

    public void onOccurrence(OccurrenceDTO occurrenceDTO) {
        log.info("sending {}", occurrenceDTO);
        occurrenceKafkaTemplate.send(OCCURRENCE_INFERENCE_TOPIC, occurrenceDTO);
    }
}