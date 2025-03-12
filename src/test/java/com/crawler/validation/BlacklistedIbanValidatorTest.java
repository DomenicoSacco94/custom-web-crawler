package com.crawler.validation;

import com.crawler.domains.blacklist.BlacklistedIbanRepository;
import com.crawler.domains.scanner.exceptions.BlacklistedIbanDetectedException;
import com.crawler.domains.scanner.validator.BlacklistedIbanValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class BlacklistedIbanValidatorTest {

    @Mock
    private BlacklistedIbanRepository ibanRepository;

    private BlacklistedIbanValidator validator;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new BlacklistedIbanValidator(ibanRepository);
        when(ibanRepository.findAllBy()).thenReturn(List.of(() -> "DE89 3704 0044 0532 0130 00"));
    }

    @Test
    public void testDocWithoutBlacklist() {
        String docText = "some text extracted from the document DE89 3704 0044 0532 0130 01";
        Errors errors = new BeanPropertyBindingResult(docText, "docText");

        validator.validate(docText, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    public void testDocWithBlacklistedIban() {
        String docText = "some text extracted from the document DE89370400440532013000";
        Errors errors = new BeanPropertyBindingResult(docText, "docText");

        validator.validate(docText, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.getAllErrors().stream()
                .anyMatch(error -> Objects.equals(error.getCode(), BlacklistedIbanDetectedException.class.getSimpleName())));
    }

    @Test
    public void testDocWithIbanWithSpaces() {
        String docText = "some text extracted from the document DE89 3704 0044 0532 0130 00";
        Errors errors = new BeanPropertyBindingResult(docText, "docText");

        validator.validate(docText, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.getAllErrors().stream()
                .anyMatch(error -> Objects.equals(error.getCode(), BlacklistedIbanDetectedException.class.getSimpleName())));
    }
}