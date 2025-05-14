package com.crawler.controllers;

import com.crawler.domains.facts.FactController;
import com.crawler.domains.facts.FactService;
import com.crawler.domains.facts.models.FactDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FactControllerTest {

    @Mock
    private FactService factService;

    @InjectMocks
    private FactController factController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFacts() {
        FactDTO fact1 = new FactDTO();
        fact1.setId(1L);
        fact1.setInferredText("Fact 1");

        FactDTO fact2 = new FactDTO();
        fact2.setId(2L);
        fact2.setInferredText("Fact 2");

        List<FactDTO> facts = List.of(fact1, fact2);

        when(factService.getAllFacts()).thenReturn(facts);

        List<FactDTO> response = factController.getFacts();

        assertEquals(facts, response);
        verify(factService, times(1)).getAllFacts();
    }
}