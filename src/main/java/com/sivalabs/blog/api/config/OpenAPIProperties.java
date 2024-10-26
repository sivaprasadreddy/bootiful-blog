package com.sivalabs.blog.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "blog.openapi")
@Validated
public record OpenAPIProperties(
        @DefaultValue("Bootiful Blog API") String title,
        @DefaultValue("Bootiful Blog API Swagger Documentation") String description,
        @DefaultValue("v1.0.0") String version,
        Contact contact) {

    public record Contact(@DefaultValue("SivaLabs") String name, @DefaultValue("support@sivalabs.in") String email) {}
}
