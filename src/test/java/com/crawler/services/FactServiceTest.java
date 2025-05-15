package com.crawler.services;

import com.crawler.domains.facts.FactRepository;
import com.crawler.domains.facts.FactServiceImpl;
import com.crawler.domains.facts.mappers.FactMapper;
import com.crawler.domains.facts.models.Fact;
import com.crawler.domains.facts.models.FactDTO;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import com.crawler.domains.regexp.models.RegexpDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ai.ollama.OllamaChatModel;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FactServiceTest {

    @Mock
    private FactRepository factRepository;

    @Mock
    private FactMapper factMapper;

    @Mock
    private OllamaChatModel ollamaChatModel;

    @InjectMocks
    private FactServiceImpl factService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllFacts() {
        Fact fact = new Fact();
        FactDTO factDTO = new FactDTO();

        List<Fact> facts = List.of(fact);
        List<FactDTO> factDTOs = List.of(factDTO);

        when(factRepository.findAll()).thenReturn(facts);
        when(factMapper.toDto(fact)).thenReturn(factDTO);

        List<FactDTO> result = factService.getAllFacts();

        assertEquals(factDTOs, result);
        verify(factRepository, times(1)).findAll();
        verify(factMapper, times(1)).toDto(fact);
    }

    @Test
    void testExtractFact() throws IOException {

        RegexpDTO regexpDTO = new RegexpDTO();
        regexpDTO.setDescription("Mocked description");

        OccurrenceDTO occurrenceDTO = new OccurrenceDTO();
        occurrenceDTO.setRegexpDTO(regexpDTO);
        occurrenceDTO.setSurroundingText("Mocked surrounding text");

        FactDTO factDTO = new FactDTO();
        factDTO.setOccurrenceDTO(occurrenceDTO);
        factDTO.setInferredText("Inferred text");
        factDTO.setConsequences("Consequences");

        String factPromptTemplate = "Fact prompt template";
        String consequencesPromptTemplate = "Consequences prompt template";
        String hydratedFactPrompt = "Hydrated fact prompt";
        String hydratedConsequencesPrompt = "Hydrated consequences prompt";
        String inferredTextResponse = "Inferred text";
        String consequencesResponse = "Consequences";

        FactServiceImpl spyFactService = spy(factService);

        doReturn(factPromptTemplate).when(spyFactService).loadFactPromptTemplate("prompts/prompt_extract_fact.txt");
        doReturn(consequencesPromptTemplate).when(spyFactService).loadFactPromptTemplate("prompts/prompt_extract_consequences.txt");
        doReturn(hydratedFactPrompt).when(spyFactService).hydrateFactPrompt(eq(factPromptTemplate), anyString(), anyString(), any());
        doReturn(hydratedConsequencesPrompt).when(spyFactService).hydrateFactPrompt(eq(consequencesPromptTemplate), anyString(), anyString(), any());

        when(ollamaChatModel.call(hydratedFactPrompt)).thenReturn(inferredTextResponse);
        when(ollamaChatModel.call(hydratedConsequencesPrompt)).thenReturn(consequencesResponse);

        spyFactService.extractFact(occurrenceDTO);

        verify(ollamaChatModel, times(1)).call(hydratedFactPrompt);
        verify(ollamaChatModel, times(1)).call(hydratedConsequencesPrompt);
    }

    @Test
    void testHydrateFactPrompt() {
        String template = "Given the following text about {description}: {text}, limit to {char_limit} characters.";
        String description = "economy";
        String text = "This is a sample text.";
        String charLimit = "100";

        String expected = "Given the following text about economy: This is a sample text., limit to 100 characters.";
        String result = factService.hydrateFactPrompt(template, description, text, charLimit);

        assertEquals(expected, result);
    }

    @Test
    void testLoadFactPromptTemplate() throws IOException {
        String expectedContent = "Mocked file content";
        String filePath = "prompts/prompt_extract_fact.txt";

        FactServiceImpl spyFactService = spy(factService);
        doReturn(expectedContent).when(spyFactService).loadFactPromptTemplate(filePath);

        String result = spyFactService.loadFactPromptTemplate(filePath);

        assertEquals(expectedContent, result);
        verify(spyFactService, times(1)).loadFactPromptTemplate(filePath);
    }
}