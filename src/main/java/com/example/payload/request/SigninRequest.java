package com.example.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninRequest {
    @NotBlank(message = "Email or username cannot be blank")
    private String emailOrUsername;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
