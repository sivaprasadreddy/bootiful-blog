package com.sivalabs.blog.domain.models;

public record User(Long id, String name, String email, String password, Role role) {}
