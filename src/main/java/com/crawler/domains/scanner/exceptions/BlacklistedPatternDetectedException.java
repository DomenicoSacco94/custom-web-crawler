package com.crawler.domains.scanner.exceptions;

import com.crawler.domains.regexps.models.Regexp;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BlacklistedPatternDetectedException extends RuntimeException {
    private final List<Regexp> detectedRegexps;

    public BlacklistedPatternDetectedException(List<Regexp> detectedRegexps) {
        super("Blacklisted Regexp(s) detected: " + formatDetectedRegexps(detectedRegexps));
        this.detectedRegexps = detectedRegexps;
    }

    private static String formatDetectedRegexps(List<Regexp> detectedRegexps) {
        return detectedRegexps.stream()
                .map(entity -> String.format("Pattern: %s, Description: %s", entity.getPattern(), entity.getDescription()))
                .collect(Collectors.joining("; "));
    }
}