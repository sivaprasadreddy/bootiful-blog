package com.sivalabs.blog.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "blog.cors")
@Validated
public record CorsProperties(
        @DefaultValue("/api/**") String pathPattern,
        @DefaultValue("*") String allowedOrigins,
        @DefaultValue("*") String allowedMethods,
        @DefaultValue("*") String allowedHeaders) {}
