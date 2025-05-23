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
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScannerServiceImpl implements ScannerService {

    public static final String KAFKA_PAGE_SCAN_TOPIC = "page-scan-topic";
    private static final int FETCH_RATE_MILLISECONDS = 200;
    private static final int THREAD_POOL_SIZE = 5;
    public static final int CRAWLER_MAX_DEPTH = 2;
    private final OccurrenceMapper occurrenceMapper;
    private final RegexpService regexpService;
    private final TopicService topicService;
    private final DownloadUtils downloadUtils;
    private final OccurrenceService occurrenceService;
    private final KafkaTemplate<String, PageScanRequest> kafkaTemplate;
    private final PageCrawlerUtils pageCrawlerUtils;
    private final Set<String> scannedLinks = new ConcurrentSkipListSet<>();

    @Override
    public List<OccurrenceDTO> onScanRequest(PageScanRequest request) {
        try {
            String pageUrl = request.getUrl();
            Long topicId = request.getTopicId();
            int currentDepth = request.getDepth();

            if (scannedLinks.contains(pageUrl)) {
                log.info("URL already analyzed: {}", pageUrl);
                return List.of();
            }

            if (currentDepth > CRAWLER_MAX_DEPTH) {
                log.info("Maximum depth reached for URL: {}", pageUrl);
                return List.of();
            }

            log.info("Analyzing page URL: {} at depth {}", pageUrl, currentDepth);
            String textContent = downloadUtils.downloadAndExtractText(pageUrl);

            List<OccurrenceDTO> occurrences = regexpService.detectPatterns(textContent, topicId, pageUrl);

            List<OccurrenceDTO> savedOccurrences = occurrenceService.saveAll(occurrences).stream().map(occurrenceMapper::toDto).toList();
            savedOccurrences.forEach(occurrenceService::onOccurrence);
            scannedLinks.add(pageUrl);

            Set<String> newLinks = pageCrawlerUtils.extractLinksFromPage(pageUrl, scannedLinks);

            for (String newLink : newLinks) {
                log.info("Sending Kafka message for link: {} at depth {}", newLink, currentDepth + 1);
                kafkaTemplate.send(KAFKA_PAGE_SCAN_TOPIC, new PageScanRequest(newLink, topicId, currentDepth + 1));
            }

            return savedOccurrences;

        } catch (IOException e) {
            throw new ScanException("Failed to process the web page at: " + request.getUrl(), e);
        }
    }

    public void scanBulkPages(BulkPageScanRequest request) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
        Long topicId = request.getTopicId();
        topicService.findTopicById(topicId);
        List<String> urls = request.getUrls();
        for (int i = 0; i < urls.size(); i++) {
            final String url = urls.get(i);
            PageScanRequest pageScanRequest = new PageScanRequest(url, topicId, 0);
            scheduler.schedule(() -> kafkaTemplate.send(KAFKA_PAGE_SCAN_TOPIC, pageScanRequest), (long) i * FETCH_RATE_MILLISECONDS, TimeUnit.MILLISECONDS);
        }

        scheduler.shutdown();
    }
}