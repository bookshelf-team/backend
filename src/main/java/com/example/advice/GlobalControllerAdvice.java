package com.example.advice;

import com.example.exception.ConflictException;
import com.example.exception.TokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    private String message;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        message = e.getMessage();
        logException();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e) {
        message = String.format("Bad Credentials: %s", e.getMessage());
        logException();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        message = String.format("Validation Failed: %s", e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(", ")));
        logException();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        message = String.format("Wrong Data: %s", e.getMessage());
        logException();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException (NoSuchElementException e) {
        message = String.format("Not Found: %s", e.getMessage());
        logException();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> handleConflictException (ConflictException e) {
        message = String.format("Conflict: %s", e.getMessage());
        logException();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<String> handleTokenException (TokenException e) {
        message = String.format("Token Problem: %s", e.getMessage());
        logException();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException (AccessDeniedException e) {
        message = String.format("Access Denied: %s", e.getMessage());
        logException();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
    }

    private void logException() {
        log.error(message);
    }
}
