package com.crawler.domains.scanner.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class BlacklistedIbanDetectedException extends RuntimeException {
    private final List<String> detectedIbans;

    public BlacklistedIbanDetectedException(List<String> detectedIbans) {
        super("Blacklisted IBAN(s) detected: " + detectedIbans);
        this.detectedIbans = detectedIbans;
    }

}