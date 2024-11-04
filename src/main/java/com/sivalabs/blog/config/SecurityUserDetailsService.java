package com.sivalabs.blog.config;

import com.sivalabs.blog.domain.UserService;
import com.sivalabs.blog.domain.models.SecurityUser;
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
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Email " + userName + " not found"));
    }
}
