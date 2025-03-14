package com.crawler.domains.occurrences.models;

import com.crawler.domains.regexp.models.Regexp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OccurrenceDTO {
    private Long id;
    private Regexp regexp;
    private String surroundingText;
    private String url;
}