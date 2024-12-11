package com.sivalabs.blog.domain;

public record UpdatePostCmd(Long id, String title, String slug, String content) {}
