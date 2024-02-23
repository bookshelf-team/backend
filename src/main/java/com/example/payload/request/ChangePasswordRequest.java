package com.example.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    @NotBlank
    String username;

    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 6, max = 40)
    private String newPassword;
}
