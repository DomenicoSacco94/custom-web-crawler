package com.crawler.controllers;

import com.crawler.domains.blacklist.BlacklistedIbanController;
import com.crawler.domains.blacklist.BlacklistedIbanService;
import com.crawler.domains.blacklist.models.BlacklistedIbanDTO;
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

class BlacklistedIbanControllerTest {

    @Mock
    private BlacklistedIbanService blacklistedIbanService;

    @InjectMocks
    private BlacklistedIbanController blacklistedIbanController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddIban() {
        BlacklistedIbanDTO dto = new BlacklistedIbanDTO();
        when(blacklistedIbanService.addIban(any(BlacklistedIbanDTO.class))).thenReturn(dto);

        ResponseEntity<BlacklistedIbanDTO> response = blacklistedIbanController.addIban(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(blacklistedIbanService, times(1)).addIban(dto);
    }

    @Test
    void testGetAllIbans() {
        List<BlacklistedIbanDTO> ibans = Arrays.asList(new BlacklistedIbanDTO(), new BlacklistedIbanDTO());
        when(blacklistedIbanService.getAllIbans()).thenReturn(ibans);

        ResponseEntity<List<BlacklistedIbanDTO>> response = blacklistedIbanController.getAllIbans();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ibans, response.getBody());
        verify(blacklistedIbanService, times(1)).getAllIbans();
    }

    @Test
    void testGetIbanById() {
        BlacklistedIbanDTO dto = new BlacklistedIbanDTO();
        when(blacklistedIbanService.getIbanById(1L)).thenReturn(dto);

        ResponseEntity<BlacklistedIbanDTO> response = blacklistedIbanController.getIbanById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(blacklistedIbanService, times(1)).getIbanById(1L);
    }

    @Test
    void testUpdateIban() {
        BlacklistedIbanDTO dto = new BlacklistedIbanDTO();
        when(blacklistedIbanService.updateIban(any(BlacklistedIbanDTO.class))).thenReturn(dto);

        ResponseEntity<BlacklistedIbanDTO> response = blacklistedIbanController.updateIban(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(blacklistedIbanService, times(1)).updateIban(dto);
    }

    @Test
    void testDeleteIban() {
        doNothing().when(blacklistedIbanService).deleteIban(1L);

        ResponseEntity<Void> response = blacklistedIbanController.deleteIban(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(blacklistedIbanService, times(1)).deleteIban(1L);
    }
}