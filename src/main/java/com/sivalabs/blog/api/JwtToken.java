package com.sivalabs.blog.api;

import java.time.Instant;

record JwtToken(String token, Instant expiresAt) {}
