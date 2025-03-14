package com.crawler.domains.inferences.listeners;

import com.crawler.domains.inferences.InferenceService;
import com.crawler.domains.inferences.models.InferenceDTO;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class InferenceListener {

    private final InferenceService inferenceService;

    @KafkaListener(topics = "occurrence-inference-topic", groupId = "inference-group")
    public void listen(OccurrenceDTO occurrenceDTO) {
        log.info("Received inference message: {}", occurrenceDTO);

        InferenceDTO inferenceDTO = new InferenceDTO();
        inferenceDTO.setOccurrenceId(occurrenceDTO.getId());
        inferenceDTO.setInferredText("some ai jibber-jabber");

        inferenceService.saveInference(inferenceDTO);
    }
}