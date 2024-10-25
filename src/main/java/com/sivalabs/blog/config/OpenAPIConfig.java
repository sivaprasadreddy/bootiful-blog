package com.sivalabs.blog.config;

import com.sivalabs.blog.ApplicationProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenAPIConfig {

    @Bean
    OpenAPI openApi(ApplicationProperties properties) {
        var openapi = properties.openapi();
        Contact contact = new Contact()
                .name(openapi.contact().name())
                .email(openapi.contact().email());
        Info info = new Info()
                .title(openapi.title())
                .description(openapi.description())
                .version(openapi.version())
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
