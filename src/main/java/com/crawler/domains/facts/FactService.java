package com.crawler.domains.facts;

import com.crawler.domains.facts.mappers.FactMapper;
import com.crawler.domains.facts.models.Fact;
import com.crawler.domains.facts.models.FactDTO;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ai.ollama.OllamaChatModel;

import java.util.List;
import java.util.stream.Collectors;

import static com.crawler.domains.scanner.processors.DocumentPatternProcessor.CHAR_WINDOW_LENGTH;

@Service
@AllArgsConstructor
public class FactService {

    private final FactRepository factRepository;
    private final OllamaChatModel ollamaChatModel;
    private final FactMapper factMapper;

    public final static int SYNTHESIS_FIRST_FACTOR = 5;

    public void extractFact(OccurrenceDTO occurrenceDTO) {
        String prompt = """
            Given the following text, make more sense of it, knowing that it is about finding the recurrence of this regexp %s.
            """.formatted(occurrenceDTO.getRegexpDTO().getPattern());

        String description = occurrenceDTO.getRegexpDTO().getDescription();
        String surroundingText = occurrenceDTO.getSurroundingText();

        if (description != null && !description.isEmpty()) {
            prompt += """
                Description: %s
                """.formatted(description);
        }

        prompt += """
            Here is the text: %s
            Please make the answer no longer than %s characters.
            Do not repeat yourself saying it matches the pattern, that is already intended.
            I would like to understand more WHY it is mentioned there and WHAT IT TALKS ABOUT in that particular frame.
            Your answer should ONLY contain the nugget of information extracted from the test, like one or multiple facts.
            """.formatted(surroundingText, CHAR_WINDOW_LENGTH/SYNTHESIS_FIRST_FACTOR);

        String response = ollamaChatModel.call(prompt);

        FactDTO factDTO = new FactDTO();
        factDTO.setOccurrenceDTO(occurrenceDTO);
        factDTO.setInferredText(response);

        factRepository.save(factMapper.toEntity(factDTO));

    }

    public List<FactDTO> getAllFacts() {
        List<Fact> facts = factRepository.findAll();
        return facts.stream()
                .map(factMapper::toDto)
                .collect(Collectors.toList());
    }
}