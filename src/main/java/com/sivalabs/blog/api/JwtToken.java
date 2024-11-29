package com.sivalabs.blog.api;

import java.time.Instant;

public record JwtToken(String token, Instant expiresAt) {}
