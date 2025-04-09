package com.crawler.services;

import com.crawler.domains.occurrences.models.OccurrenceDTO;
import com.crawler.domains.scanner.DocumentDownloadService;
import com.crawler.domains.scanner.DocumentScannerService;
import com.crawler.domains.scanner.exceptions.DocumentScanException;
import com.crawler.domains.scanner.models.DocumentScanRequest;
import com.crawler.domains.scanner.processors.DocumentPatternProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentScannerServiceTest {

    @Mock
    private DocumentDownloadService documentDownloadService;

    @Mock
    private DocumentPatternProcessor patternProcessor;

    @InjectMocks
    private DocumentScannerService documentScannerService;

    @Test
    public void testScanPdfDocument() throws IOException {
        String pdfUrl = "http://example.com/document.pdf";
        String pdfText = "Sample PDF text";
        Long topicId = 1L;

        when(documentDownloadService.downloadAndExtractText(pdfUrl)).thenReturn(pdfText);
        when(patternProcessor.detectPatterns(pdfText, topicId)).thenReturn(List.of(new OccurrenceDTO()));

        DocumentScanRequest request = new DocumentScanRequest(pdfUrl, topicId);
        List<OccurrenceDTO> occurrences = documentScannerService.scanDocument(request);

        assertNotNull(occurrences);
        assertFalse(occurrences.isEmpty());
    }

    @Test
    public void testScanHtmlDocument() throws IOException {
        String htmlUrl = "http://example.com/page.html";
        String htmlText = "Sample HTML text";
        Long topicId = 1L;

        when(documentDownloadService.downloadAndExtractText(htmlUrl)).thenReturn(htmlText);
        when(patternProcessor.detectPatterns(htmlText, topicId)).thenReturn(List.of(new OccurrenceDTO()));

        DocumentScanRequest request = new DocumentScanRequest(htmlUrl, topicId);
        List<OccurrenceDTO> occurrences = documentScannerService.scanDocument(request);

        assertNotNull(occurrences);
        assertFalse(occurrences.isEmpty());
    }

    @Test
    public void testScanDocumentThrowsException() throws IOException {
        String invalidUrl = "http://example.com/invalid_document.pdf";
        Long topicId = 1L;

        when(documentDownloadService.downloadAndExtractText(invalidUrl)).thenThrow(new IOException("File not found"));

        DocumentScanRequest request = new DocumentScanRequest(invalidUrl, topicId);

        assertThrows(DocumentScanException.class, () -> documentScannerService.scanDocument(request));
    }
}