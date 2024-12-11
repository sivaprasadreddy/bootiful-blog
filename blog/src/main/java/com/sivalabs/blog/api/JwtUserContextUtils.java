package com.sivalabs.blog.api;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

class JwtUserContextUtils {
    static Long getCurrentUserIdOrThrow() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Jwt jwt) {
            Long userId = jwt.getClaim("user_id");
            if (userId != null) {
                return userId;
            }
            throw new AccessDeniedException("Access denied");
        }
        throw new AccessDeniedException("Access denied");
    }
}
