package com.crawler.domains.facts;

import com.crawler.domains.facts.mappers.FactMapper;
import com.crawler.domains.facts.models.Fact;
import com.crawler.domains.facts.models.FactDTO;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static com.crawler.domains.regexp.RegexpService.CHAR_WINDOW_LENGTH;

@Service
@AllArgsConstructor
public class FactServiceImpl implements FactService {

    private final FactRepository factRepository;
    private final OllamaChatModel ollamaChatModel;
    private final FactMapper factMapper;

    public static final int SYNTHESIS_FIRST_FACTOR = 5;

    public String hydrateFactPrompt(String template, String description, String text, String charLimit) {
        return template
                .replace("{description}", description)
                .replace("{text}", text)
                .replace("{char_limit}", charLimit);
    }

    public String loadFactPromptTemplate(String factPromptFilePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(factPromptFilePath);
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    public void extractFact(OccurrenceDTO occurrenceDTO) {
        String factPromptTemplate;
        String consequencesPromptTemplate;
        try {
            factPromptTemplate = loadFactPromptTemplate("prompts/prompt_extract_fact.txt");
            consequencesPromptTemplate = loadFactPromptTemplate("prompts/prompt_extract_consequences.txt");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load prompt template", e);
        }

        String description = occurrenceDTO.getRegexpDTO().getDescription();
        String surroundingText = occurrenceDTO.getSurroundingText();
        String charLimit = String.valueOf(CHAR_WINDOW_LENGTH / SYNTHESIS_FIRST_FACTOR);

        String factPrompt = hydrateFactPrompt(factPromptTemplate, description, surroundingText, charLimit);
        String inferredTextResponse = ollamaChatModel.call(factPrompt);

        String consequencesPrompt = hydrateFactPrompt(consequencesPromptTemplate, description, surroundingText, charLimit);
        String consequencesResponse = ollamaChatModel.call(consequencesPrompt);

        FactDTO factDTO = new FactDTO();
        factDTO.setOccurrenceDTO(occurrenceDTO);
        factDTO.setInferredText(inferredTextResponse);
        factDTO.setConsequences(consequencesResponse);

        factRepository.save(factMapper.toEntity(factDTO));
    }

    public List<FactDTO> getAllFacts() {
        List<Fact> facts = factRepository.findAll();
        return facts.stream()
                .map(factMapper::toDto)
                .collect(Collectors.toList());
    }
}