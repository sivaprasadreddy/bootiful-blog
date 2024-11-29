package com.sivalabs.blog.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.OK;

import com.sivalabs.blog.AbstractIT;
import com.sivalabs.blog.api.LoginRestController.LoginRequest;
import org.junit.jupiter.api.Test;

class LoginRestControllerTests extends AbstractIT {

    @Test
    void shouldLoginSuccessfully() {
        var payload = new LoginRequest("siva@gmail.com", "siva");
        given().contentType("application/json")
                .body(payload)
                .when()
                .post("/api/login")
                .then()
                .statusCode(OK.value())
                .assertThat()
                .body("token", not(blankOrNullString()))
                .body("email", equalTo("siva@gmail.com"))
                .body("name", equalTo("Siva Prasad"));
    }
}
