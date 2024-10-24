package com.sivalabs.blog.domain.models;

import java.time.LocalDateTime;

/**
 * Projection for {@link com.sivalabs.blog.domain.entities.PostEntity}
 */
public interface PostProjection {
    Long getId();

    String getTitle();

    String getSlug();

    String getContent();

    UserProjection getCreatedBy();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();
}
