package com.crawler.domains.facts.models;

import com.crawler.domains.occurrences.models.OccurrenceDTO;
import lombok.Data;

@Data
public class FactDTO {
    private Long id;
    private OccurrenceDTO occurrenceDTO;
    private String inferredText;
    private String consequences;
}