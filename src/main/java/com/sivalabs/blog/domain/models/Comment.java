package com.sivalabs.blog.domain.models;

import java.time.LocalDateTime;

public record Comment(
        Long id, String name, String email, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {}
