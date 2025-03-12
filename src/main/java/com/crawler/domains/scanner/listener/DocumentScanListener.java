package com.crawler.domains.scanner.listener;

import com.crawler.domains.scanner.DocumentScannerService;
import com.crawler.domains.scanner.models.DocumentValidationMessage;
import com.crawler.domains.scanner.models.DocumentScanRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class DocumentScanListener {

    private final DocumentScannerService scannerService;

    @KafkaListener(topics = "document-scan-topic", groupId = "document-scanner-group")
    public void listen(String url) {
        DocumentScanRequest request = new DocumentScanRequest(url);
        DocumentValidationMessage documentValidationMessage;
        try {
            scannerService.scanDocument(request);
            documentValidationMessage = new DocumentValidationMessage(request.getUrl(), "Document is valid");
        } catch (Exception e) {
            documentValidationMessage = new DocumentValidationMessage(request.getUrl(), e.getMessage());
        }
        log.info("New message: {}", documentValidationMessage);
        // and then we can send this event to another topic, and so on...
    }
}