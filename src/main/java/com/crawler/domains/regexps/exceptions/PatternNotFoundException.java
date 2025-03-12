package com.crawler.domains.regexps.exceptions;

public class PatternNotFoundException extends RuntimeException {

    public PatternNotFoundException(Long id) {
        super("Pattern not found with id: " + id);
    }
}
