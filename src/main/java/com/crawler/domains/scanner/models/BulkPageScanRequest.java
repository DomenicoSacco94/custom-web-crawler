package com.crawler.domains.scanner.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class BulkPageScanRequest {

    @NotEmpty(message = "URLs list must not be empty")
    private List<String> urls;

    @NotNull(message = "Topic ID must not be null")
    private Long topicId;
}