package com.sivalabs.blog.domain;

public record PostPublishedEvent(String title, String slug, String content) {}
