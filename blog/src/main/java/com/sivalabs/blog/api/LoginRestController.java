package com.sivalabs.blog.api;

import com.sivalabs.blog.domain.UserService;
import com.sivalabs.blog.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users API")
class LoginRestController {
    private static final Logger log = LoggerFactory.getLogger(LoginRestController.class);

    private final UserService userService;
    private final JwtTokenHelper jwtTokenHelper;
    private final UserMapper userMapper;

    LoginRestController(UserService userService, JwtTokenHelper jwtTokenHelper, UserMapper userMapper) {
        this.userService = userService;
        this.jwtTokenHelper = jwtTokenHelper;
        this.userMapper = userMapper;
    }

    @PostMapping("/api/login")
    @Operation(summary = "Authenticate user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Returns successful authentication response"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
    })
    LoginResponse login(@RequestBody @Valid LoginRequest req) {
        log.info("Login request for email: {}", req.email());
        var user = userService
                .login(req.email(), req.password())
                .map(userMapper::toUser)
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
            @NotEmpty(message = "Email is required") @Email(message = "Invalid email address") String email,
            @NotEmpty(message = "Password is required") String password) {}

    public record LoginResponse(String token, Instant expiresAt, String name, String email, String role) {}
}
