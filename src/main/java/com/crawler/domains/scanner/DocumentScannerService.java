package com.crawler.domains.scanner;

import com.crawler.domains.occurrences.OccurrenceService;
import com.crawler.domains.occurrences.mappers.OccurrenceMapper;
import com.crawler.domains.scanner.exceptions.DocumentScanException;
import com.crawler.domains.scanner.models.DocumentScanRequest;
import com.crawler.domains.scanner.models.BulkDocumentScanRequest;
import com.crawler.domains.scanner.processors.DocumentPatternProcessor;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentScannerService {

    private final OccurrenceMapper occurrenceMapper;
    private final DocumentPatternProcessor patternValidator;
    private final DocumentDownloadService documentDownloadService;
    private final OccurrenceService occurrenceService;
    private final KafkaTemplate<String, DocumentScanRequest> kafkaTemplate;
    public static final String KAFKA_DOCUMENT_SCAN_TOPIC = "document-scan-topic";
    private static final int FETCH_RATE_MILLISECONDS = 200;
    private static final int THREAD_POOL_SIZE = 15;

    public List<OccurrenceDTO> scanDocument(DocumentScanRequest request) {
        try {
            String documentUrl = request.getUrl();
            Long topicId = request.getTopicId();
            String textContent = documentDownloadService.downloadAndExtractText(documentUrl);
            List<OccurrenceDTO> occurrences = patternValidator.detectPatterns(textContent, topicId);
            occurrences.forEach(occurrence -> {
                occurrence.setUrl(documentUrl);
                occurrence.setTopicId(topicId);
            });
            return occurrenceService.saveAll(occurrences).stream().map(occurrenceMapper::toDto).toList();
        } catch (IOException e) {
            throw new DocumentScanException("Failed to process the document from: " + request.getUrl(), e);
        }
    }

    public void scanBulkDocuments(BulkDocumentScanRequest request) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
        Long topicId = request.getTopicId();
        List<String> urls = request.getUrls();
        for (int i = 0; i < urls.size(); i++) {
            final String url = urls.get(i);
            DocumentScanRequest documentScanRequest = new DocumentScanRequest(url, topicId);
            scheduler.schedule(() -> kafkaTemplate.send(KAFKA_DOCUMENT_SCAN_TOPIC, documentScanRequest), (long) i * FETCH_RATE_MILLISECONDS, TimeUnit.MILLISECONDS);
        }

        scheduler.shutdown();
    }
}