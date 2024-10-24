package com.sivalabs.blog;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@TestConfiguration(proxyBeanMethods = false)
@Testcontainers
public class TestcontainersConfig {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    static GenericContainer<?> mailhog = new GenericContainer<>("mailhog/mailhog:v1.0.1").withExposedPorts(1025);

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgres() {
        return postgres;
    }

    static {
        mailhog.start();
        System.setProperty("spring.mail.host", mailhog.getHost());
        System.setProperty("spring.mail.port", String.valueOf(mailhog.getMappedPort(1025)));
    }
}
