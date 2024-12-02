package com.sivalabs.blog.api;

import com.sivalabs.blog.dtos.UserDto;
import java.time.Instant;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

@Component
class JwtTokenHelper {
    private final JwtEncoder encoder;
    private final JwtProperties jwtProperties;

    JwtTokenHelper(JwtEncoder encoder, JwtProperties jwtProperties) {
        this.encoder = encoder;
        this.jwtProperties = jwtProperties;
    }

    public JwtToken generateToken(UserDto userDto) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(jwtProperties.expiresInSeconds());
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.issuer())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(userDto.email())
                .claim("user_id", userDto.id())
                .claim("role", userDto.role().name())
                .build();
        var token = this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new JwtToken(token, expiresAt);
    }
}
