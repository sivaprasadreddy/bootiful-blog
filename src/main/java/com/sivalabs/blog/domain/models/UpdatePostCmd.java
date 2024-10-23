package com.sivalabs.blog.domain.models;

public record UpdatePostCmd(Long id, String title, String slug, String content) {}
