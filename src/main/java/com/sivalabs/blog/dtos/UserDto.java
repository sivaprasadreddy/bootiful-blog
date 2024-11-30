package com.sivalabs.blog.dtos;

import com.sivalabs.blog.domain.Role;

public record UserDto(Long id, String name, String email, String password, Role role) {}
