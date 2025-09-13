package com.example.Kiraflow.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.validation.FieldError;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    record ApiError(String message, int status, Instant timestamp, Map<String, String> fieldErrors, String path) {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex, WebRequest req) {
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a,b) -> a + ", " + b));

        ApiError error = new ApiError("Validation failed", HttpStatus.BAD_REQUEST.value(), Instant.now(), fieldErrors, req.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException ex, WebRequest req) {
        ApiError error = new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), Instant.now(), Collections.emptyMap(), req.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> handleNotFound(NoSuchElementException ex, WebRequest req) {
        ApiError error = new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND.value(), Instant.now(), Collections.emptyMap(), req.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(Exception ex, WebRequest req) {
        ApiError error = new ApiError("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), Instant.now(), Collections.emptyMap(), req.getDescription(false));
        ex.printStackTrace(); // for dev; remove or log properly in prod
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex, WebRequest req) {
        ApiError error = new ApiError(ex.getMessage(), HttpStatus.FORBIDDEN.value(), Instant.now(), Collections.emptyMap(), req.getDescription(false));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
}
