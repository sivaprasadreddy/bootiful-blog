package com.sivalabs.blog;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@TestConfiguration(proxyBeanMethods = false)
@Testcontainers
public class TestcontainersConfiguration {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @Container
    static GenericContainer<?> mailhog = new GenericContainer<>("mailhog/mailhog:v1.0.1").withExposedPorts(1025);

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgres() {
        return postgres;
    }

    @Bean
    DynamicPropertyRegistrar dynamicPropertyRegistrar(GenericContainer<?> mailhog) {
        return (registry) -> {
            registry.add("spring.mail.host", mailhog::getHost);
            registry.add("spring.mail.port", mailhog::getFirstMappedPort);
        };
    }
}
