package com.crawler.domains.scanner.exceptions;

public class InvalidDocumentUrlException extends RuntimeException {
    public InvalidDocumentUrlException(String message) {
        super(message);
    }
}
