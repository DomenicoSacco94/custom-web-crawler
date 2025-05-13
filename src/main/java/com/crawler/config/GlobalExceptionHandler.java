package com.crawler.config;

import com.crawler.domains.scanner.exceptions.ScanException;
import com.crawler.domains.scanner.exceptions.InvalidContentFormatException;
import com.crawler.domains.scanner.exceptions.InvalidUrlException;
import com.crawler.utils.exceptions.models.ExceptionResponse;
import com.crawler.utils.exceptions.ExceptionUtils;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private ExceptionUtils exceptionUtils;

    @ExceptionHandler(ScanException.class)
    public ResponseEntity<ExceptionResponse> handleScanException(ScanException ex) {
        return exceptionUtils.handleScannerException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidContentFormatException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidContentFormatException(InvalidContentFormatException ex) {
        return exceptionUtils.handleScannerException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUrlException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidUrlException(InvalidUrlException ex) {
        return exceptionUtils.handleScannerException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = exceptionUtils.getValidationErrors(ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex) {
        return exceptionUtils.handleScannerException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}