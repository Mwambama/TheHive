package com.example.thehiveapp;

import com.example.thehiveapp.dto.authentication.EmployerSignUpRequest;
import com.example.thehiveapp.entity.user.Company;
import com.example.thehiveapp.enums.user.Role;
import com.example.thehiveapp.service.user.CompanyService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RilloSystemTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CompanyService companyService;

    private Long companyId;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

        // Add initial values to database
        setUpCompany();
    }

    @Test
    void testSignUp() {
        // Test case 1: Successful sign-up
        assertSignUp("validemail@test.com", "ValidPassword1@", "Test User", companyId, 200, null);

        // Test case 2: Invalid password
        assertSignUp("validemail2@test.com", "short", "Test User", companyId, 400, "Password must have at least 8 characters");

        // Test case 3: Duplicate email
        assertSignUp("duplicate@test.com", "ValidPassword1@", "Test User", companyId, 200, null);
        assertSignUp("duplicate@test.com", "ValidPassword1@", "Test User", companyId, 400, "User already exists with this email");

        // Test case 4: Missing email
        assertSignUp("", "ValidPassword1@", "Test User", companyId, 400, "Email is required");

        // Test case 5: Invalid email format
        assertSignUp("invalidemail", "ValidPassword1@", "Test User", companyId, 400, "Invalid email format");

        // Test case 6: Missing password
        assertSignUp("validemail3@test.com", "", "Test User", companyId, 400, "Password is required");

        // Test case 7: Missing name
        assertSignUp("validemail4@test.com", "ValidPassword1@", "", companyId, 400, "Name is required");

        // Test case 8: Password missing special characters
        assertSignUp("validemail5@test.com", "Password123", "Test User", companyId, 400, "Password must have at least one digit, one uppercase letter, one lowercase letter, and one special character");

        // Test case 9: Missing companyId
        assertSignUp("validemail6@test.com", "ValidPassword1@", "Test User", null, 400, "Company Id is required");
    }

    private void assertSignUp(String email, String password, String name, Long companyId, int expectedStatus, String expectedError) {
        EmployerSignUpRequest employerSignUpRequest = new EmployerSignUpRequest();
        employerSignUpRequest.setEmail(email);
        employerSignUpRequest.setPassword(password);
        employerSignUpRequest.setName(name);
        employerSignUpRequest.setCompanyId(companyId);

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(employerSignUpRequest)
                .when()
                .post("/account/signup/employer");

        assertEquals(expectedStatus, response.getStatusCode());

        if (expectedError != null) {
            assertTrue(response.asString().contains(expectedError), "Expected error message: " + expectedError);
        }
    }

    private void setUpCompany() {
        Company company = new Company();
        company.setName("Test Company");
        company.setEmail("info@testcompany.com");
        company.setRole(Role.COMPANY);
        company = companyService.createCompany(company);
        companyId = company.getUserId();
    }
}
