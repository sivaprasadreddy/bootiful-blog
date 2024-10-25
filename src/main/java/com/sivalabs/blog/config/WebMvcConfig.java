package com.sivalabs.blog.config;

import com.sivalabs.blog.ApplicationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class WebMvcConfig implements WebMvcConfigurer {
    private final ApplicationProperties properties;

    WebMvcConfig(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addRedirectViewController("/", "/blog/posts");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(properties.cors().pathPattern())
                .allowedOriginPatterns(properties.cors().allowedOrigins())
                .allowedMethods(properties.cors().allowedMethods())
                .allowedHeaders(properties.cors().allowedHeaders());
    }
}
