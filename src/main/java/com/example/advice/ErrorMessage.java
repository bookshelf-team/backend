package com.example.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorMessage {
    private int status;
    private String error;
    private String message;
    private String path;
}
