package com.sivalabs.blog.api.controllers;

import com.sivalabs.blog.api.services.JwtTokenHelper;
import com.sivalabs.blog.domain.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class LoginRestController {
    private static final Logger log = LoggerFactory.getLogger(LoginRestController.class);

    private final UserService userService;
    private final JwtTokenHelper jwtTokenHelper;

    LoginRestController(UserService userService, JwtTokenHelper jwtTokenHelper) {
        this.userService = userService;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @PostMapping("/api/login")
    LoginResponse login(@RequestBody @Valid LoginRequest req) {
        log.info("Login request for email: {}", req.email());
        var user = userService
                .login(req.email(), req.password())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        var jwtToken = jwtTokenHelper.generateToken(user);
        return new LoginResponse(
                jwtToken.token(),
                jwtToken.expiresAt(),
                user.name(),
                user.email(),
                user.role().name());
    }

    public record LoginRequest(
            @NotEmpty(message = "{email.required}") @Email(message = "{email.invalid}") String email,
            @NotEmpty(message = "{password.required}") String password) {}

    public record LoginResponse(String token, Instant expiresAt, String name, String email, String role) {}
}
