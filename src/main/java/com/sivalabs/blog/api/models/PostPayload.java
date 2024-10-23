package com.sivalabs.blog.api.models;

import jakarta.validation.constraints.NotEmpty;

public record PostPayload(
        @NotEmpty(message = "Title is required") String title,
        @NotEmpty(message = "Slug is required") String slug,
        @NotEmpty(message = "Content is required") String content) {}
