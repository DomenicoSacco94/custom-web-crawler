package com.crawler.domains.listeners;

import com.crawler.domains.scanner.ScannerService;
import com.crawler.domains.scanner.models.PageScanRequest;
import com.crawler.domains.occurrences.OccurrenceService;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.crawler.domains.scanner.ScannerServiceImpl.KAFKA_DOCUMENT_SCAN_TOPIC;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ScanListener {

    private final ScannerService scannerService;
    private final OccurrenceService occurrenceService;

    @KafkaListener(topics = KAFKA_DOCUMENT_SCAN_TOPIC, groupId = "scanner-group", containerFactory = "documentScanRequestKafkaListenerContainerFactory")
    public void listen(PageScanRequest pageScanRequest) {
        List<OccurrenceDTO> occurrences = scannerService.onScanRequest(pageScanRequest);
        occurrences.forEach(occurrenceService::onOccurrence);
    }
}