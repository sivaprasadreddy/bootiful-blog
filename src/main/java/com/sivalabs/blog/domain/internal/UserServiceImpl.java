package com.sivalabs.blog.domain.internal;

import com.sivalabs.blog.domain.UserService;
import com.sivalabs.blog.domain.models.User;
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
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).map(userMapper::toUser);
    }

    @Override
    public Optional<User> login(String email, String password) {
        return findByEmail(email).filter(user -> passwordEncoder.matches(password, user.password()));
    }
}
