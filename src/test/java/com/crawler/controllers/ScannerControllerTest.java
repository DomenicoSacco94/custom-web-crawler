package com.crawler.controllers;

import com.crawler.domains.scanner.ScannerController;
import com.crawler.domains.scanner.ScannerServiceImpl;
import com.crawler.domains.scanner.models.BulkPageScanRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ScannerControllerTest {

    @Mock
    private ScannerServiceImpl scannerServiceImpl;

    @InjectMocks
    private ScannerController scannerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testScanPages() {
        BulkPageScanRequest request = new BulkPageScanRequest(List.of("http://example.com/document1.pdf", "http://example.com/document2.pdf"), 1L);
        doNothing().when(scannerServiceImpl).scanBulkPages(any(BulkPageScanRequest.class));

        ResponseEntity<Void> response = scannerController.scanUrls(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}