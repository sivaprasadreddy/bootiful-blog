package com.sivalabs.blog.api.models;

import java.time.Instant;

public record LoginResponse(String token, Instant expiresAt, String name, String email, String role) {}
