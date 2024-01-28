package com.example.service;

import com.example.exception.ConflictException;
import com.example.exception.TokenException;
import com.example.model.User;
import com.example.model.Profile;
import com.example.model.Role;
import com.example.model.ERole;
import com.example.model.RefreshToken;
import com.example.payload.request.ChangePasswordRequest;
import com.example.payload.request.RefreshTokenRequest;
import com.example.payload.request.SigninRequest;
import com.example.payload.request.SignupRequest;
import com.example.payload.response.JwtResponse;
import com.example.payload.response.RefreshTokenResponse;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.repository.ProfileRepository;
import com.example.security.jwt.JwtUtils;
import com.example.security.model.UserDetailsImpl;
import com.example.security.service.RefreshTokenService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.List;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;

    private final RoleRepository roleRepository;

    private final RefreshTokenService refreshTokenService;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    private final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final Validator validator;

    @Transactional
    public JwtResponse authenticateUser(SigninRequest signinRequest) {
        Set<ConstraintViolation<SigninRequest>> violations = validator.validate(signinRequest);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", ")));
        }

        User user = null;
        if (userRepository.existsByEmail(signinRequest.getEmailOrUsername())) {
            user = userRepository.findByEmail(signinRequest.getEmailOrUsername()).orElseThrow();
        } else if (userRepository.existsByUsername(signinRequest.getEmailOrUsername())) {
            user = userRepository.findByUsername(signinRequest.getEmailOrUsername()).orElseThrow();
        }

        if (user != null) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), signinRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String jwtToken = jwtUtils.generateJwtToken(userDetails);

            userRepository.updateLastLogin(getTimeStamp(), user.getUsername());

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            refreshTokenService.deleteByUserId(userDetails.getId());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

            return new JwtResponse(jwtToken,
                    refreshToken.getRefreshToken(),
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles);
        } else {
            throw new BadCredentialsException("User is not exist");
        }
    }

    @Transactional
    public String registerUser(SignupRequest signUpRequest) {
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signUpRequest);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", ")));
        }

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ConflictException("Username is already taken");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ConflictException("Email is already in use");
        }

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                getTimeStamp());

        Profile profile = new Profile();
        profileRepository.save(profile);
        user.setProfile(profile);

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new NoSuchElementException("Role is not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new NoSuchElementException("Role is not found"));
                        roles.add(adminRole);
                    }
                    case "moderator" -> {
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new NoSuchElementException("Role is not found"));
                        roles.add(modRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new NoSuchElementException("Role is not found"));
                        roles.add(userRole);
                    }
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return "User registered successfully";
    }

    @Transactional
    public String logoutUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (refreshTokenService.deleteByUserId(userDetails.getId())) {
            return "User logged out successfully";
        }
        throw new RuntimeException("An error occurred while trying to log out");
    }

    @Transactional
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        Set<ConstraintViolation<RefreshTokenRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", ")));
        }

        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByRefreshToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    Set<String> roles = userRepository.findByUsername(user.getUsername()).orElseThrow()
                            .getRoles()
                            .stream()
                            .map(role -> role.getName().name())
                            .collect(Collectors.toSet());
                    Collection<? extends GrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    String jwtToken = jwtUtils.generateTokenFromUsername(user.getUsername(), authorities);
                    return new RefreshTokenResponse(jwtToken, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenException(requestRefreshToken,
                        "Refresh token is not in database"));
    }

    @Transactional
    public String changePassword(ChangePasswordRequest changePasswordRequest) {
        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(changePasswordRequest);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", ")));
        }

        String username = changePasswordRequest.getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User is not found"));

        String oldPassword = changePasswordRequest.getOldPassword();
        String newPassword = changePasswordRequest.getNewPassword();

        if (oldPassword.equals(newPassword)) {
            throw new IllegalArgumentException("New password is equal to the old one");
        }

        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new BadCredentialsException("Old password doesn't match");
        }

        userRepository.updatePassword(encoder.encode(newPassword), username);
        return "Password changed successfully";
    }

    private String getTimeStamp() {
        return DATE_TIME_FORMAT.format(new Date());
    }
}
