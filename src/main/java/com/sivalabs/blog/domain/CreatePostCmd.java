package com.sivalabs.blog.domain;

public record CreatePostCmd(String title, String slug, String content, Long createdBy) {}
