package com.crawler.domains.regexp.exceptions;

public class PatternNotFoundException extends RuntimeException {

    public PatternNotFoundException(Long id) {
        super("Pattern not found with id: " + id);
    }
}
