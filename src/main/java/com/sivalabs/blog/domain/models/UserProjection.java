package com.sivalabs.blog.domain.models;

/**
 * Projection for {@link com.sivalabs.blog.domain.entities.UserEntity}
 */
public interface UserProjection {
    Long getId();

    String getName();

    String getEmail();
}
