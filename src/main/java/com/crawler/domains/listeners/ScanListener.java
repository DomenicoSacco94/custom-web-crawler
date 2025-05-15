package com.crawler.domains.listeners;

import com.crawler.domains.scanner.ScannerService;
import com.crawler.domains.scanner.models.PageScanRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.crawler.domains.scanner.ScannerServiceImpl.KAFKA_PAGE_SCAN_TOPIC;

@Service
@AllArgsConstructor
@Slf4j
public class ScanListener {

    private final ScannerService scannerService;

    @KafkaListener(topics = KAFKA_PAGE_SCAN_TOPIC, groupId = "scanner-group", containerFactory = "scanRequestKafkaListenerContainerFactory")
    public void listen(PageScanRequest pageScanRequest) {
        scannerService.onScanRequest(pageScanRequest);
    }
}