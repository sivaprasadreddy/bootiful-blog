package com.sivalabs.blog.security;

import com.sivalabs.blog.ApplicationProperties;
import com.sivalabs.blog.domain.models.JwtToken;
import com.sivalabs.blog.domain.models.User;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenHelper {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenHelper.class);

    private final JwtEncoder encoder;
    private final ApplicationProperties properties;

    public JwtTokenHelper(JwtEncoder encoder, ApplicationProperties properties) {
        this.encoder = encoder;
        this.properties = properties;
    }

    public JwtToken generateToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(properties.jwt().expiresInSeconds());
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(properties.jwt().issuer())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(user.email())
                .claim("user_id", user.id())
                .claim("role", user.role().name())
                .build();
        var token = this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new JwtToken(token, expiresAt);
    }
}
