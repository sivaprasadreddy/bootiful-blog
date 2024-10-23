package com.sivalabs.blog;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "blog")
@Validated
public record ApplicationProperties(
        @DefaultValue("10") int postsPerPage,
        RSAPublicKey publicKey,
        RSAPrivateKey privateKey,
        OpenAPIProperties openapi,
        CorsProperties cors,
        JwtProperties jwt) {

    public record JwtProperties(
            @DefaultValue("BootifulBlog") String issuer, @DefaultValue("604800") Long expiresInSeconds) {}

    public record OpenAPIProperties(
            @DefaultValue("Bootiful Blog API") String title,
            @DefaultValue("Bootiful Blog API Swagger Documentation") String description,
            @DefaultValue("v1.0.0") String version,
            Contact contact) {

        public record Contact(
                @DefaultValue("SivaLabs") String name, @DefaultValue("support@sivalabs.in") String email) {}
    }

    public record CorsProperties(
            @DefaultValue("/api/**") String pathPattern,
            @DefaultValue("*") String allowedOrigins,
            @DefaultValue("*") String allowedMethods,
            @DefaultValue("*") String allowedHeaders) {}
}
