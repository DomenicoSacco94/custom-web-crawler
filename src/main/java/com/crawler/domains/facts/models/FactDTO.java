package com.crawler.domains.facts.models;

import lombok.Data;

@Data
public class FactDTO {
    private Long id;
    private Long occurrenceId;
    private String inferredText;
}