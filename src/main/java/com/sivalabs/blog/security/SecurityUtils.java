package com.sivalabs.blog.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtils {

    public static Long getCurrentUserIdOrThrow() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Jwt jwt) {
            Long userId = jwt.getClaim("user_id");
            if (userId != null) {
                return userId;
            }
            throw new AccessDeniedException("Access denied");
        }
        if (principal instanceof SecurityUser securityUser) {
            return securityUser.getId();
        }
        throw new AccessDeniedException("Access denied");
    }
}
