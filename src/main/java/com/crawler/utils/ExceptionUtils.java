package com.crawler.utils;

import com.crawler.utils.models.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ExceptionUtils {

    public ResponseEntity<ExceptionResponse> handleScannerException(Exception ex, HttpStatus status) {
        log.error("Exception: ", ex);
        return ResponseEntity.status(status)
                .body(new ExceptionResponse(status.value(), ex.getMessage()));
    }

    public String getValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        String errorMessage = String.join(", ", errors);
        log.error("Validation error: {}", errorMessage);
        return errorMessage;
    }
}