package com.sivalabs.blog.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class WebApiConfig implements WebMvcConfigurer {
    private final CorsProperties corsProperties;

    WebApiConfig(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(corsProperties.pathPattern())
                .allowedOriginPatterns(corsProperties.allowedOrigins())
                .allowedMethods(corsProperties.allowedMethods())
                .allowedHeaders(corsProperties.allowedHeaders());
    }

    @ConfigurationProperties(prefix = "blog.cors")
    @Validated
    record CorsProperties(
            @DefaultValue("/api/**") String pathPattern,
            @DefaultValue("*") String allowedOrigins,
            @DefaultValue("*") String allowedMethods,
            @DefaultValue("*") String allowedHeaders) {}
}
