package com.crawler.utils;

import com.crawler.utils.models.ExceptionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExceptionUtilsTest {

    private ExceptionUtils exceptionUtils;

    @BeforeEach
    public void setUp() {
        exceptionUtils = new ExceptionUtils();
    }

    @Test
    public void testHandleScannerException() {
        Exception ex = new Exception("Test exception");
        ResponseEntity<ExceptionResponse> response = exceptionUtils.handleScannerException(ex, HttpStatus.INTERNAL_SERVER_ERROR);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Test exception", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void testGetValidationErrors() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objectName");
        bindingResult.addError(new FieldError("objectName", "field", "Field error message"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        String errorMessage = exceptionUtils.getValidationErrors(ex);

        assertEquals("Field error message", errorMessage);
    }

    @Test
    public void testGetValidationErrorsWithMultipleErrors() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objectName");
        bindingResult.addError(new FieldError("objectName", "field1", "Field1 error message"));
        bindingResult.addError(new FieldError("objectName", "field2", "Field2 error message"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        String errorMessage = exceptionUtils.getValidationErrors(ex);

        assertEquals("Field1 error message, Field2 error message", errorMessage);
    }
}