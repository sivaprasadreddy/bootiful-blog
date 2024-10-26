package com.sivalabs.blog.api.services;

import java.time.Instant;

public record JwtToken(String token, Instant expiresAt) {}
