package com.crawler.controllers;

import com.crawler.domains.scanner.DocumentScannerController;
import com.crawler.domains.scanner.DocumentScannerService;
import com.crawler.domains.scanner.models.BulkDocumentScanRequest;
import com.crawler.domains.scanner.models.DocumentScanRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DocumentScannerControllerTest {

    @Mock
    private DocumentScannerService scannerService;

    @InjectMocks
    private DocumentScannerController documentScannerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testScanDocument() {
        doNothing().when(scannerService).scanDocument(any(DocumentScanRequest.class));

        DocumentScanRequest request = new DocumentScanRequest("http://example.com/document.pdf");

        ResponseEntity<Void> response = documentScannerController.scanDocument(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(scannerService, times(1)).scanDocument(any(DocumentScanRequest.class));
    }

    @Test
    void testScanUploadedDocument() {
        MultipartFile file = mock(MultipartFile.class);
        doNothing().when(scannerService).scanUploadedDocument(any(MultipartFile.class));

        ResponseEntity<Void> response = documentScannerController.scanUploadedDocument(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(scannerService, times(1)).scanUploadedDocument(file);
    }

    @Test
    void testScanBulkDocuments() {
        BulkDocumentScanRequest request = new BulkDocumentScanRequest(List.of("http://example.com/document1.pdf", "http://example.com/document2.pdf"));
        doNothing().when(scannerService).scanBulkDocuments(any(BulkDocumentScanRequest.class));

        ResponseEntity<Void> response = documentScannerController.scanBulkDocuments(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}