package com.sivalabs.blog.config;

import com.sivalabs.blog.domain.SecurityUser;
import com.sivalabs.blog.domain.User;
import com.sivalabs.blog.domain.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
class SecurityUserDetailsService implements UserDetailsService {
    private final UserService userService;

    SecurityUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) {
        return userService
                .findByEmail(userName)
                .map(this::toSecurityUser)
                .orElseThrow(() -> new UsernameNotFoundException("Email " + userName + " not found"));
    }

    private SecurityUser toSecurityUser(User user) {
        return new SecurityUser(user.getId(), user.getEmail(), user.getPassword(), user.getRole());
    }
}
