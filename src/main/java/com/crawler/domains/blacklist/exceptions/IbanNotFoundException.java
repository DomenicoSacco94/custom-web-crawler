package com.crawler.domains.blacklist.exceptions;

public class IbanNotFoundException extends RuntimeException {

    public IbanNotFoundException(Long id) {
        super("IBAN not found with id: " + id);
    }
}
