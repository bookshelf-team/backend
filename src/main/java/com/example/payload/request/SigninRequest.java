package com.example.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninRequest {
    @NotBlank(message = "Email or username cannot be null")
    private String emailOrUsername;

    @NotBlank(message = "Password cannot be null")
    private String password;
}
