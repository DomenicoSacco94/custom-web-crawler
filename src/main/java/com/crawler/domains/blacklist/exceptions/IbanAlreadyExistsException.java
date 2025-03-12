package com.crawler.domains.blacklist.exceptions;

public class IbanAlreadyExistsException extends RuntimeException {
    public IbanAlreadyExistsException(String message) {
        super("IBAN already exists: " + message);
    }
}
