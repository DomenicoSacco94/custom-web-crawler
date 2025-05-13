package com.crawler.services;

import com.crawler.domains.occurrences.models.OccurrenceDTO;
import com.crawler.utils.DownloadUtils;
import com.crawler.domains.scanner.ScannerService;
import com.crawler.domains.scanner.exceptions.ScanException;
import com.crawler.domains.scanner.models.PageScanRequest;
import com.crawler.domains.regexp.RegexpService;
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
public class ScannerServiceTest {

    @Mock
    private DownloadUtils downloadUtils;

    @Mock
    private RegexpService patternProcessor;

    @InjectMocks
    private ScannerService scannerService;

    @Test
    public void testScanPdfDocument() throws IOException {
        String pdfUrl = "http://example.com/document.pdf";
        String pdfText = "Sample PDF text";
        Long topicId = 1L;

        when(downloadUtils.downloadAndExtractText(pdfUrl)).thenReturn(pdfText);
        when(patternProcessor.detectPatterns(pdfText, topicId, pdfUrl)).thenReturn(List.of(new OccurrenceDTO()));

        PageScanRequest request = new PageScanRequest(pdfUrl, topicId);
        List<OccurrenceDTO> occurrences = scannerService.onDocumentScanRequest(request);

        assertNotNull(occurrences);
        assertFalse(occurrences.isEmpty());
    }

    @Test
    public void testScanHtmlDocument() throws IOException {
        String htmlUrl = "http://example.com/page.html";
        String htmlText = "Sample HTML text";
        Long topicId = 1L;

        when(downloadUtils.downloadAndExtractText(htmlUrl)).thenReturn(htmlText);
        when(patternProcessor.detectPatterns(htmlText, topicId, htmlUrl)).thenReturn(List.of(new OccurrenceDTO()));

        PageScanRequest request = new PageScanRequest(htmlUrl, topicId);
        List<OccurrenceDTO> occurrences = scannerService.onDocumentScanRequest(request);

        assertNotNull(occurrences);
        assertFalse(occurrences.isEmpty());
    }

    @Test
    public void testOnDocumentScanRequestThrowsException() throws IOException {
        String invalidUrl = "http://example.com/invalid_document.pdf";
        Long topicId = 1L;

        when(downloadUtils.downloadAndExtractText(invalidUrl)).thenThrow(new IOException("File not found"));

        PageScanRequest request = new PageScanRequest(invalidUrl, topicId);

        assertThrows(ScanException.class, () -> scannerService.onDocumentScanRequest(request));
    }
}