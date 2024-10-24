package com.sivalabs.blog.events;

public record PostPublishedEvent(String title, String slug, String content) {}
