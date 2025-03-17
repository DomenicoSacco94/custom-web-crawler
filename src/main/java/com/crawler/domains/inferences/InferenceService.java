package com.crawler.domains.inferences;

import com.crawler.domains.inferences.models.Inference;
import com.crawler.domains.inferences.models.InferenceDTO;
import com.crawler.domains.occurrences.OccurrenceRepository;
import com.crawler.domains.occurrences.models.Occurrence;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ai.ollama.OllamaChatModel;

import static com.crawler.domains.scanner.processors.DocumentPatternProcessor.CHAR_WINDOW_LENGTH;

@Service
@AllArgsConstructor
public class InferenceService {

    private final InferenceRepository inferenceRepository;
    private final OccurrenceRepository occurrenceRepository;
    private final OllamaChatModel ollamaChatModel;

    public final static int SYNTHESIS_FIRST_FACTOR = 2;

    public void saveInference(InferenceDTO inferenceDTO) {
        Occurrence occurrence = occurrenceRepository.findById(inferenceDTO.getOccurrenceId())
                .orElseThrow(() -> new IllegalArgumentException("Occurrence not found"));

        Inference inference = new Inference();
        inference.setOccurrence(occurrence);
        inference.setInferredText(inferenceDTO.getInferredText());

        inferenceRepository.save(inference);
    }

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

        String response = ollamaChatModel.call(prompt);

        InferenceDTO inferenceDTO = new InferenceDTO();
        inferenceDTO.setOccurrenceId(occurrenceDTO.getId());
        inferenceDTO.setInferredText(response);

        saveInference(inferenceDTO);

    }
}