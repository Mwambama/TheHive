package com.example.thehiveapp;

import com.example.thehiveapp.dto.authentication.CompanySignUpRequest;
import com.example.thehiveapp.dto.authentication.EmployerSignUpRequest;
import com.example.thehiveapp.dto.authentication.StudentSignUpRequest;
import com.example.thehiveapp.dto.jobPosting.JobPostingDto;
import com.example.thehiveapp.entity.user.Company;
import com.example.thehiveapp.entity.user.Employer;
import com.example.thehiveapp.entity.user.Student;
import com.example.thehiveapp.service.authentication.AuthenticationService;
import com.example.thehiveapp.service.jobPosting.JobPostingService;
import com.example.thehiveapp.service.user.CompanyService;
import com.example.thehiveapp.service.user.EmployerService;
import com.example.thehiveapp.service.user.StudentService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RilloSystemTest {

    @LocalServerPort
    private int port;

    @Autowired private CompanyService companyService;
    @Autowired private StudentService studentService;
    @Autowired private EmployerService employerService;
    @Autowired private JobPostingService jobPostingService;
    @Autowired private AuthenticationService authenticationService;

    private Long companyId;
    private Long studentId;
    private Long employerId;
    private Long jobPostingId;
    private Long anotherJobPostingId;

    // Set Up Users
    private final String COMPANY_NAME = "Company";
    private final String COMPANY_EMAIL = "company@test.com";
    private final String COMPANY_PASSWORD = "Password1@";
    private final String STUDENT_NAME = "Student";
    private final String STUDENT_EMAIL = "student@test.com";
    private final String STUDENT_PASSWORD = "Password1@";
    private final String STUDENT_UNIVERSITY = "University";
    private final String EMPLOYER_NAME = "Employer";
    private final String EMPLOYER_EMAIL = "employer@test.com";
    private final String EMPLOYER_PASSWORD = "Password1@";
    private final Double STUDENT_GPA = 3.5;

    // Set Up Job Postings
    private final String JOB_POSTING_TITLE = "Job Posting Title";
    private final String ANOTHER_JOB_POSTING_TITLE = "Another Job Posting Title";
    private final String JOB_POSTING_DESCRIPTION = "Job Posting Description";
    private final String ANOTHER_JOB_POSTING_DESCRIPTION = "Another Job Posting Description";
    private final Double JOB_POSTING_MINIMUM_GPA = 3.5;
    private final Double ANOTHER_JOB_POSTING_MINIMUM_GPA = 3.0;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

        // Create test company
        CompanySignUpRequest companySignUpRequest = new CompanySignUpRequest();
        companySignUpRequest.setName(COMPANY_NAME);
        companySignUpRequest.setEmail(COMPANY_EMAIL);
        companySignUpRequest.setPassword(COMPANY_PASSWORD);
        Company company = authenticationService.signUpCompany(companySignUpRequest);
        companyId = company.getUserId();

        // Create test student
        StudentSignUpRequest studentSignUpRequest = new StudentSignUpRequest();
        studentSignUpRequest.setName(STUDENT_NAME);
        studentSignUpRequest.setEmail(STUDENT_EMAIL);
        studentSignUpRequest.setPassword(STUDENT_PASSWORD);
        studentSignUpRequest.setUniversity(STUDENT_UNIVERSITY);
        Student student = authenticationService.signUpStudent(studentSignUpRequest);
        student.setGpa(STUDENT_GPA);
        studentService.updateStudent(student);
        studentId = student.getUserId();

        // Create test employer
        EmployerSignUpRequest employerSignUpRequest = new EmployerSignUpRequest();
        employerSignUpRequest.setName(EMPLOYER_NAME);
        employerSignUpRequest.setEmail(EMPLOYER_EMAIL);
        employerSignUpRequest.setPassword(EMPLOYER_PASSWORD);
        employerSignUpRequest.setCompanyId(companyId);
        Employer employer = authenticationService.signUpEmployer(employerSignUpRequest);
        employerId = employer.getUserId();

        // Create test job posting
        JobPostingDto jobPostingDto = jobPostingService.createJobPosting(
                JobPostingDto.builder()
                        .title(JOB_POSTING_TITLE)
                        .description(JOB_POSTING_DESCRIPTION)
                        .minimumGpa(JOB_POSTING_MINIMUM_GPA)
                        .employerId(employerId)
                        .build()
        );
        jobPostingId = jobPostingDto.getJobPostingId();

        // Create another test job posting
        JobPostingDto anotherJobPostingDto = jobPostingService.createJobPosting(
                JobPostingDto.builder()
                        .title(ANOTHER_JOB_POSTING_TITLE)
                        .description(ANOTHER_JOB_POSTING_DESCRIPTION)
                        .minimumGpa(ANOTHER_JOB_POSTING_MINIMUM_GPA)
                        .employerId(employerId)
                        .build()
        );
        anotherJobPostingId = anotherJobPostingDto.getJobPostingId();
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

    @Test
    void searchTest() {
        // Test case 1: Search by keyword
        assertSearch("Developer", null, null, null, null, null, null, 200, 2);

        // Test case 2: Filter by minimum salary
        assertSearch(null, BigDecimal.valueOf(60000), null, null, null, null, null, 200, 1);

        // Test case 3: Filter by maximum salary
        assertSearch(null, null, BigDecimal.valueOf(50000), null, null, null, null, 200, 1);

        // Test case 4: Filter by job start date range
        assertSearch(null, null, null, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), null, null, 200, 1);

        // Test case 5: Filter by open applications
        assertSearch(null, null, null, null, null, true, null, 200, 2);

        // Test case 6: No results for unmatched filter
        assertSearch(null, BigDecimal.valueOf(100000), null, null, null, null, null, 200, 0);
    }

    private void assertSearch(String q, BigDecimal minSalary, BigDecimal maxSalary, LocalDate minJobStart, LocalDate maxJobStart, Boolean isApplicationOpen, Boolean isQualified, int expectedStatus, int expectedResults) {
        Response response = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .queryParam("q", q)
                .queryParam("minSalary", minSalary)
                .queryParam("maxSalary", maxSalary)
                .queryParam("minJobStart", minJobStart)
                .queryParam("maxJobStart", maxJobStart)
                .queryParam("isApplicationOpen", isApplicationOpen)
                .queryParam("isQualified", isQualified)
                .when()
                .get("/job-posting/search");

        assertEquals(expectedStatus, response.getStatusCode());
        assertEquals(expectedResults, response.jsonPath().getList("$").size(), "Expected number of results does not match");
    }


}
