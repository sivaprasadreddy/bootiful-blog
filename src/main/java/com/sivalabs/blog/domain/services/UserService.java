package com.sivalabs.blog.domain.services;

import com.sivalabs.blog.domain.mappers.UserMapper;
import com.sivalabs.blog.domain.models.User;
import com.sivalabs.blog.domain.repositories.UserRepository;
import java.util.Optional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toUser);
    }

    public Optional<User> login(String email, String password) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new BadCredentialsException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return Optional.empty();
        }
        return Optional.of(userMapper.toUser(user));
    }
}
