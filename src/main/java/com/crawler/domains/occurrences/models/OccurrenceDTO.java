package com.crawler.domains.occurrences.models;

import com.crawler.domains.topics.regexp.models.RegexpDTO;
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
    private RegexpDTO regexpDTO;
    private String surroundingText;
    private String url;
}