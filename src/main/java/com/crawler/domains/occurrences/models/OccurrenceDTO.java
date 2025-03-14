package com.crawler.domains.occurrences.models;

import com.crawler.domains.regexp.models.Regexp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OccurrenceDTO {
    private Long id;
    private String pattern;
    private String description;
    private String surroundingText;
    private String url;
}