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

    public static Post from(PostProjection p) {
        return new Post(
                p.getId(),
                p.getTitle(),
                p.getSlug(),
                p.getContent(),
                p.getCreatedBy().getName(),
                p.getCreatedAt(),
                p.getUpdatedAt());
    }

    public String getShortDescription() {
        return content.length() > 200 ? content.substring(0, 200) + "..." : content;
    }
}
