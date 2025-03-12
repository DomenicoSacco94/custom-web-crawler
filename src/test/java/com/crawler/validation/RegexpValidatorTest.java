package com.crawler.validation;

import com.crawler.domains.regexps.RegexpRepository;
import com.crawler.domains.scanner.exceptions.BlacklistedPatternDetectedException;
import com.crawler.domains.scanner.validator.BlacklistedPatternValidator;
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

public class RegexpValidatorTest {

    @Mock
    private RegexpRepository regexpRepository;

    private BlacklistedPatternValidator validator;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new BlacklistedPatternValidator(regexpRepository);
        when(regexpRepository.findAllBy()).thenReturn(List.of(() -> "DE15\\s3006\\s0601\\s0505\\s7807\\s80"));
    }

    @Test
    public void testDocWithoutBlacklist() {
        String docText = "some text extracted from the document DE89 3704 0044 0532 0130 01";
        Errors errors = new BeanPropertyBindingResult(docText, "docText");

        validator.validate(docText, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    public void testDocWithBlacklistedRegexp() {
        String docText = "some text extracted from the document DE15 3006 0601 0505 7807 80";
        Errors errors = new BeanPropertyBindingResult(docText, "docText");

        validator.validate(docText, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.getAllErrors().stream()
                .anyMatch(error -> Objects.equals(error.getCode(), BlacklistedPatternDetectedException.class.getSimpleName())));
    }
}