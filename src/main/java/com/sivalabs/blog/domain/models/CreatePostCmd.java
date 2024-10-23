package com.sivalabs.blog.domain.models;

public record CreatePostCmd(String title, String slug, String content, Long createdBy) {}
