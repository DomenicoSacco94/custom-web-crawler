package com.crawler.domains.scanner;

import com.crawler.domains.occurrences.OccurrenceService;
import com.crawler.domains.occurrences.mappers.OccurrenceMapper;
import com.crawler.domains.scanner.exceptions.ScanException;
import com.crawler.domains.scanner.models.PageScanRequest;
import com.crawler.domains.scanner.models.BulkPageScanRequest;
import com.crawler.domains.regexp.RegexpService;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import com.crawler.domains.topics.TopicService;
import com.crawler.utils.DownloadUtils;
import com.crawler.utils.PageCrawlerUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScannerServiceImpl implements ScannerService {

    public static final String KAFKA_DOCUMENT_SCAN_TOPIC = "document-scan-topic";
    private static final int FETCH_RATE_MILLISECONDS = 200;
    private static final int THREAD_POOL_SIZE = 5;

    private final OccurrenceMapper occurrenceMapper;
    private final RegexpService regexpService;
    private final TopicService topicService;
    private final DownloadUtils downloadUtils;
    private final OccurrenceService occurrenceService;
    private final KafkaTemplate<String, PageScanRequest> kafkaTemplate;
    private final PageCrawlerUtils pageCrawlerUtils;
    private final Set<String> scannedLinks = new HashSet<>();

    public List<OccurrenceDTO> onDocumentScanRequest(PageScanRequest request) {
        try {
            String documentUrl = request.getUrl();
            Long topicId = request.getTopicId();

            if (scannedLinks.contains(documentUrl)) {
                log.info("URL already analyzed: {}", documentUrl);
                return List.of();
            }

            log.info("Analyzing document URL: {}", documentUrl);
            String textContent = downloadUtils.downloadAndExtractText(documentUrl);

            List<OccurrenceDTO> occurrences = regexpService.detectPatterns(textContent, topicId, documentUrl);

            scannedLinks.add(documentUrl);

            Set<String> newLinks = pageCrawlerUtils.extractLinksFromPage(documentUrl, scannedLinks);

            for (String newLink : newLinks) {
                log.info("Sending Kafka message for link: {}", newLink);
                kafkaTemplate.send(KAFKA_DOCUMENT_SCAN_TOPIC, new PageScanRequest(newLink, topicId));
            }

            return occurrenceService.saveAll(occurrences).stream().map(occurrenceMapper::toDto).toList();
        } catch (IOException e) {
            throw new ScanException("Failed to process the document from: " + request.getUrl(), e);
        }
    }

    public void scanBulkDocuments(BulkPageScanRequest request) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
        Long topicId = request.getTopicId();
        topicService.findTopicById(topicId);
        List<String> urls = request.getUrls();
        for (int i = 0; i < urls.size(); i++) {
            final String url = urls.get(i);
            PageScanRequest pageScanRequest = new PageScanRequest(url, topicId);
            scheduler.schedule(() -> kafkaTemplate.send(KAFKA_DOCUMENT_SCAN_TOPIC, pageScanRequest), (long) i * FETCH_RATE_MILLISECONDS, TimeUnit.MILLISECONDS);
        }

        scheduler.shutdown();
    }
}