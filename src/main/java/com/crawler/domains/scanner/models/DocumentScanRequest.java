package com.crawler.domains.scanner.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentScanRequest {
    private String url;
}