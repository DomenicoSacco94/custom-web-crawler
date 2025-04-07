package com.crawler.domains.scanner.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BulkDocumentScanRequest {
    private List<String> urls;
    private Long topicId;
}