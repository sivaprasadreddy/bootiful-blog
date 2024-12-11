package com.sivalabs.blog.domain;

import java.time.LocalDateTime;

public interface PostProjection {
    Long getId();

    String getTitle();

    String getSlug();

    String getContent();

    UserProjection getCreatedBy();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();
}
