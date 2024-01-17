package com.example.util;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TimeProvider {
    public Instant now() {
        return Instant.now();
    }
}
