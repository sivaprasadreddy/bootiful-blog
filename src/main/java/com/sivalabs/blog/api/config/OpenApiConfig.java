package com.sivalabs.blog.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
class OpenApiConfig {

    @ConfigurationProperties(prefix = "blog.openapi")
    @Validated
    record OpenAPIProperties(
            @DefaultValue("Bootiful Blog API") String title,
            @DefaultValue("Bootiful Blog API Swagger Documentation") String description,
            @DefaultValue("v1.0.0") String version,
            OpenAPIProperties.Contact contact) {

        record Contact(@DefaultValue("SivaLabs") String name, @DefaultValue("support@sivalabs.in") String email) {}
    }

    @Bean
    OpenAPI openApi(OpenAPIProperties properties) {
        Contact contact = new Contact()
                .name(properties.contact().name())
                .email(properties.contact().email());
        Info info = new Info()
                .title(properties.title())
                .description(properties.description())
                .version(properties.version())
                .contact(contact);
        return new OpenAPI()
                .info(info)
                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .components(new Components().addSecuritySchemes("Bearer", createJwtTokenScheme()));
    }

    private SecurityScheme createJwtTokenScheme() {
        return new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("Bearer");
    }
}
