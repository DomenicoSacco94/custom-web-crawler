package com.crawler.domains.regexps.exceptions;

public class PatternAlreadyExistsException extends RuntimeException {
    public PatternAlreadyExistsException(String message) {
        super("Pattern already exists: " + message);
    }
}
