package com.example.controller;

import com.example.payload.request.RefreshTokenRequest;
import com.example.payload.request.SigninRequest;
import com.example.payload.request.SignupRequest;
import com.example.payload.response.JwtResponse;
import com.example.payload.response.RefreshTokenResponse;
import com.example.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PreAuthorize("permitAll()")
    @PostMapping("/signin")
    public JwtResponse authenticateUser(@Valid @RequestBody SigninRequest signinRequest) {
        return authService.authenticateUser(signinRequest);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/signup")
    public String registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("/signout")
    public String logoutUser() {
        return authService.logoutUser();
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/refresh")
    public RefreshTokenResponse refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request);
    }
}
