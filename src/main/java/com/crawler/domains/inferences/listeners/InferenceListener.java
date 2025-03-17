package com.crawler.domains.inferences.listeners;

import com.crawler.domains.inferences.InferenceService;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.crawler.domains.occurrences.OccurrenceService.OCCURRENCE_INFERENCE_TOPIC;

@Service
@AllArgsConstructor
public class InferenceListener {

    private final InferenceService inferenceService;

    @KafkaListener(topics = OCCURRENCE_INFERENCE_TOPIC, groupId = "inference-group", containerFactory = "jsonKafkaListenerContainerFactory")
    public void listen(OccurrenceDTO occurrenceDTO) {
        inferenceService.extractInference(occurrenceDTO);
    }
}