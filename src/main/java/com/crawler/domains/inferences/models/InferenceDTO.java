package com.crawler.domains.inferences.models;

import lombok.Data;

@Data
public class InferenceDTO {
    private Long id;
    private Long occurrenceId;
    private String inferredText;
}