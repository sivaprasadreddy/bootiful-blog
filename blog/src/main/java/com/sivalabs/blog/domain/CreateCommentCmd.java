package com.sivalabs.blog.domain;

public record CreateCommentCmd(String name, String email, String content, Long postId) {}
