package com.crawler.services;

import com.crawler.domains.occurrences.OccurrenceService;
import com.crawler.domains.occurrences.mappers.OccurrenceMapper;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import com.crawler.utils.DownloadUtils;
import com.crawler.domains.scanner.ScannerServiceImpl;
import com.crawler.domains.scanner.exceptions.ScanException;
import com.crawler.domains.scanner.models.PageScanRequest;
import com.crawler.domains.regexp.RegexpService;
import com.crawler.utils.PageCrawlerUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static com.crawler.domains.scanner.ScannerServiceImpl.CRAWLER_MAX_DEPTH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScannerServiceImplTest {

    @Mock
    private DownloadUtils downloadUtils;

    @Mock
    private RegexpService regexpService;

    @Mock
    private OccurrenceService occurrenceService;

    @Mock
    private PageCrawlerUtils pageCrawlerUtils;

    @Mock
    private OccurrenceMapper occurrenceMapper;

    @Mock
    private KafkaTemplate<String, PageScanRequest> kafkaTemplate;

    @InjectMocks
    private ScannerServiceImpl scannerServiceImpl;

    @Test
    public void testScanPdfDocument() throws IOException {
        String pdfUrl = "http://example.com/document.pdf";
        String pdfText = "Sample PDF text";
        Long topicId = 1L;

        OccurrenceDTO occurrenceDTO = new OccurrenceDTO();
        List<OccurrenceDTO> occurrencesDTO = List.of(occurrenceDTO);

        when(downloadUtils.downloadAndExtractText(pdfUrl)).thenReturn(pdfText);
        when(regexpService.detectPatterns(pdfText, topicId, pdfUrl)).thenReturn(occurrencesDTO);
        when(occurrenceService.saveAll(any())).thenReturn(List.of());

        PageScanRequest request = new PageScanRequest(pdfUrl, topicId, 0);
        List<OccurrenceDTO> occurrences = scannerServiceImpl.onScanRequest(request);

        assertNotNull(occurrences);
        verify(downloadUtils, times(1)).downloadAndExtractText(pdfUrl);
        verify(regexpService, times(1)).detectPatterns(pdfText, topicId, pdfUrl);
        verify(occurrenceService, times(1)).saveAll(occurrencesDTO);
    }

    @Test
    public void testScanHtmlDocument() throws IOException {
        String htmlUrl = "http://example.com/page.html";
        String htmlText = "Sample HTML text";
        Long topicId = 1L;

        Set<String> expectedLinks = Set.of(htmlUrl);

        OccurrenceDTO occurrenceDTO = new OccurrenceDTO();
        List<OccurrenceDTO> occurrencesDTO = List.of(occurrenceDTO);

        when(downloadUtils.downloadAndExtractText(htmlUrl)).thenReturn(htmlText);
        when(regexpService.detectPatterns(htmlText, topicId, htmlUrl)).thenReturn(occurrencesDTO);
        when(pageCrawlerUtils.extractLinksFromPage(htmlUrl, expectedLinks)).thenReturn(Set.of("http://example.com/link1"));
        when(occurrenceService.saveAll(any())).thenReturn(List.of());

        PageScanRequest request = new PageScanRequest(htmlUrl, topicId, 0);
        List<OccurrenceDTO> occurrences = scannerServiceImpl.onScanRequest(request);

        assertNotNull(occurrences);
        verify(downloadUtils, times(1)).downloadAndExtractText(htmlUrl);
        verify(regexpService, times(1)).detectPatterns(htmlText, topicId, htmlUrl);
        verify(pageCrawlerUtils, times(1)).extractLinksFromPage(htmlUrl, expectedLinks);
        verify(kafkaTemplate, times(1)).send(anyString(), any(PageScanRequest.class));
        verify(occurrenceService, times(1)).saveAll(occurrencesDTO);
    }

    @Test
    public void testOnScanRequestThrowsException() throws IOException {
        String invalidUrl = "http://example.com/invalid_document.pdf";
        Long topicId = 1L;

        when(downloadUtils.downloadAndExtractText(invalidUrl)).thenThrow(new IOException("File not found"));

        PageScanRequest request = new PageScanRequest(invalidUrl, topicId, 0);

        assertThrows(ScanException.class, () -> scannerServiceImpl.onScanRequest(request));
        verify(downloadUtils, times(1)).downloadAndExtractText(invalidUrl);
        verifyNoInteractions(regexpService, pageCrawlerUtils, kafkaTemplate);
    }

    @Test
    public void testScanWithMaxDepthReached() {
        String url = "http://example.com/document.pdf";
        Long topicId = 1L;

        PageScanRequest request = new PageScanRequest(url, topicId, CRAWLER_MAX_DEPTH);

        List<OccurrenceDTO> occurrences = scannerServiceImpl.onScanRequest(request);

        assertNotNull(occurrences);
        assertTrue(occurrences.isEmpty());
    }

    @Test
    public void testScanPageAlreadyScanned() {
        String url = "http://example.com/document.pdf";
        Long topicId = 1L;

        scannerServiceImpl.onScanRequest(new PageScanRequest(url, topicId, 0));

        PageScanRequest request = new PageScanRequest(url, topicId, 1);
        List<OccurrenceDTO> occurrences = scannerServiceImpl.onScanRequest(request);

        assertNotNull(occurrences);
        assertTrue(occurrences.isEmpty());
    }
}