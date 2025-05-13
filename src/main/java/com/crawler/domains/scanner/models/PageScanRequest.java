package com.crawler.domains.scanner.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageScanRequest {
    private String url;
    private Long topicId;
    private int depth;
}