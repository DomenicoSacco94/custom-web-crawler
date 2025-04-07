package com.crawler.domains.facts.listeners;

import com.crawler.domains.facts.FactService;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.crawler.domains.occurrences.OccurrenceService.OCCURRENCE_FACT_TOPIC;

@Service
@AllArgsConstructor
public class OccurrencesListener {

    private final FactService factService;

    @KafkaListener(topics = OCCURRENCE_FACT_TOPIC, groupId = "facts-group", containerFactory = "jsonKafkaListenerContainerFactory")
    public void listen(OccurrenceDTO occurrenceDTO) {
        factService.extractFact(occurrenceDTO);
    }
}