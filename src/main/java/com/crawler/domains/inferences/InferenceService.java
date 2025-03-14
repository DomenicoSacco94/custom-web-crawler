package com.crawler.domains.inferences;

import com.crawler.domains.inferences.models.Inference;
import com.crawler.domains.inferences.models.InferenceDTO;

import com.crawler.domains.occurrences.OccurrenceRepository;
import com.crawler.domains.occurrences.models.Occurrence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InferenceService {

    private final InferenceRepository inferenceRepository;
    private final OccurrenceRepository occurrenceRepository;

    @Transactional
    public void saveInference(InferenceDTO inferenceDTO) {
        Occurrence occurrence = occurrenceRepository.findById(inferenceDTO.getOccurrenceId())
                .orElseThrow(() -> new IllegalArgumentException("Occurrence not found"));

        Inference inference = new Inference();
        inference.setOccurrence(occurrence);
        inference.setInferredText(inferenceDTO.getInferredText());

        inferenceRepository.save(inference);
    }
}