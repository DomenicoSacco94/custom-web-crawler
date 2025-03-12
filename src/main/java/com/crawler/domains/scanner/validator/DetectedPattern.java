package com.crawler.domains.scanner.validator;

import com.crawler.domains.regexps.models.Regexp;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetectedPattern {
    private Regexp regexp;
    private String surroundingText;
}