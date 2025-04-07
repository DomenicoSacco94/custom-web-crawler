package com.crawler.domains.facts;

import com.crawler.domains.facts.models.Fact;
import com.crawler.domains.facts.models.FactDTO;
import com.crawler.domains.occurrences.OccurrenceRepository;
import com.crawler.domains.occurrences.models.Occurrence;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ai.ollama.OllamaChatModel;

import static com.crawler.domains.scanner.processors.DocumentPatternProcessor.CHAR_WINDOW_LENGTH;

@Service
@AllArgsConstructor
public class FactService {

    private final FactRepository factRepository;
    private final OccurrenceRepository occurrenceRepository;
    private final OllamaChatModel ollamaChatModel;

    public final static int SYNTHESIS_FIRST_FACTOR = 2;

    public void saveFact(FactDTO factDTO) {
        Occurrence occurrence = occurrenceRepository.findById(factDTO.getOccurrenceId())
                .orElseThrow(() -> new IllegalArgumentException("Occurrence not found"));

        Fact fact = new Fact();
        fact.setOccurrence(occurrence);
        fact.setInferredText(factDTO.getInferredText());

        factRepository.save(fact);
    }

    public void extractFact(OccurrenceDTO occurrenceDTO) {
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
            Do not repeat yourself saying it matches the pattern, that is already intended.
            I would like to understand more WHY it is mentioned there and WHAT IT TALKS ABOUT in that particular frame.
            Your answer should ONLY contain the nugget of information extracted from the test, like one or multiple facts.
            """.formatted(occurrenceDTO.getSurroundingText(), CHAR_WINDOW_LENGTH/SYNTHESIS_FIRST_FACTOR);

        String response = ollamaChatModel.call(prompt);

        FactDTO factDTO = new FactDTO();
        factDTO.setOccurrenceId(occurrenceDTO.getId());
        factDTO.setInferredText(response);

        saveFact(factDTO);

    }
}