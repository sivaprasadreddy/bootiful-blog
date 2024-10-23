package com.sivalabs.blog.domain.models;

import java.time.Instant;

public record JwtToken(String token, Instant expiresAt) {}
