package com.sivalabs.blog.config;

import com.sivalabs.blog.ApplicationProperties;
import com.sivalabs.blog.domain.ConsoleLoggingEmailService;
import com.sivalabs.blog.domain.EmailService;
import com.sivalabs.blog.domain.RealEmailService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
class AppConfig {

    @Bean
    @ConditionalOnProperty(name = "emailservice.type", havingValue = "real", matchIfMissing = true)
    EmailService realEmailService(JavaMailSender javaMailSender, ApplicationProperties properties) {
        return new RealEmailService(javaMailSender, properties);
    }

    @Bean
    @ConditionalOnProperty(name = "emailservice.type", havingValue = "console")
    EmailService consoleLoggingEmailService(ApplicationProperties properties) {
        return new ConsoleLoggingEmailService(properties);
    }
}
