package com.example.advice;

import com.example.exception.ConflictException;
import com.example.exception.TokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Wrong Data: %s", e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException (NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Not Found: %s", e.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> handleConflictException (ConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(String.format("Conflict: %s", e.getMessage()));
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<String> handleTokenException (TokenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(String.format("Token Problem: %s", e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException (AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(String.format("Access Denied: %s", e.getMessage()));
    }
}

