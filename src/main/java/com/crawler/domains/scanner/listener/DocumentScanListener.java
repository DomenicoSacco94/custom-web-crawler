package com.crawler.domains.scanner.listener;

import com.crawler.domains.scanner.DocumentScannerService;
import com.crawler.domains.scanner.models.DocumentScanRequest;
import com.crawler.domains.occurrences.OccurrenceService;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.crawler.domains.scanner.DocumentScannerService.KAFKA_DOCUMENT_SCAN_TOPIC;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DocumentScanListener {

    private final DocumentScannerService scannerService;
    private final OccurrenceService occurrenceService;

    @KafkaListener(topics = KAFKA_DOCUMENT_SCAN_TOPIC, groupId = "document-scanner-group", containerFactory = "documentScanRequestKafkaListenerContainerFactory")
    public void listen(DocumentScanRequest documentScanRequest) {
        List<OccurrenceDTO> occurrences = scannerService.scanDocument(documentScanRequest);
        occurrences.forEach(occurrenceService::onOccurrence);
    }
}