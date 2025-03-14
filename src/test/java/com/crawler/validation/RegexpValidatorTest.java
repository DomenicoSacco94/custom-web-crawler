package com.crawler.validation;

import com.crawler.domains.regexp.RegexpRepository;
import com.crawler.domains.regexp.models.Regexp;
import com.crawler.domains.scanner.processors.DocumentPatternProcessor;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RegexpValidatorTest {

    @Mock
    private RegexpRepository regexpRepository;

    private DocumentPatternProcessor processor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        processor = new DocumentPatternProcessor(regexpRepository);
        when(regexpRepository.findAllBy()).thenReturn(List.of(() -> "DE15\\s3006\\s0601\\s0505\\s7807\\s80"));
        Regexp mockRegexp = new Regexp();
        mockRegexp.setId(1L);
        mockRegexp.setPattern("DE15\\s3006\\s0601\\s0505\\s7807\\s80");
        when(regexpRepository.findByPattern(any())).thenReturn(List.of(mockRegexp));
    }

    @Test
    public void testDocWithoutBlacklist() {
        String docText = "some text extracted from the document DE89 3704 0044 0532 0130 01";

        List<OccurrenceDTO> occurrences = processor.detectPatterns(docText);

        assertEquals(0, occurrences.size());
    }

    @Test
    public void testDocWithBlacklistedRegexp() {
        String docText = "some text extracted from the document DE15 3006 0601 0505 7807 80";

        List<OccurrenceDTO> occurrences = processor.detectPatterns(docText);

        assertEquals(1, occurrences.size());
        assertEquals("DE15\\s3006\\s0601\\s0505\\s7807\\s80", occurrences.get(0).getRegexp().getPattern());
    }
}