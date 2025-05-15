package com.crawler.validators;

import com.crawler.domains.regexp.models.validator.RegexpValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class RegexpValidatorTest {

    private RegexpValidator regexpValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        regexpValidator = new RegexpValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void testValidRegexp() {
        assertTrue(regexpValidator.isValid("^[a-zA-Z0-9]+$", context));
        assertTrue(regexpValidator.isValid(".*", context));
    }

    @Test
    void testInvalidRegexp() {
        assertFalse(regexpValidator.isValid("[a-z", context));
        assertFalse(regexpValidator.isValid("(", context));
        assertFalse(regexpValidator.isValid("\\", context));
    }

    @Test
    void testNullRegexp() {
        assertFalse(regexpValidator.isValid(null, context));
    }

    @Test
    void testEmptyRegexp() {
        assertTrue(regexpValidator.isValid("", context));
    }
}