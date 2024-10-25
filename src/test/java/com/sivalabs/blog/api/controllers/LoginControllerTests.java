package com.sivalabs.blog.api.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

import com.sivalabs.blog.TestcontainersConfig;
import com.sivalabs.blog.api.controllers.LoginRestController.LoginRequest;
import com.sivalabs.blog.api.controllers.LoginRestController.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestcontainersConfig.class)
class LoginControllerTests {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldLoginSuccessfully() {
        var payload = new LoginRequest("siva@gmail.com", "siva");
        ResponseEntity<LoginResponse> response =
                restTemplate.postForEntity("/api/login", payload, LoginResponse.class, "introducing-springboot");
        assertThat(response.getStatusCode()).isEqualTo(OK);
        LoginResponse loginResponse = response.getBody();
        assertThat(loginResponse.token()).isNotBlank();
        assertThat(loginResponse.email()).isEqualTo("siva@gmail.com");
        assertThat(loginResponse.name()).isEqualTo("Siva Prasad");
    }
}
