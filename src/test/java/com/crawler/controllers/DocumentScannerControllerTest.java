package com.crawler.controllers;

import com.crawler.domains.regexp.models.Regexp;
import com.crawler.domains.scanner.DocumentScannerController;
import com.crawler.domains.scanner.DocumentScannerService;
import com.crawler.domains.scanner.models.BulkDocumentScanRequest;
import com.crawler.domains.scanner.models.DocumentScanRequest;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
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
        List<OccurrenceDTO> mockOccurrences = List.of(new OccurrenceDTO(new Regexp(), "surroundingText", null));
        when(scannerService.scanDocument(any(DocumentScanRequest.class))).thenReturn(mockOccurrences);

        DocumentScanRequest request = new DocumentScanRequest("http://example.com/document.pdf");

        ResponseEntity<List<OccurrenceDTO>> response = documentScannerController.scanDocument(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOccurrences, response.getBody());
        verify(scannerService, times(1)).scanDocument(any(DocumentScanRequest.class));
    }

    @Test
    void testScanUploadedDocument() {
        MultipartFile file = mock(MultipartFile.class);
        List<OccurrenceDTO> mockOccurrences = List.of(new OccurrenceDTO(new Regexp(), "surroundingText", null));
        when(scannerService.scanUploadedDocument(any(MultipartFile.class))).thenReturn(mockOccurrences);

        ResponseEntity<List<OccurrenceDTO>> response = documentScannerController.scanUploadedDocument(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOccurrences, response.getBody());
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