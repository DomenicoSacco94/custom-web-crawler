package com.crawler.domains.blacklist.models.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IbanFormatValidator implements ConstraintValidator<ValidIban, String> {

    @Override
    public void initialize(ValidIban constraintAnnotation) {
    }

    @Override
    public boolean isValid(String iban, ConstraintValidatorContext context) {
        if (!isValidIban(iban)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid IBAN: " + iban)
                   .addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean isValidIban(String iban) {
        return iban != null && iban.matches("([A-Z0-9]{4} ){3,7}[A-Z0-9]{1,4}") && iban.length() >= 19 && iban.length() <= 39;
    }
}