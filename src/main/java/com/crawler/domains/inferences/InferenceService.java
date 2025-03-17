package com.crawler.domains.inferences;

import com.crawler.domains.inferences.models.Inference;
import com.crawler.domains.inferences.models.InferenceDTO;
import com.crawler.domains.occurrences.OccurrenceRepository;
import com.crawler.domains.occurrences.models.Occurrence;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static com.crawler.domains.scanner.processors.DocumentPatternProcessor.CHAR_WINDOW_LENGTH;

@Service
@RequiredArgsConstructor
public class InferenceService {

    private final InferenceRepository inferenceRepository;
    private final OccurrenceRepository occurrenceRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    public final static int SYNTHESIS_FIRST_FACTOR = 2;

    private String ollamaApiUrl = "http://ollama:8086";

    @Transactional
    public void saveInference(InferenceDTO inferenceDTO) {
        Occurrence occurrence = occurrenceRepository.findById(inferenceDTO.getOccurrenceId())
                .orElseThrow(() -> new IllegalArgumentException("Occurrence not found"));

        Inference inference = new Inference();
        inference.setOccurrence(occurrence);
        inference.setInferredText(inferenceDTO.getInferredText());

        inferenceRepository.save(inference);
    }

    @Transactional
    public void extractInference(OccurrenceDTO occurrenceDTO) {
        String prompt = """
            Given the following text, make more sense of it, knowing that it is about finding the recurrence of this regexp %s.
            """.formatted(occurrenceDTO.getPattern());

        if (occurrenceDTO.getDescription() != null && !occurrenceDTO.getDescription().isEmpty()) {
            prompt += """
                Description: %s
                """.formatted(occurrenceDTO.getDescription());
        }

        prompt += """
            Here is the text: %s
            Please make the answer no longer than %s characters.
            """.formatted(occurrenceDTO.getSurroundingText(), CHAR_WINDOW_LENGTH/SYNTHESIS_FIRST_FACTOR);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(prompt, headers);

        ResponseEntity<String> response = restTemplate.exchange(ollamaApiUrl, HttpMethod.POST, entity, String.class);

        String inferredText = response.getBody();

        InferenceDTO inferenceDTO = new InferenceDTO();
        inferenceDTO.setOccurrenceId(occurrenceDTO.getId());
        inferenceDTO.setInferredText(inferredText);

        saveInference(inferenceDTO);

    }
}