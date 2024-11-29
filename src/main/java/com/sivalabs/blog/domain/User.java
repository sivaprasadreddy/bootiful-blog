package com.sivalabs.blog.domain;

public record User(Long id, String name, String email, String password, Role role) {}
