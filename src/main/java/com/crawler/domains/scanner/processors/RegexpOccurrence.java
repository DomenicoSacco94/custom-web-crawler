package com.crawler.domains.scanner.processors;

import com.crawler.domains.regexps.models.Regexp;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegexpOccurrence {
    private Regexp regexp;
    private String surroundingText;
}