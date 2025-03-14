package com.crawler.domains.occurrences.models;

import com.crawler.domains.regexp.models.Regexp;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OccurrenceDTO {
    private Regexp regexp;
    private String surroundingText;
    private String url;
}