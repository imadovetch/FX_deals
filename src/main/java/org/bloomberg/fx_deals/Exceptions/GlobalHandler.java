package org.bloomberg.fx_deals.Exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalHandler {

    /**
     * Handles validation errors from @Valid annotated DTOs in request bodies.
     * Returns a structured JSON with errors grouped by list index and field.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, List<Map<String, String>>> errorsGroupedByIndex = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String field = error.getField(); // e.g. "dealDtos[0].dealUniqueId"
            // Extract the index and the actual property name
            String index = "unknown";
            String property = field;

            // Regex to extract index from something like "dealDtos[0].dealUniqueId"
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(".*\\[(\\d+)\\]\\.(.+)");
            java.util.regex.Matcher matcher = pattern.matcher(field);

            if (matcher.matches()) {
                index = matcher.group(1);       // e.g. "0"
                property = matcher.group(2);    // e.g. "dealUniqueId"
            }

            Map<String, String> errorDetails = Map.of(
                    "property", property,
                    "message", error.getDefaultMessage()
            );

            errorsGroupedByIndex.computeIfAbsent(index, k -> new java.util.ArrayList<>()).add(errorDetails);
        }

        Map<String, Object> responseBody = Map.of(
                "timestamp", Instant.now(),
                "type", "Validation Error",
                "status", HttpStatus.BAD_REQUEST.value(),
                "errors", errorsGroupedByIndex
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }


    /**
     * Handles ConstraintViolationException from @Validated method parameters.
     * Returns errors with property path and message in a list.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, List<Map<String, String>>> errorsGroupedByIndex = new HashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            String path = violation.getPropertyPath().toString(); // e.g. "importDeals.dealDtos[0].dealUniqueId"
            String index = "unknown";
            String property = path;

            // Extract index and property name using regex
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(".*\\[(\\d+)\\]\\.(.+)");
            java.util.regex.Matcher matcher = pattern.matcher(path);

            if (matcher.matches()) {
                index = matcher.group(1);
                property = matcher.group(2);
            }

            Map<String, String> errorDetails = Map.of(
                    "property", property,
                    "message", violation.getMessage()
            );

            errorsGroupedByIndex.computeIfAbsent(index, k -> new java.util.ArrayList<>()).add(errorDetails);
        });

        Map<String, Object> responseBody = Map.of(
                "timestamp", Instant.now(),
                "type", "Validation Error",
                "status", HttpStatus.BAD_REQUEST.value(),
                "errors", errorsGroupedByIndex
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }


    // The rest of your existing handlers remain unchanged
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ApiError errorDetails = new ApiError(
                Instant.now(),
                "Malformed Request",
                "Request body is missing or malformed.",
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ApiError errorDetails = new ApiError(
                Instant.now(),
                "Database Error",
                "A database error occurred: " + ex.getRootCause().getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        ApiError errorDetails = new ApiError(
                Instant.now(),
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        // Optionally log ex here
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiError {
        private Instant timestamp;
        private String type;
        private String message;
        private int status;
    }
}
