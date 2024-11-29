package com.sivalabs.blog.domain.internal;

import com.sivalabs.blog.domain.User;
import com.sivalabs.blog.domain.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUser).toList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).map(userMapper::toUser);
    }

    @Override
    public Optional<User> login(String email, String password) {
        return findByEmail(email).filter(user -> passwordEncoder.matches(password, user.password()));
    }
}
