package com.crawler.domains.scanner.listener;

import com.crawler.domains.occurrences.OccurrenceService;
import com.crawler.domains.scanner.DocumentScannerService;
import com.crawler.domains.scanner.models.DocumentScanRequest;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DocumentScanListener {

    private final DocumentScannerService scannerService;
    private final OccurrenceService occurrenceService;

    @KafkaListener(topics = "document-scan-topic", groupId = "document-scanner-group")
    public void listen(String url) {
        DocumentScanRequest request = new DocumentScanRequest(url);

        List<OccurrenceDTO> occurrences = scannerService.scanDocument(request);
        occurrences.forEach(occurrence -> {
            occurrence.setUrl(url);
            occurrenceService.save(occurrence);
        });

        occurrences.forEach(occurrence -> log.info("New message: {}", occurrence));
        // and then we can send this event to another topic, and so on...
    }
}