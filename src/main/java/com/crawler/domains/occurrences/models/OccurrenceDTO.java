package com.crawler.domains.occurrences.models;

import com.crawler.domains.regexps.models.Regexp;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OccurrenceDTO {
    private Regexp regexp;
    private String surroundingText;
    private String url;
}