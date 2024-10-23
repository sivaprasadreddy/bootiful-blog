package com.sivalabs.blog.api.controllers;

import com.sivalabs.blog.api.models.LoginRequest;
import com.sivalabs.blog.api.models.LoginResponse;
import com.sivalabs.blog.domain.models.JwtToken;
import com.sivalabs.blog.domain.services.UserService;
import com.sivalabs.blog.security.JwtTokenHelper;
import jakarta.validation.Valid;
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
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

        JwtToken jwtToken = jwtTokenHelper.generateToken(user);
        return new LoginResponse(
                jwtToken.token(),
                jwtToken.expiresAt(),
                user.name(),
                user.email(),
                user.role().name());
    }
}
