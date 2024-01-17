package com.example.exception;

public class TokenException extends RuntimeException {
    public TokenException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
