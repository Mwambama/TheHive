package com.example.thehiveapp;

import com.example.thehiveapp.dto.application.ApplicationRequest;
import com.example.thehiveapp.dto.authentication.CompanySignUpRequest;
import com.example.thehiveapp.dto.authentication.EmployerSignUpRequest;
import com.example.thehiveapp.dto.authentication.StudentSignUpRequest;
import com.example.thehiveapp.dto.chat.ChatMessageDto;
import com.example.thehiveapp.dto.jobPosting.JobPostingDto;
import com.example.thehiveapp.entity.address.Address;
import com.example.thehiveapp.entity.chat.Chat;
import com.example.thehiveapp.entity.user.Company;
import com.example.thehiveapp.entity.user.Employer;
import com.example.thehiveapp.entity.user.Student;
import com.example.thehiveapp.enums.jobPosting.JobType;
import com.example.thehiveapp.service.address.AddressService;
import com.example.thehiveapp.service.authentication.AuthenticationService;
import com.example.thehiveapp.service.chat.ChatMessageService;
import com.example.thehiveapp.service.chat.ChatService;
import com.example.thehiveapp.service.jobPosting.JobPostingService;
import com.example.thehiveapp.service.user.StudentService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class RilloSystemTest {

    @LocalServerPort
    private int port;

    @Autowired private StudentService studentService;
    @Autowired private JobPostingService jobPostingService;
    @Autowired private AuthenticationService authenticationService;
    @Autowired private AddressService addressService;
    @Autowired private ChatService chatService;
    @Autowired private ChatMessageService chatMessageService;

    private Long companyId;
    private Long studentId;
    private Long employerId;
    private Long jobPostingId;
    private Long anotherJobPostingId;
    private Long addressId;

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
    private final String JOB_POSTING_TITLE = "Job Posting Title";
    private final String ANOTHER_JOB_POSTING_TITLE = "Another Job Posting Title";
    private final String JOB_POSTING_DESCRIPTION = "Job Posting Description";
    private final String ANOTHER_JOB_POSTING_DESCRIPTION = "Another Job Posting Description";
    private final String JOB_POSTING_SUMMARY = "Job Posting Summary";
    private final String ANOTHER_JOB_POSTING_SUMMARY = "Another Job Posting Summary";
    private final BigDecimal SMALL_SALARY = BigDecimal.valueOf(23.5);
    private final BigDecimal LARGE_SALARY = BigDecimal.valueOf(40);
    private final Double SMALL_GPA = 1.0;
    private final Double LARGE_GPA = 3.5;
    private final LocalDate JOB_POSTING_APPLICATION_START = LocalDate.now().minusWeeks(2); // Applications open
    private final LocalDate ANOTHER_JOB_POSTING_APPLICATION_START = LocalDate.now().plusWeeks(2); // Applications open in two weeks
    private final LocalDate JOB_POSTING_APPLICATION_END = JOB_POSTING_APPLICATION_START.plusMonths(2);
    private final LocalDate ANOTHER_JOB_POSTING_APPLICATION_END = ANOTHER_JOB_POSTING_APPLICATION_START.plusMonths(2);
    private final LocalDate JOB_POSTING_JOB_START = JOB_POSTING_APPLICATION_END.plusMonths(1);
    private final LocalDate ANOTHER_JOB_POSTING_JOB_START = ANOTHER_JOB_POSTING_APPLICATION_END.plusMonths(3);

    // Set up addresses
    private final String ADDRESS_STREET = "123 Main St";
    private final String ANOTHER_ADDRESS_STREET = "456 Elm St";
    private final String ADDRESS_COMPLEMENT = "Suite 4B";
    private final String ANOTHER_ADDRESS_COMPLEMENT = "Apt 2C";
    private final String ADDRESS_CITY = "Springfield";
    private final String ANOTHER_ADDRESS_CITY = "Chicago";
    private final String ADDRESS_STATE = "IL";
    private final String ANOTHER_ADDRESS_STATE = "IL";
    private final String ADDRESS_ZIP_CODE = "62704";
    private final String ANOTHER_ADDRESS_ZIP_CODE = "60616";

    @BeforeAll
    public void setUp() throws BadRequestException {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

        // Create test address
        Address address = Address.builder()
                .street(ADDRESS_STREET)
                .complement(ADDRESS_COMPLEMENT)
                .city(ADDRESS_CITY)
                .state(ADDRESS_STATE)
                .zipCode(ADDRESS_ZIP_CODE)
                .build();
        address = addressService.createAddress(address);
        addressId = address.getAddressId();

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
        student.setAddress(address);
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

        // Create test job posting (open)
        JobPostingDto jobPostingDto = jobPostingService.createJobPosting(
                JobPostingDto.builder()
                        .title(JOB_POSTING_TITLE)
                        .description(JOB_POSTING_DESCRIPTION)
                        .summary(JOB_POSTING_SUMMARY)
                        .salary(SMALL_SALARY)
                        .jobType(JobType.POST_GRADUATION_JOB)
                        .minimumGpa(SMALL_GPA)
                        .applicationStart(JOB_POSTING_APPLICATION_START)
                        .applicationEnd(JOB_POSTING_APPLICATION_END)
                        .jobStart(JOB_POSTING_JOB_START)
                        .employerId(employerId)
                        .build()
        );
        jobPostingId = jobPostingDto.getJobPostingId();

        // Create test job posting (opens in the future)
        JobPostingDto anotherJobPostingDto = jobPostingService.createJobPosting(
                JobPostingDto.builder()
                        .title(ANOTHER_JOB_POSTING_TITLE)
                        .description(ANOTHER_JOB_POSTING_DESCRIPTION)
                        .summary(ANOTHER_JOB_POSTING_SUMMARY)
                        .salary(LARGE_SALARY)
                        .jobType(JobType.INTERNSHIP)
                        .minimumGpa(LARGE_GPA)
                        .applicationStart(ANOTHER_JOB_POSTING_APPLICATION_START)
                        .applicationEnd(ANOTHER_JOB_POSTING_APPLICATION_END)
                        .jobStart(ANOTHER_JOB_POSTING_JOB_START)
                        .employerId(employerId)
                        .build()
        );
        anotherJobPostingId = anotherJobPostingDto.getJobPostingId();

        // Create a third open job posting for search
        jobPostingDto.setJobPostingId(null);
        jobPostingDto.setTitle(JOB_POSTING_TITLE + "2");
        jobPostingService.createJobPosting(jobPostingDto);
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
    void testSearch() {
        // Test case 1a: Search by keyword, one result
        assertSearch(ANOTHER_JOB_POSTING_DESCRIPTION, null, null, null, null, null, null, null,200, 1);

        // Test case 1b: Search by keyword, two results
        assertSearch(JOB_POSTING_DESCRIPTION, null, null, null, null, null, null, null, 200, 3);

        // Test case 2: Filter by minimum salary
        assertSearch(null, SMALL_SALARY.add(BigDecimal.valueOf(1)), null, null, null, null, null, null, 200, 1);

        // Test case 3: Filter by maximum salary
        assertSearch(null, null, LARGE_SALARY.subtract(BigDecimal.valueOf(1)), null, null, null, null, null, 200, 2);

        // Test case 4: Filter by job start date range
        assertSearch(null, null, null, JOB_POSTING_JOB_START, ANOTHER_JOB_POSTING_JOB_START.minusDays(1), null, null, null, 200, 2);

        // Test case 5: Filter by open applications
        assertSearch(null, null, null, null, null, true, null, null, 200, 2);

        // Test case 6: Filter by is_qualified
        assertSearch(null, null, null, null, null, true, true, null, 200, 2);

        // Test case 7: Filter by has not applied to
        assertSearch(null, null, null, null, null, true, null, true, 200, 2);

        // Test case 7: No results for unmatched filter
        assertSearch(null, LARGE_SALARY.add(BigDecimal.valueOf(1)), null, null, null, null, null, null, 200, 0);
    }

    private void assertSearch(String q, BigDecimal minSalary, BigDecimal maxSalary, LocalDate minJobStart, LocalDate maxJobStart, Boolean isApplicationOpen, Boolean isQualified, Boolean hasNotAppliedTo, int expectedStatus, int expectedResults) {
        Response response = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .queryParam("q", q)
                .queryParam("minSalary", minSalary)
                .queryParam("maxSalary", maxSalary)
                .queryParam("minJobStart", minJobStart != null ? minJobStart.toString() : null)
                .queryParam("maxJobStart", maxJobStart != null ? maxJobStart.toString() : null)
                .queryParam("isApplicationOpen", isApplicationOpen)
                .queryParam("isQualified", isQualified)
                .when()
                .get("/job-posting/search");

        assertEquals(expectedStatus, response.getStatusCode());
        assertEquals(expectedResults, response.jsonPath().getList("$").size(), "Expected number of results does not match");
    }

    @Test
    void testAddressCRUDL() {
        // Test POST: Create a second address
        Address anotherAddressRequest = Address.builder()
                .street(ANOTHER_ADDRESS_STREET)
                .complement(ANOTHER_ADDRESS_COMPLEMENT)
                .city(ANOTHER_ADDRESS_CITY)
                .state(ANOTHER_ADDRESS_STATE)
                .zipCode(ANOTHER_ADDRESS_ZIP_CODE)
                .build();
        Response postResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .header("Content-Type", "application/json")
                .body(anotherAddressRequest)
                .post("/address");
        assertEquals(200, postResponse.getStatusCode(), "POST request should succeed");
        Address anotherAddress = postResponse.as(Address.class);
        assertNotNull(anotherAddress.getAddressId(), "Second address ID should not be null");

        // Test GET by ID: Verify the initial address
        Response getByIdResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .get("/address/" + addressId);
        assertEquals(200, getByIdResponse.getStatusCode(), "GET by ID should succeed");
        Address fetchedAddress = getByIdResponse.as(Address.class);
        assertEquals(ADDRESS_STREET, fetchedAddress.getStreet(), "Street should match the initial address");

        // Test GET all: Verify both addresses are listed
        Response getAllResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .get("/address");
        assertEquals(200, getAllResponse.getStatusCode(), "GET all should succeed");
        List<Address> addresses = getAllResponse.jsonPath().getList("$", Address.class);
        assertEquals(2, addresses.size(), "There should be two addresses");

        // Test PUT: Update the second address
        anotherAddress.setStreet(ADDRESS_STREET);
        anotherAddress.setComplement(ADDRESS_COMPLEMENT);
        Response putResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .header("Content-Type", "application/json")
                .body(anotherAddress)
                .put("/address");
        assertEquals(200, putResponse.getStatusCode(), "PUT request should succeed");
        Address updatedAddress = putResponse.as(Address.class);
        assertEquals(ADDRESS_STREET, updatedAddress.getStreet(), "Street should be updated");
        assertEquals(ADDRESS_COMPLEMENT, updatedAddress.getComplement(), "Complement should be updated");

        // Test DELETE: Delete the second address
        Response deleteResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .delete("/address/" + anotherAddress.getAddressId());
        assertEquals(200, deleteResponse.getStatusCode(), "DELETE request should succeed");
        assertTrue(deleteResponse.asString().contains("Address successfully deleted"), "Deletion confirmation message should be returned");
        Response verifyDeleteResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .get("/address/" + anotherAddress.getAddressId());
        assertEquals(404, verifyDeleteResponse.getStatusCode(), "Deleted address should not be found");
    }

    @Test
    void testChat() {
        // Step 1: Apply for a job posting
        Response applyResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .header("Content-Type", "application/json")
                .body(ApplicationRequest.builder()
                        .studentId(studentId)
                        .jobPostingId(jobPostingId)
                        .build())
                .post("/applications/apply");
        assertEquals(200, applyResponse.getStatusCode(), "POST request should succeed");

        // Step 2: Retrieve applications for the student
        Response getApplicationsResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .queryParam("studentId", studentId)
                .get("/applications/student");
        assertEquals(200, getApplicationsResponse.getStatusCode(), "GET request should succeed");
        List<?> applications = getApplicationsResponse.jsonPath().getList("$");
        assertEquals(1, applications.size(), "There should be exactly one application in the response");

        // Step 3: Accept the application
        Long applicationId = getApplicationsResponse.jsonPath().getLong("[0].applicationId");
        Response acceptApplicationResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .post("/applications/" + applicationId + "/accept");
        assertEquals(200, acceptApplicationResponse.getStatusCode(), "POST request should succeed for accepting application");

        // Step 4: Verify the chat is created for the student
        Response getChatResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .queryParam("userId", studentId)
                .get("/chat");
        assertEquals(200, getChatResponse.getStatusCode(), "GET request should succeed for retrieving chats");
        List<?> chats = getChatResponse.jsonPath().getList("$");
        assertEquals(1, chats.size(), "There should be exactly one chat for the student");
        Long chatUserId = getChatResponse.jsonPath().getLong("[0].studentId");
        assertEquals(studentId, chatUserId, "Chat should belong to the correct student");
    }

    @Test
    void testSwaggerUIEndpoint() {
        Response response = RestAssured.given()
                .when()
                .get("/swagger-ui.html");

        assertEquals(200, response.getStatusCode(), "Swagger UI should be accessible");
        assertTrue(response.asString().contains("Swagger UI"), "Swagger UI page should contain 'Swagger UI'");
    }

    @Test
    void testJobPostingEndpoints() {
        // Create another test job posting (in the future)
        JobPostingDto anotherJobPostingDto = JobPostingDto.builder()
                .title(ANOTHER_JOB_POSTING_TITLE)
                .description(ANOTHER_JOB_POSTING_DESCRIPTION)
                .summary(ANOTHER_JOB_POSTING_SUMMARY)
                .salary(LARGE_SALARY)
                .jobType(JobType.INTERNSHIP)
                .minimumGpa(LARGE_GPA)
                .applicationStart(ANOTHER_JOB_POSTING_APPLICATION_START)
                .applicationEnd(ANOTHER_JOB_POSTING_APPLICATION_END)
                .jobStart(ANOTHER_JOB_POSTING_JOB_START)
                .employerId(employerId)
        .build();

        // Step 1: Create a job posting
        Response createResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .contentType("application/json")
                .body(anotherJobPostingDto)
                .post("/job-posting");
        assertEquals(200, createResponse.getStatusCode(), "Create Job Posting should succeed");
        JobPostingDto createdJobPosting = createResponse.as(JobPostingDto.class);
        assertNotNull(createdJobPosting.getJobPostingId(), "Created Job Posting should have an ID");

        // Step 2a: Get job postings (all)
        Response getAllResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .get("/job-posting");
        assertEquals(200, getAllResponse.getStatusCode(), "Get all Job Postings should succeed");
        List<JobPostingDto> allJobPostings = getAllResponse.jsonPath().getList("$", JobPostingDto.class);
        assertTrue(allJobPostings.size() >= 2, "At least two job postings should be returned");

        // Step 2b: Get job postings (filtered by employer ID)
        Response getByEmployerResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .queryParam("employerId", employerId)
                .get("/job-posting");
        assertEquals(200, getByEmployerResponse.getStatusCode(), "Get Job Postings by Employer ID should succeed");
        List<JobPostingDto> employerJobPostings = getByEmployerResponse.jsonPath().getList("$", JobPostingDto.class);
        assertTrue(allJobPostings.size() >= 2, "At least two job postings should be returned");

        // Step 2: Get the created job posting by ID
        Long jobPostingId = createdJobPosting.getJobPostingId();
        Response getResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .get("/job-posting/" + jobPostingId);
        assertEquals(200, getResponse.getStatusCode(), "Get Job Posting by ID should succeed");
        JobPostingDto fetchedJobPosting = getResponse.as(JobPostingDto.class);
        assertEquals(ANOTHER_JOB_POSTING_TITLE, fetchedJobPosting.getTitle(), "Fetched job title should match");

        // Step 3: Update the job posting
        String updatedTitle = "Senior Software Engineer";
        createdJobPosting.setTitle(updatedTitle);
        Response updateResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .contentType("application/json")
                .body(createdJobPosting)
                .put("/job-posting");
        assertEquals(200, updateResponse.getStatusCode(), "Update Job Posting should succeed");
        JobPostingDto updatedJobPosting = updateResponse.as(JobPostingDto.class);
        assertEquals(updatedTitle, updatedJobPosting.getTitle(), "Updated title should match");

        // Step 4: Get job postings for a student
        Response getForStudentResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .get("/job-posting/student/" + studentId);
        assertEquals(200, getForStudentResponse.getStatusCode(), "Get Job Postings for Student should succeed");
        List<JobPostingDto> studentJobPostings = getForStudentResponse.jsonPath().getList("$", JobPostingDto.class);
        assertFalse(studentJobPostings.isEmpty(), "Student should have job postings available");

        // Step 5: Get job posting suggestions for a student
        Response suggestionsResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .get("/job-posting/suggestions/" + studentId);
        assertEquals(200, suggestionsResponse.getStatusCode(), "Get Job Posting Suggestions should succeed");

        // Step 6: Delete the job posting
        Response deleteResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .delete("/job-posting/" + jobPostingId);
        assertEquals(200, deleteResponse.getStatusCode(), "Delete Job Posting should succeed");
        assertTrue(deleteResponse.asString().contains("successfully deleted"), "Delete confirmation message should be returned");

        // Step 7: Verify deletion
        Response verifyDeleteResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .get("/job-posting/" + jobPostingId);
        assertEquals(404, verifyDeleteResponse.getStatusCode(), "Deleted job posting should not be found");
    }

    @Test
    void testChatControllerEndpoints() {
        // Step 1: Create a chat for testing
        Chat chat = new Chat();
        chat.setStudentId(studentId);
        chat.setEmployerId(employerId);
        Chat createdChat = chatService.createChat(chat);
        assertNotNull(createdChat.getChatId(), "Created Chat should have an ID");

        // Step 2: Get all chats
        Response getAllChatsResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .get("/chat");
        assertEquals(200, getAllChatsResponse.getStatusCode(), "GET request for all chats should succeed");
        List<Chat> allChats = getAllChatsResponse.jsonPath().getList("$", Chat.class);
        assertFalse(allChats.isEmpty(), "There should be at least one chat");

        // Step 3: Get chats filtered by user ID (student)
        Response getChatsByUserIdResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .queryParam("userId", studentId)
                .get("/chat");
        assertEquals(200, getChatsByUserIdResponse.getStatusCode(), "GET request for chats filtered by user ID should succeed");
        List<Chat> userChats = getChatsByUserIdResponse.jsonPath().getList("$", Chat.class);
        assertEquals(studentId, userChats.get(0).getStudentId(), "The chat should belong to the correct student");

        // Step 4: Get chat by ID
        Long chatId = createdChat.getChatId();
        Response getChatByIdResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .get("/chat/" + chatId);
        assertEquals(200, getChatByIdResponse.getStatusCode(), "GET request for chat by ID should succeed");
        Chat fetchedChat = getChatByIdResponse.as(Chat.class);
        assertEquals(chatId, fetchedChat.getChatId(), "The fetched chat ID should match the created chat");

        // Step 5: Update the chat
        fetchedChat.setJobPostingId(anotherJobPostingId);
        Response updateChatResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .contentType("application/json")
                .body(fetchedChat)
                .put("/chat");
        assertEquals(200, updateChatResponse.getStatusCode(), "PUT request for updating chat should succeed");
        Chat updatedChat = updateChatResponse.as(Chat.class);
        assertEquals(anotherJobPostingId, updatedChat.getJobPostingId(), "The chat jobPostingId should be updated");

        // Step 6: Delete the chat
        Response deleteChatResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .delete("/chat/" + chatId);
        assertEquals(200, deleteChatResponse.getStatusCode(), "DELETE request for chat should succeed");
        assertTrue(deleteChatResponse.asString().contains("successfully deleted"), "Delete confirmation message should be returned");

        // Step 7: Verify the chat has been deleted
        Response verifyDeleteResponse = RestAssured.given()
                .auth()
                .basic(STUDENT_EMAIL, STUDENT_PASSWORD)
                .get("/chat/" + chatId);
        assertEquals(404, verifyDeleteResponse.getStatusCode(), "Deleted chat should not be found");
    }

    @Test
    void testChatMessageService() {
        // Step 1: Create a chat for testing
        Chat chat = new Chat();
        chat.setStudentId(studentId);
        chat.setEmployerId(employerId);
        Chat createdChat = chatService.createChat(chat);
        Long chatId = createdChat.getChatId();
        assertNotNull(chatId, "Created Chat should have an ID");

        // Step 2: Create a chat message
        ChatMessageDto messageDto = ChatMessageDto.builder()
                .chatId(chatId)
                .userId(studentId)
                .message("Hello, this is a test message!")
                .build();
        ChatMessageDto createdMessage = chatMessageService.createChatMessage(messageDto);
        assertNotNull(createdMessage.getMessageId(), "Created message should have an ID");
        assertEquals("Hello, this is a test message!", createdMessage.getMessage(), "Message content should match");

        // Step 3: Retrieve chat message by ID
        ChatMessageDto fetchedMessage = chatMessageService.getChatMessageById(createdMessage.getMessageId());
        assertEquals(createdMessage.getMessageId(), fetchedMessage.getMessageId(), "Fetched message ID should match created message");

        // Step 4: Retrieve all chat messages
        List<ChatMessageDto> allMessages = chatMessageService.getChatMessages();
        assertFalse(allMessages.isEmpty(), "There should be at least one chat message");

        // Step 5: Retrieve chat messages by chat ID
        List<ChatMessageDto> chatMessages = chatMessageService.getChatMessagesByChatId(chatId);
        assertEquals(1, chatMessages.size(), "There should be exactly one message in the chat");
        assertEquals("Hello, this is a test message!", chatMessages.get(0).getMessage(), "Message content should match");

        // Step 6: Mark messages as seen
        chatMessageService.markMessagesAsSeen(chatId, employerId);
        List<ChatMessageDto> unreadMessages = chatMessageService.getUnreadChatMessagesByUserId(employerId);
        assertEquals(0, unreadMessages.size(), "All messages should be marked as seen");

        // Step 7: Update the chat message
        createdMessage.setMessage("Updated content");
        ChatMessageDto updatedMessage = chatMessageService.updateChatMessage(createdMessage);
        assertEquals("Updated content", updatedMessage.getMessage(), "Message content should be updated");

        // Step 8: Delete the chat message
        chatMessageService.deleteChatMessage(createdMessage.getMessageId());
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> chatMessageService.getChatMessageById(createdMessage.getMessageId()),
                "Fetching deleted message should throw ResourceNotFoundException"
        );
        assertTrue(exception.getMessage().contains("Chat Message not found with id"), "Exception message should indicate message not found");

        // Step 9: Cleanup - Delete the chat
        chatService.deleteChat(chatId);
        ResourceNotFoundException chatException = assertThrows(
                ResourceNotFoundException.class,
                () -> chatService.getChatById(chatId),
                "Fetching deleted chat should throw ResourceNotFoundException"
        );
        assertTrue(chatException.getMessage().contains("Chat not found with id"), "Exception message should indicate chat not found");
    }
}
