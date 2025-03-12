package com.crawler.domains.scanner.listener;

import com.crawler.domains.scanner.DocumentScannerService;
import com.crawler.domains.scanner.models.DocumentScanRequest;
import com.crawler.domains.scanner.processors.RegexpOccurrence;
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

    @KafkaListener(topics = "document-scan-topic", groupId = "document-scanner-group")
    public void listen(String url) {
        DocumentScanRequest request = new DocumentScanRequest(url);

        List<RegexpOccurrence> occurrences = scannerService.scanDocument(request);
        occurrences.forEach(occurrence-> log.info("New message: {}", occurrence));
        // and then we can send this event to another topic, and so on...
    }
}