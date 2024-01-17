package com.example.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignupRequest {
    @NotBlank(message = "Username cannot be null")
    @Size(min = 3, max = 20)
    private String username;

    @Email(message = "Email should be valid")
    private String email;

    private Set<String> role;

    @NotBlank(message = "Password cannot be null")
    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
    private String password;
}
