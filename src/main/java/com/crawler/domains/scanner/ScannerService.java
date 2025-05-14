package com.crawler.domains.scanner;

import com.crawler.domains.scanner.models.PageScanRequest;
import com.crawler.domains.scanner.models.BulkPageScanRequest;
import com.crawler.domains.occurrences.models.OccurrenceDTO;

import java.util.List;

public interface ScannerService {
    List<OccurrenceDTO> onScanRequest(PageScanRequest request);
    void scanBulkDocuments(BulkPageScanRequest request);
}