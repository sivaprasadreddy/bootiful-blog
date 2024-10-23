package com.sivalabs.blog.security;

import com.sivalabs.blog.domain.models.Role;
import com.sivalabs.blog.domain.models.User;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUser implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String username;
    private final String password;
    private final Role role;

    public SecurityUser(User user) {
        this.id = user.id();
        this.username = user.email();
        this.password = user.password();
        this.role = user.role();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }
}
