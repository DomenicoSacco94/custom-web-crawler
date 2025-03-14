package com.crawler.controllers;

import com.crawler.domains.regexp.RegexpController;
import com.crawler.domains.regexp.RegexpService;
import com.crawler.domains.regexp.models.RegexpDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegexpControllerTest {

    @Mock
    private RegexpService regexpService;

    @InjectMocks
    private RegexpController regexpController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPattern() {
        RegexpDTO dto = new RegexpDTO();
        when(regexpService.addPattern(any(RegexpDTO.class))).thenReturn(dto);

        ResponseEntity<RegexpDTO> response = regexpController.addPattern(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(regexpService, times(1)).addPattern(dto);
    }

    @Test
    void testGetAllPatterns() {
        List<RegexpDTO> patterns = Arrays.asList(new RegexpDTO(), new RegexpDTO());
        when(regexpService.getAllPatterns()).thenReturn(patterns);

        ResponseEntity<List<RegexpDTO>> response = regexpController.getAllPatterns();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patterns, response.getBody());
        verify(regexpService, times(1)).getAllPatterns();
    }

    @Test
    void testGetPatternById() {
        RegexpDTO dto = new RegexpDTO();
        when(regexpService.getPatternById(1L)).thenReturn(dto);

        ResponseEntity<RegexpDTO> response = regexpController.getPatternById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(regexpService, times(1)).getPatternById(1L);
    }

    @Test
    void testUpdatePattern() {
        RegexpDTO dto = new RegexpDTO();
        when(regexpService.updatePattern(any(RegexpDTO.class))).thenReturn(dto);

        ResponseEntity<RegexpDTO> response = regexpController.updatePattern(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(regexpService, times(1)).updatePattern(dto);
    }

    @Test
    void testDeletePattern() {
        doNothing().when(regexpService).deletePattern(1L);

        ResponseEntity<Void> response = regexpController.deletePattern(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(regexpService, times(1)).deletePattern(1L);
    }
}