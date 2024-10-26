package com.sivalabs.blog.api.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "blog.jwt")
@Validated
public record JwtProperties(
        @DefaultValue("BootifulBlog") String issuer,
        @DefaultValue("604800") Long expiresInSeconds,
        RSAPublicKey publicKey,
        RSAPrivateKey privateKey) {}
