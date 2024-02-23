package com.example.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninRequest {
    @NotBlank
    private String emailOrUsername;

    @NotBlank
    private String password;
}
