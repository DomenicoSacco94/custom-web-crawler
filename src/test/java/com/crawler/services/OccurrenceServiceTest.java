package com.crawler.services;

import com.crawler.domains.occurrences.OccurrenceRepository;
import com.crawler.domains.occurrences.OccurrenceService;
import com.crawler.domains.occurrences.mappers.OccurrenceMapper;
import com.crawler.domains.occurrences.models.Occurrence;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OccurrenceServiceTest {

    @Mock
    private OccurrenceRepository repository;

    @Mock
    private OccurrenceMapper occurrenceMapper;

    @Mock
    private KafkaTemplate<String, OccurrenceDTO> occurrenceKafkaTemplate;

    @InjectMocks
    private OccurrenceService occurrenceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveAll() {
        OccurrenceDTO occurrenceDTO = new OccurrenceDTO();
        Occurrence occurrence = new Occurrence();

        List<OccurrenceDTO> occurrenceDTOs = List.of(occurrenceDTO);
        List<Occurrence> occurrences = List.of(occurrence);

        when(occurrenceMapper.toEntity(occurrenceDTO)).thenReturn(occurrence);
        when(repository.saveAll(occurrences)).thenReturn(occurrences);

        List<Occurrence> result = occurrenceService.saveAll(occurrenceDTOs);

        assertEquals(occurrences, result);
        verify(occurrenceMapper, times(1)).toEntity(occurrenceDTO);
        verify(repository, times(1)).saveAll(occurrences);
    }

    @Test
    void testOnOccurrence() {
        OccurrenceDTO occurrenceDTO = new OccurrenceDTO();

        occurrenceService.onOccurrence(occurrenceDTO);

        verify(occurrenceKafkaTemplate, times(1)).send(OccurrenceService.OCCURRENCE_FACT_TOPIC, occurrenceDTO);
    }
}