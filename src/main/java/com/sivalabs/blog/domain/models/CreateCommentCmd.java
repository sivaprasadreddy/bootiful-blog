package com.sivalabs.blog.domain.models;

public record CreateCommentCmd(String name, String email, String content, Long postId) {}
