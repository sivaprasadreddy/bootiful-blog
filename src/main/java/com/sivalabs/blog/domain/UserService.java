package com.sivalabs.blog.domain;

import com.sivalabs.blog.domain.models.User;
import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);

    Optional<User> login(String email, String password);
}
