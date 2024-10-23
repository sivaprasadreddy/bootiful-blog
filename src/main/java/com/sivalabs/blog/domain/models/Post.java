package com.sivalabs.blog.domain.models;

import java.time.LocalDateTime;

public record Post(
        Long id,
        String title,
        String slug,
        String content,
        String createdByUserName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public String getShortDescription() {
        return content.length() > 200 ? content.substring(0, 200) + "..." : content;
    }
}
