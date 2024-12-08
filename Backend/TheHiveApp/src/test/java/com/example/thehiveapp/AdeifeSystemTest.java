package com.example.thehiveapp;

import com.example.thehiveapp.dto.application.ApplicationRequest;
import com.example.thehiveapp.dto.authentication.CompanySignUpRequest;
import com.example.thehiveapp.dto.authentication.EmployerSignUpRequest;
import com.example.thehiveapp.dto.authentication.StudentSignUpRequest;
import com.example.thehiveapp.dto.email.ChangePasswordRequest;
import com.example.thehiveapp.dto.jobPosting.JobPostingDto;
import com.example.thehiveapp.dto.otp.OtpRequest;
import com.example.thehiveapp.dto.otp.OtpValidationRequest;
import com.example.thehiveapp.entity.otp.Otp;
import com.example.thehiveapp.entity.user.Company;
import com.example.thehiveapp.entity.user.Employer;
import com.example.thehiveapp.entity.user.Student;
import com.example.thehiveapp.enums.jobPosting.JobType;
import com.example.thehiveapp.service.application.ApplicationService;
import com.example.thehiveapp.service.authentication.AuthenticationService;
import com.example.thehiveapp.service.jobPosting.JobPostingService;
import com.example.thehiveapp.service.otp.OtpService;
import com.example.thehiveapp.service.user.StudentService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AdeifeSystemTest {
    @LocalServerPort
    private int port;

    @Autowired private StudentService studentService;
    @Autowired private ApplicationService applicationService;
    @Autowired private JobPostingService jobPostingService;
    @Autowired private AuthenticationService authenticationService;
    @Autowired private OtpService otpService;

    private Long companyId;
    private Long studentId;
    private Long employerId;
    private Long jobPostingId1;
    private Long jobPostingId2;
    private Long jobPostingId3;
    private Long applicationId;
    private String otp;

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
    private final Double STUDENT_GPA = 3.0;

    // Set Up Job Postings
    private final String JOB_POSTING_TITLE_1 = "Job Posting Title 1";
    private final String JOB_POSTING_TITLE_2 = "Job Posting Title 2";
    private final String JOB_POSTING_DESCRIPTION_1 = "Job Posting Description 1";
    private final String JOB_POSTING_DESCRIPTION_2 = "Job Posting Description 2";
    private final String JOB_POSTING_SUMMARY_1 = "Job Posting Summary";
    private final String JOB_POSTING_SUMMARY_2 = "Another Job Posting Summary";
    private final BigDecimal SMALL_SALARY = BigDecimal.valueOf(23.5);
    private final BigDecimal LARGE_SALARY = BigDecimal.valueOf(40);
    private final Double SMALL_GPA = 1.0;
    private final Double LARGE_GPA = 3.5;
    private final LocalDate JOB_POSTING_APPLICATION_START_1 = LocalDate.now().minusWeeks(2); // Application 1 open
    private final LocalDate JOB_POSTING_APPLICATION_START_2 = LocalDate.now().minusWeeks(1); // Application 2 open
    private final LocalDate JOB_POSTING_APPLICATION_END_1 = JOB_POSTING_APPLICATION_START_1.plusMonths(2);
    private final LocalDate JOB_POSTING_APPLICATION_END_2 = JOB_POSTING_APPLICATION_START_2.plusMonths(2);
    private final LocalDate JOB_POSTING_JOB_START_1 = JOB_POSTING_APPLICATION_END_1.plusMonths(1);
    private final LocalDate JOB_POSTING_JOB_START_2 = JOB_POSTING_APPLICATION_END_2.plusMonths(3);

    @BeforeAll
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

        // Create test job posting 1
        JobPostingDto jobPostingDto1 = jobPostingService.createJobPosting(
                JobPostingDto.builder()
                        .title(JOB_POSTING_TITLE_1)
                        .description(JOB_POSTING_DESCRIPTION_1)
                        .summary(JOB_POSTING_SUMMARY_1)
                        .salary(SMALL_SALARY)
                        .jobType(JobType.POST_GRADUATION_JOB)
                        .minimumGpa(SMALL_GPA)
                        .applicationStart(JOB_POSTING_APPLICATION_START_1)
                        .applicationEnd(JOB_POSTING_APPLICATION_END_1)
                        .jobStart(JOB_POSTING_JOB_START_1)
                        .employerId(employerId)
                        .build()
        );
        jobPostingId1 = jobPostingDto1.getJobPostingId();

        // Create test job posting 2
        JobPostingDto jobPostingDto2 = jobPostingService.createJobPosting(
                JobPostingDto.builder()
                        .title(JOB_POSTING_TITLE_2)
                        .description(JOB_POSTING_DESCRIPTION_2)
                        .summary(JOB_POSTING_SUMMARY_2)
                        .salary(LARGE_SALARY)
                        .jobType(JobType.INTERNSHIP)
                        .minimumGpa(LARGE_GPA)
                        .applicationStart(JOB_POSTING_APPLICATION_START_2)
                        .applicationEnd(JOB_POSTING_APPLICATION_END_2)
                        .jobStart(JOB_POSTING_JOB_START_2)
                        .employerId(employerId)
                        .build()
        );
        jobPostingId2 = jobPostingDto2.getJobPostingId();

        // Create test job posting 3
        JobPostingDto jobPostingDto3 = jobPostingService.createJobPosting(
                JobPostingDto.builder()
                        .title(JOB_POSTING_TITLE_2)
                        .description(JOB_POSTING_DESCRIPTION_2)
                        .summary(JOB_POSTING_SUMMARY_2)
                        .salary(LARGE_SALARY)
                        .jobType(JobType.INTERNSHIP)
                        .minimumGpa(LARGE_GPA)
                        .applicationStart(JOB_POSTING_APPLICATION_START_2)
                        .applicationEnd(JOB_POSTING_APPLICATION_END_2)
                        .jobStart(JOB_POSTING_JOB_START_2)
                        .employerId(employerId)
                        .build()
        );
        jobPostingId3 = jobPostingDto3.getJobPostingId();

        // Create application for student with job posting 3
        applicationId = applicationService.applyForJobPosting(
                ApplicationRequest.builder()
                        .studentId(studentId)
                        .jobPostingId(jobPostingId3)
                        .build()).getApplicationId();

        // Create an otp for password reset for student
        otp = otpService.sendOtp(
                OtpRequest.builder()
                        .email(STUDENT_EMAIL)
                        .build()).getOtpResponse().getOtp();
    }
    @Test
    void testApply() {
        // Test case 1: Successful application
        assertApply(studentId, jobPostingId1, 200, null);

        // Test case 2: Student already applied to job posting
        assertApply(studentId, jobPostingId1, 500, "Student has already applied for this job posting!");

        // Test case 3: Student is able to apply for multiple job postings
        assertApply(studentId, jobPostingId2, 200, null);

        // Test case 4: Testing invalid student id
        assertApply(-1L, jobPostingId1, 404, "Student not found");

        // Test case 5: Testing invalid job posting id
        assertApply(studentId, -1L, 404, "Job posting not found");

        // Test case 6: Testing missing student id
        assertApply(null, jobPostingId1, 500, "The given id must not be null");

        // Test case 7: Testing missing job posting id
        assertApply(studentId, null, 500, "The given id must not be null");
    }

    private void assertApply(Long studentId, Long jobPostingId, int expectedStatus, String expectedError){
        ApplicationRequest applicationRequest = new ApplicationRequest();
        applicationRequest.setStudentId(studentId);
        applicationRequest.setJobPostingId(jobPostingId);

        Response response = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .contentType("application/json")
                .body(applicationRequest)
                .when()
                .post("/applications/apply");

        assertEquals(expectedStatus, response.getStatusCode());

        if (expectedError != null) {
            assertTrue(response.asString().contains(expectedError), "Expected error message: " + expectedError);
        }
    }

    @Test
    void testAcceptApplication(){
        // Test case 1: Test employer can accept an application
        assertAcceptApplication(applicationId, 200, null);

        // Test case 2: Testing invalid application
        assertAcceptApplication(-1L, 404, "Application not found");
    }

    private void assertAcceptApplication(Long applicationId, int expectedStatus, String expectedError){
        Response response = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .when()
                .post("/applications/" + applicationId + "/accept");

        assertEquals(expectedStatus, response.getStatusCode());

        if (expectedError != null) {
            assertTrue(response.asString().contains(expectedError), "Expected error message: " + expectedError);
        }
    }

    @Test
    void testValidateOtp(){
        // Test case 1: Correct validation of the otp
        assertValidateOtp(STUDENT_EMAIL, otp, false, 200, null);

        // Test case 2: Testing no email provided
        assertValidateOtp("", otp, false, 200, "You have not sent an otp!");

        // Test case 3: Invalid OTP for student
        assertValidateOtp(STUDENT_EMAIL, "InvalidOtp", false, 200, "Invalid otp!");

        // Test case 4: Expired OTP
        assertValidateOtp(STUDENT_EMAIL, otp, true, 200, "Expired otp!");
    }

    private void assertValidateOtp(String email, String otp, boolean expired, int expectedStatus, String expectedError){
        OtpValidationRequest otpValidationRequest = new OtpValidationRequest();
        otpValidationRequest.setEmail(email);
        otpValidationRequest.setOtp(otp);
        if (expired){
            otpService.updateOtp(
                    Otp.builder()
                            .otp(otp)
                            .email(email)
                            .expiresAt(LocalDateTime.now().minusHours(1))
                            .build());
        }

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(otpValidationRequest)
                .when()
                .post("/otp/validateOtp");

        assertEquals(expectedStatus, response.getStatusCode());

        if (expectedError != null) {
            String responseBody = response.asString();
            assertTrue(responseBody.contains("\"responseMessage\":\"" + expectedError + "\""),
                    "Expected error message: " + expectedError);
        }
    }

    @Test
    void testChangePassword(){
        // Test case 1: Unable to use old password
        assertChangePassword(STUDENT_EMAIL, STUDENT_PASSWORD, STUDENT_PASSWORD, 400, "You cannot use old password! Try again!");

        // Test case 2: Password changed successfully
        assertChangePassword(STUDENT_EMAIL, "testNewPassword2024*", "testNewPassword2024*", 200, null);

        // Test case 3: Password and confirm password mismatch
        assertChangePassword(STUDENT_EMAIL, "testNewPassword2025*", "testNewPassword2024*", 400, "Passwords do not match! Try again!");
    }

    private void assertChangePassword(String email, String password, String confirmPassword, int expectedStatus, String expectedError){
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setEmail(email);
        changePasswordRequest.setPassword(password);
        changePasswordRequest.setConfirmPassword(confirmPassword);

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(changePasswordRequest)
                .when()
                .post("/account/change-password");

        assertEquals(expectedStatus, response.getStatusCode());

        if (expectedError != null) {
            String responseBody = response.asString();
            assertTrue(responseBody.contains("\"message\":\"" + expectedError + "\""),
                    "Expected error message: " + expectedError);
        }

    }

    @Test
    void testJobAnalytics(){
        // Test case 1: Successful Retrieval of Job Analytics
        assertJobAnalytics(jobPostingId3, 200, null);

        // Test case 2: Invalid job posting id
        assertJobAnalytics(-1L, 404, "Job Posting not found with id -1");
    }

    private void assertJobAnalytics(Long jobPostingId3, int expectedStatus, String expectedError){
        Response response = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .when()
                .get("/job-posting/analytics/" + jobPostingId3);

        assertEquals(expectedStatus, response.getStatusCode());
        if (expectedError != null) {
            assertTrue(response.asString().contains(expectedError), "Expected error message: " + expectedError);
        }
    }



}
