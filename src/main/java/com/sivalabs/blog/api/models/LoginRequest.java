package com.sivalabs.blog.api.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotEmpty(message = "{email.required}") @Email(message = "{email.invalid}") String email,
        @NotEmpty(message = "{password.required}") String password) {}
