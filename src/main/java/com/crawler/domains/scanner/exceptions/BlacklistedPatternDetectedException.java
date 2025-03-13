package com.crawler.domains.scanner.exceptions;

import com.crawler.domains.occurrences.models.OccurrenceDTO;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BlacklistedPatternDetectedException extends RuntimeException {
    private final List<OccurrenceDTO> detectedRegexps;

    public BlacklistedPatternDetectedException(List<OccurrenceDTO> detectedRegexps) {
        super("Blacklisted Regexp(s) detected: " + formatDetectedRegexps(detectedRegexps));
        this.detectedRegexps = detectedRegexps;
    }

    private static String formatDetectedRegexps(List<OccurrenceDTO> detectedRegexps) {
        return detectedRegexps.stream()
                .map(entity -> String.format("Pattern: %s, Description: %s, Text: %s", entity.getRegexp().getPattern(), entity.getRegexp().getDescription(), entity.getSurroundingText()))
                .collect(Collectors.joining("; "));
    }
}