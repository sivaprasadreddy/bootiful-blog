package com.sivalabs.blog;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "blog")
@Validated
public record ApplicationProperties(
        String supportEmail,
        String newsletterJobCron,
        @DefaultValue("10") int postsPerPage,
        JwtProperties jwt,
        OpenAPIProperties openApi) {
    public record JwtProperties(
            @DefaultValue("BootifulBlog") String issuer,
            @DefaultValue("604800") Long expiresInSeconds,
            RSAPublicKey publicKey,
            RSAPrivateKey privateKey) {}

    public record OpenAPIProperties(
            @DefaultValue("Bootiful Blog API") String title,
            @DefaultValue("Bootiful Blog API Swagger Documentation") String description,
            @DefaultValue("v1.0.0") String version,
            OpenAPIProperties.Contact contact) {

        public record Contact(
                @DefaultValue("SivaLabs") String name, @DefaultValue("support@sivalabs.in") String email) {}
    }
}
