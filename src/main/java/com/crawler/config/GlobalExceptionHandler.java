package com.crawler.config;

import com.crawler.domains.regexps.exceptions.PatternAlreadyExistsException;
import com.crawler.domains.regexps.exceptions.PatternNotFoundException;
import com.crawler.domains.scanner.exceptions.DocumentScanException;
import com.crawler.domains.scanner.exceptions.InvalidDocumentFormatException;
import com.crawler.domains.scanner.exceptions.InvalidDocumentUrlException;
import com.crawler.utils.models.ExceptionResponse;
import com.crawler.utils.ExceptionUtils;
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

    @ExceptionHandler(DocumentScanException.class)
    public ResponseEntity<ExceptionResponse> handleDocumentScanException(DocumentScanException ex) {
        return exceptionUtils.handleScannerException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidDocumentFormatException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidDocumentFormatException(InvalidDocumentFormatException ex) {
        return exceptionUtils.handleScannerException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDocumentUrlException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidDocumentUrlException(InvalidDocumentUrlException ex) {
        return exceptionUtils.handleScannerException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PatternNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleRegexpNotFoundException(PatternNotFoundException ex) {
        return exceptionUtils.handleScannerException(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PatternAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleRegexpAlreadyExistsException(PatternAlreadyExistsException ex) {
        return exceptionUtils.handleScannerException(ex, HttpStatus.CONFLICT);
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