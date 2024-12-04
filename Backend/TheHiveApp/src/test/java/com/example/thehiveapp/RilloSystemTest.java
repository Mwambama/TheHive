package com.example.thehiveapp;

import com.example.thehiveapp.dto.authentication.CompanySignUpRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RilloSystemTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    void signUpCompany() {
        // Create a request object
        CompanySignUpRequest companySignUpRequest = new CompanySignUpRequest();
        companySignUpRequest.setEmail("test23r24r43f3lDkf@test.com");
        companySignUpRequest.setPassword("PDassword234r@3#");
        companySignUpRequest.setName("Test");

        // Send the request and validate the response
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(companySignUpRequest)
                .when()
                .post("/account/signup/company");

        assertEquals(200, response.getStatusCode());
    }
}
