package com.sivalabs.blog;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
public abstract class AbstractIT {

    // Note: Using RestAssured, TestRestTemplate, etc. to demonstrate how to test using different approaches.
    // In real projects, you should better stick with one approach.

    @LocalServerPort
    public int serverPort;

    @Autowired
    protected TestRestTemplate restTemplate;

    @PostConstruct
    public void initRestAssured() {
        RestAssured.port = serverPort;
        RestAssured.urlEncodingEnabled = false;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
