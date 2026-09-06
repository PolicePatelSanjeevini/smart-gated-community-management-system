package com.smartcommunity.exception;

import com.smartcommunity.dto.response.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .success(false)
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .timestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDetails> handleBadRequestException(
            BadRequestException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .success(false)
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .timestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorDetails> handleDuplicateResourceException(
            DuplicateResourceException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .success(false)
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .timestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDetails> handleUnauthorizedException(
            UnauthorizedException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .success(false)
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .timestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleBadCredentialsException(
            BadCredentialsException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .success(false)
                .message("Invalid email or password")
                .details(request.getDescription(false))
                .timestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleSpringAccessDeniedException(
            Exception ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .success(false)
                .message("Access denied: You do not have permission to perform this action")
                .details(request.getDescription(false))
                .timestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorDetails errorDetails = ErrorDetails.builder()
                .success(false)
                .message("Validation failed for input arguments")
                .validationErrors(errors)
                .details(request.getDescription(false))
                .timestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(
            Exception ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .success(false)
                .message(ex.getMessage() != null ? ex.getMessage() : "An unexpected server error occurred")
                .details(request.getDescription(false))
                .timestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
