package com.crawler.services;

import com.crawler.domains.occurrences.models.OccurrenceDTO;
import com.crawler.domains.regexp.RegexpRepository;
import com.crawler.domains.regexp.RegexpService;
import com.crawler.domains.regexp.models.Regexp;
import com.crawler.domains.regexp.models.RegexpDTO;
import com.crawler.domains.regexp.models.RegexpProjection;
import com.crawler.domains.regexp.models.mappers.RegexpMapper;
import com.crawler.domains.scanner.exceptions.TopicNotFoundException;
import com.crawler.domains.topics.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegexpServiceTest {

    @Mock
    private RegexpRepository regexpRepository;

    @Mock
    private RegexpMapper regexpMapper;

    @Mock
    private TopicService topicService;

    @InjectMocks
    private RegexpService regexpService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDetectPatterns() {
        String text = "Sample text with a pattern";
        Long topicId = 1L;
        String url = "http://example.com";

        RegexpProjection regexpProjection = mock(RegexpProjection.class);
        when(regexpProjection.getPattern()).thenReturn("pattern");

        Regexp regexp = new Regexp();
        RegexpDTO regexpDTO = new RegexpDTO();

        String surroundingText = "Sample text with a pattern";

        OccurrenceDTO occurrenceDTO = new OccurrenceDTO(null, regexpDTO, surroundingText, url);

        when(topicService.findTopicById(topicId)).thenReturn(null);
        when(regexpRepository.findAllByTopicId(topicId)).thenReturn(List.of(regexpProjection));
        when(regexpRepository.findByPattern("pattern")).thenReturn(List.of(regexp));
        when(regexpMapper.toDto(regexp)).thenReturn(regexpDTO);

        List<OccurrenceDTO> result = regexpService.detectPatterns(text, topicId, url);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(occurrenceDTO.getSurroundingText(), result.get(0).getSurroundingText());
        verify(topicService, times(1)).findTopicById(topicId);
        verify(regexpRepository, times(1)).findAllByTopicId(topicId);
        verify(regexpMapper, times(1)).toDto(regexp);
    }

    @Test
    void testDetectPatternsThrowsException() {
        Long topicId = 1L;

        when(topicService.findTopicById(topicId)).thenReturn(null);
        when(regexpRepository.findAllByTopicId(topicId)).thenReturn(List.of());

        assertThrows(TopicNotFoundException.class, () -> regexpService.detectPatterns("text", topicId, "http://example.com"));
        verify(topicService, times(1)).findTopicById(topicId);
        verify(regexpRepository, times(1)).findAllByTopicId(topicId);
    }
}