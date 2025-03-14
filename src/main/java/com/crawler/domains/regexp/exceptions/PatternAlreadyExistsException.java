package com.crawler.domains.regexp.exceptions;

public class PatternAlreadyExistsException extends RuntimeException {
    public PatternAlreadyExistsException(String message) {
        super("Pattern already exists: " + message);
    }
}
