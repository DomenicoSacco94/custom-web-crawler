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
        assertTrue(regexpValidator.isValid("^[a-zA-Z0-9]+$", context)); // Alphanumeric regex
        assertTrue(regexpValidator.isValid(".*", context)); // Match any string
        assertTrue(regexpValidator.isValid("\\d{3}-\\d{2}-\\d{4}", context)); // Social Security Number format
    }

    @Test
    void testInvalidRegexp() {
        assertFalse(regexpValidator.isValid("[a-z", context)); // Unclosed character class
        assertFalse(regexpValidator.isValid("(", context)); // Unclosed group
        assertFalse(regexpValidator.isValid("\\", context)); // Trailing escape character
    }

    @Test
    void testNullRegexp() {
        assertFalse(regexpValidator.isValid(null, context)); // Null input
    }

    @Test
    void testEmptyRegexp() {
        assertTrue(regexpValidator.isValid("", context)); // Empty string is technically valid
    }
}