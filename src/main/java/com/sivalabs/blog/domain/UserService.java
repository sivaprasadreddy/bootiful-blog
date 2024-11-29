package com.sivalabs.blog.domain;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);

    Optional<User> login(String email, String password);

    List<User> findAllUsers();
}
