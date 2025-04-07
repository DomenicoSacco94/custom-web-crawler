package com.crawler.domains.topics.regexp.models.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexpValidator implements ConstraintValidator<ValidRegexp, String> {

    @Override
    public void initialize(ValidRegexp constraintAnnotation) {
    }

    @Override
    public boolean isValid(String regexp, ConstraintValidatorContext context) {
        if (!isValidRegexp(regexp)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid regular expression: " + regexp)
                   .addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean isValidRegexp(String regexp) {
        if (regexp == null) {
            return false;
        }
        try {
            Pattern.compile(regexp);
        } catch (PatternSyntaxException e) {
            return false;
        }
        return true;
    }
}