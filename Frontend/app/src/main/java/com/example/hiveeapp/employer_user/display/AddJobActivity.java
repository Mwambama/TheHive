package com.example.hiveeapp.employer_user.display;


import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.VolleyError;
import com.example.hiveeapp.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

/**
 * AddJobActivity allows the user to add a new job posting by filling out a form.
 * The form includes fields like job title, description, requirements, etc., which are validated before submission.
 */
public class AddJobActivity extends AppCompatActivity {

    private EditText jobTitleField, jobDescriptionField, summaryField, jobTypeField, salaryRequirementsField,
            minimumGpaField, jobStartField, applicationStartField, applicationEndField;
    private Button postJobButton;
    private ImageButton backArrowIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job); // Ensure this matches your XML layout file name

        // Initialize the input fields and buttons
        initViews();

        // Set up back arrow functionality to close the current activity and return to the previous one
        backArrowIcon.setOnClickListener(v -> finish());

        // Handle the "Post Job" button click and validate inputs
        postJobButton.setOnClickListener(v -> {
            if (validateInput()) {
                addJobs();  // If inputs are valid, proceed to add the job
            }
        });
    }

    /**
     * Initializes the input fields and buttons in the activity layout.
     */
    private void initViews() {
        jobTitleField = findViewById(R.id.jobTitleField); // Ensure these IDs match your layout
        jobDescriptionField = findViewById(R.id.jobDescriptionField);
        summaryField = findViewById(R.id.summaryField);
        jobTypeField = findViewById(R.id.jobTypeField);
        salaryRequirementsField = findViewById(R.id.salaryRequirementsField);
        minimumGpaField = findViewById(R.id.minimumGpaField);
        jobStartField = findViewById(R.id.jobStartDateField);
        applicationStartField = findViewById(R.id.applicationStartDateField);
        applicationEndField = findViewById(R.id.applicationEndDateField);
        postJobButton = findViewById(R.id.addJobButton);
        backArrowIcon = findViewById(R.id.backArrowIcon);
    }

    /**
     * Validates the input fields to ensure that all required fields are filled and valid.
     *
     * @return true if all fields are valid, false otherwise
     */
    private boolean validateInput() {
        boolean isValid = true;

        // Reset any previous error messages
        jobTitleField.setError(null);
        jobDescriptionField.setError(null);
        summaryField.setError(null);
        jobTypeField.setError(null);
        salaryRequirementsField.setError(null);
        minimumGpaField.setError(null);
        jobStartField.setError(null);
        applicationStartField.setError(null);
        applicationEndField.setError(null);

        // Get the input values from the fields
        String jobTitle = jobTitleField.getText().toString().trim();
        String jobDescription = jobDescriptionField.getText().toString().trim();
        String summary = summaryField.getText().toString().trim();
        String jobType = jobTypeField.getText().toString().trim();
        String salaryRequirements = salaryRequirementsField.getText().toString().trim();
        String minimumGpa = minimumGpaField.getText().toString().trim();
        String jobStart = jobStartField.getText().toString().trim();
        String applicationStart = applicationStartField.getText().toString().trim();
        String applicationEnd = applicationEndField.getText().toString().trim();

        // Validate the job title field
        if (jobTitle.isEmpty()) {
            jobTitleField.setError("Job title is required");
            isValid = false;
        }

        // Validate the job description field
        if (jobDescription.isEmpty()) {
            jobDescriptionField.setError("Job description is required");
            isValid = false;
        }

        // Validate the summary field
        if (summary.isEmpty()) {
            summaryField.setError("Summary is required");
            isValid = false;
        }

        // Validate the job type field
        if (jobType.isEmpty()) {
            jobTypeField.setError("Job type is required");
            isValid = false;
        }

        // Validate the salary requirements field
        if (salaryRequirements.isEmpty()) {
            salaryRequirementsField.setError("Salary requirements are required");
            isValid = false;
        }

        // Validate the minimum GPA field
        if (minimumGpa.isEmpty()) {
            minimumGpaField.setError("Minimum GPA is required");
            isValid = false;
        }

        // Validate the job start date field
        if (jobStart.isEmpty()) {
            jobStartField.setError("Job start date is required");
            isValid = false;
        }

        // Validate the application start date field
        if (applicationStart.isEmpty()) {
            applicationStartField.setError("Application start date is required");
            isValid = false;
        }

        // Validate the application end date field
        if (applicationEnd.isEmpty()) {
            applicationEndField.setError("Application end date is required");
            isValid = false;
        }

        return isValid;
    }

    /**
     * Constructs a JSON object with job details and sends a request to add a new job posting.
     */
    private void addJobs() {
        // Get the values from the input fields
        String jobTitle = jobTitleField.getText().toString().trim();
        String jobDescription = jobDescriptionField.getText().toString().trim();
        String summary = summaryField.getText().toString().trim();
        String jobType = jobTypeField.getText().toString().trim();
        String salaryRequirements = salaryRequirementsField.getText().toString().trim();
        String minimumGpa = minimumGpaField.getText().toString().trim();
        String jobStart = jobStartField.getText().toString().trim();
        String applicationStart = applicationStartField.getText().toString().trim();
        String applicationEnd = applicationEndField.getText().toString().trim();

        // Construct the job posting JSON object
        JSONObject jobData = new JSONObject();
        try {
           // jobData.put("jobPostingId", 625); // Hardcoded for now; adjust as necessary
            jobData.put("title", jobTitle);
            jobData.put("description", jobDescription);
            jobData.put("summary", summary);
            jobData.put("salary", Integer.parseInt(salaryRequirements)); // Assuming salary is an integer
            jobData.put("jobType", jobType); // Ensure jobType matches your enum or backend requirement
            jobData.put("minimumGpa", Double.parseDouble(minimumGpa)); // Assuming GPA is a double
            jobData.put("jobStart", jobStart);
            jobData.put("applicationStart", applicationStart);
            jobData.put("applicationEnd", applicationEnd);

            // Add employerId logic (replace with actual logic to retrieve employerId)
            int employerId = 373; // Hardcoded for now; replace with your logic
            jobData.put("employerId", employerId);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating job data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send the job data to the server using the EmployerApis
        EmployerApis.addJob(this, jobData,
                response -> {
                    // Handle successful response
                    Toast.makeText(AddJobActivity.this, "Job posted successfully!", Toast.LENGTH_SHORT).show();
                    finish();  // Close the activity and return to the previous screen
                },
                error -> {
                    // Handle error response and display user-friendly message
                    String errorMessage = getErrorMessage(error);
                    Toast.makeText(AddJobActivity.this, "Error posting job: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Extracts and returns a meaningful error message from a VolleyError.
     *
     * @param error The VolleyError object containing the error details
     * @return A user-friendly error message
     */
    private String getErrorMessage(VolleyError error) {
        String errorMsg = "An unexpected error occurred";
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String errorData = new String(error.networkResponse.data, "UTF-8");

                // Attempt to parse errorData as JSON
                try {
                    JSONObject jsonError = new JSONObject(errorData);
                    errorMsg = jsonError.optString("message", errorMsg);
                } catch (JSONException jsonException) {
                    // If parsing fails, use the raw errorData
                    errorMsg = errorData;
                }

            } catch (UnsupportedEncodingException e) {
                errorMsg = error.getMessage();
            }
        }
        return errorMsg;
    }
}










//import android.os.Bundle;
//import android.util.Patterns;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.volley.VolleyError;
//import com.example.hiveeapp.R;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import java.io.UnsupportedEncodingException;
//
///**
// * AddJobActivity allows the user to add a new employer by filling out a form.
// * The form includes fields like name, email, phone number, address, etc., which are validated before submission.
// */
//public class AddJobActivity extends AppCompatActivity {
//
//    private EditText nameField, emailField, phoneField, streetField, complementField, cityField, stateField, zipField;
//    private Button addEmployerButton;
//    private ImageButton backArrowIcon;
//
//    // Constants for validation
//    private static final int MAX_PHONE_LENGTH = 15;  // Adjust based on your DB schema
//    private static final int MIN_PHONE_LENGTH = 7;   // Minimum acceptable phone length
//    private static final int ZIP_CODE_LENGTH = 5;    // Standard US ZIP code length
//    private static final String USER_PREFS = "MyAppPrefs"; // Shared preferences key
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_job);
//
//        // Initialize the input fields and buttons
//        initViews();
//
//        // Set up back arrow functionality to close the current activity and return to the previous one
//        backArrowIcon.setOnClickListener(v -> finish());
//
//        // Handle the "Add Employer" button click and validate inputs
//        addEmployerButton.setOnClickListener(v -> {
//            if (validateInput()) {
//                addJobs();  // If inputs are valid, proceed to add the employer
//            }
//        });
//    }
//
//    /**
//     * Initializes the input fields and buttons in the activity layout.
//     */
//    private void initViews() {
//        nameField = findViewById(R.id.nameField);
//        emailField = findViewById(R.id.emailField);
//        phoneField = findViewById(R.id.phoneField);
//        streetField = findViewById(R.id.streetField);
//        complementField = findViewById(R.id.complementField);
//        cityField = findViewById(R.id.cityField);
//        stateField = findViewById(R.id.stateField);
//        zipField = findViewById(R.id.zipField);
//        addEmployerButton = findViewById(R.id.addEmployerButton);
//        backArrowIcon = findViewById(R.id.backArrowIcon);
//    }
//
//    /**
//     * Validates the input fields to ensure that all required fields are filled and valid.
//     *
//     * @return true if all fields are valid, false otherwise
//     */
//    private boolean validateInput() {
//        boolean isValid = true;
//
//        // Reset any previous error messages
//        nameField.setError(null);
//        emailField.setError(null);
//        phoneField.setError(null);
//        streetField.setError(null);
//        cityField.setError(null);
//        stateField.setError(null);
//        zipField.setError(null);
//
//        // Get the input values from the fields
//        String name = nameField.getText().toString().trim();
//        String email = emailField.getText().toString().trim();
//        String phone = phoneField.getText().toString().trim();
//        String street = streetField.getText().toString().trim();
//        String city = cityField.getText().toString().trim();
//        String state = stateField.getText().toString().trim();
//        String zip = zipField.getText().toString().trim();
//
//        // Validate the name field
//        if (name.isEmpty()) {
//            nameField.setError("Name is required");
//            isValid = false;
//        }
//
//        // Validate the email field using Patterns.EMAIL_ADDRESS
//        if (email.isEmpty()) {
//            emailField.setError("Email is required");
//            isValid = false;
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            emailField.setError("Invalid email format");
//            isValid = false;
//        }
//
//        // Validate the phone field
//        if (phone.isEmpty()) {
//            phoneField.setError("Phone number is required");
//            isValid = false;
//        } else if (phone.length() > MAX_PHONE_LENGTH || phone.length() < MIN_PHONE_LENGTH) {
//            phoneField.setError("Phone number must be between " + MIN_PHONE_LENGTH + " and " + MAX_PHONE_LENGTH + " digits");
//            isValid = false;
//        } else if (!phone.matches("\\d+")) {
//            phoneField.setError("Phone number must contain only digits");
//            isValid = false;
//        }
//
//        // Validate the address fields
//        if (street.isEmpty()) {
//            streetField.setError("Street address is required");
//            isValid = false;
//        }
//
//        if (city.isEmpty()) {
//            cityField.setError("City is required");
//            isValid = false;
//        }
//
//        if (state.isEmpty()) {
//            stateField.setError("State is required");
//            isValid = false;
//        }
//
//        // Validate the ZIP code format (assuming US ZIP code)
//        if (zip.isEmpty()) {
//            zipField.setError("Zip code is required");
//            isValid = false;
//        } else if (zip.length() != ZIP_CODE_LENGTH || !zip.matches("\\d{" + ZIP_CODE_LENGTH + "}")) {
//            zipField.setError("Zip code must be " + ZIP_CODE_LENGTH + " digits");
//            isValid = false;
//        }
//
//        return isValid;
//    }
//
//    /**
//     * Constructs a JSON object with employer details and sends a request to add a new employer.
//     */
//    private void addJobs() {
//        // Get the values from the input fields
//        String name = nameField.getText().toString().trim();
//        String email = emailField.getText().toString().trim();
//        String phone = phoneField.getText().toString().trim();
//        String street = streetField.getText().toString().trim();
//        String complement = complementField.getText().toString().trim();
//        String city = cityField.getText().toString().trim();
//        String state = stateField.getText().toString().trim();
//        String zipCode = zipField.getText().toString().trim();
//
//        // Retrieve the companyId from SharedPreferences (or use a hardcoded value for testing)
//        int companyId = 611;  // This is a test company ID
//
//        // Construct the employer JSON object
//        JSONObject employerData = new JSONObject();
//        try {
//            employerData.put("name", name);
//            employerData.put("email", email);
//            employerData.put("phone", phone);
//            employerData.put("role", "EMPLOYER");
//            employerData.put("companyId", companyId);
//            employerData.put("field", JSONObject.NULL);
//            employerData.put("jobPostings", new JSONArray());
//
//            // Construct the address JSON object
//            JSONObject address = new JSONObject();
//            address.put("addressId", JSONObject.NULL);
//            address.put("street", street);
//            address.put("complement", complement.isEmpty() ? JSONObject.NULL : complement);
//            address.put("city", city);
//            address.put("state", state);
//            address.put("zipCode", zipCode);
//
//            employerData.put("address", address);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Error creating Job data", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Send the employer data to the server using the EmployerApis
//        EmployerApis.addEmployerWithAddress(this, employerData,
//                response -> {
//                    // Handle successful response
//                    Toast.makeText(AddJobActivity.this, "Job added successfully!", Toast.LENGTH_SHORT).show();
//                    finish();  // Close the activity and return to the previous screen
//                },
//                error -> {
//                    // Handle error response and display user-friendly message
//                    String errorMessage = getErrorMessage(error);
//                    Toast.makeText(AddJobActivity.this, "Error adding Job: " + errorMessage, Toast.LENGTH_SHORT).show();
//                });
//    }
//
//    /**
//     * Extracts and returns a meaningful error message from a VolleyError.
//     *
//     * @param error The VolleyError object containing the error details
//     * @return A user-friendly error message
//     */
//    private String getErrorMessage(VolleyError error) {
//        String errorMsg = "An unexpected error occurred";
//        if (error.networkResponse != null && error.networkResponse.data != null) {
//            try {
//                String errorData = new String(error.networkResponse.data, "UTF-8");
//
//                // Attempt to parse errorData as JSON
//                try {
//                    JSONObject jsonError = new JSONObject(errorData);
//                    errorMsg = jsonError.optString("message", errorMsg);
//                } catch (JSONException jsonException) {
//                    // If parsing fails, use the raw errorData
//                    errorMsg = errorData;
//                }
//
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//                errorMsg = "Error parsing error message";
//            }
//        } else if (error.getMessage() != null) {
//            errorMsg = error.getMessage();
//        }
//        return errorMsg;
//    }
//}












//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.example.hiveeapp.R;
//import com.example.hiveeapp.volley.VolleySingleton;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class AddJobActivity extends AppCompatActivity {
//    private EditText jobTitleEditText;
//    private EditText jobDescriptionEditText;
//    private EditText summaryEditText;
//    private EditText jobTypeEditText;
//    private EditText salaryRequirementsEditText;
//    private EditText ageRequirementEditText;
//    private EditText minimumGpaEditText;
//    private EditText jobStartEditText;
//    private EditText applicationStartEditText;
//    private EditText applicationEndEditText;
//    private Button postJobButton;
//    private Button backButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_job);
//
//        // Initialize EditText fields
//        jobTitleEditText = findViewById(R.id.job_title);
//        jobDescriptionEditText = findViewById(R.id.job_description);
//        summaryEditText = findViewById(R.id.summary);
//        jobTypeEditText = findViewById(R.id.job_type);
//        salaryRequirementsEditText = findViewById(R.id.salary_requirements);
//        ageRequirementEditText = findViewById(R.id.age_requirement);
//        minimumGpaEditText = findViewById(R.id.minimum_gpa);
//        jobStartEditText = findViewById(R.id.job_start);
//        applicationStartEditText = findViewById(R.id.application_start);
//        applicationEndEditText = findViewById(R.id.application_end);
//        postJobButton = findViewById(R.id.post_job_button);
//        backButton = findViewById(R.id.back_button);
//
//        // Post Job button click listener
//        postJobButton.setOnClickListener(view -> {
//            String jobTitle = jobTitleEditText.getText().toString();
//            String jobDescription = jobDescriptionEditText.getText().toString();
//            String summary = summaryEditText.getText().toString();
//            String jobType = jobTypeEditText.getText().toString();
//            String salaryRequirements = salaryRequirementsEditText.getText().toString();
//            String ageRequirement = ageRequirementEditText.getText().toString();
//            String minimumGpa = minimumGpaEditText.getText().toString();
//            String jobStart = jobStartEditText.getText().toString();
//            String applicationStart = applicationStartEditText.getText().toString();
//            String applicationEnd = applicationEndEditText.getText().toString();
//
//            if (jobTitle.isEmpty() || jobDescription.isEmpty() || summary.isEmpty() || jobType.isEmpty() ||
//                    salaryRequirements.isEmpty() || ageRequirement.isEmpty() || minimumGpa.isEmpty() ||
//                    jobStart.isEmpty() || applicationStart.isEmpty() || applicationEnd.isEmpty()) {
//                Toast.makeText(AddJobActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Create JSON object with job data
//            JSONObject jobData = new JSONObject();
//            try {
//                jobData.put("title", jobTitle);
//                jobData.put("description", jobDescription);
//                jobData.put("summary", summary);
//                jobData.put("jobType", jobType);
//                jobData.put("salary", salaryRequirements);
//                jobData.put("ageRequirement", ageRequirement);
//                jobData.put("minimumGpa", minimumGpa);
//                jobData.put("jobStart", jobStart);
//                jobData.put("applicationStart", applicationStart);
//                jobData.put("applicationEnd", applicationEnd);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            // URL for the Postman mock server
//            String url = "http://coms-3090-063.class.las.iastate.edu:8080/job-posting"; // Replace with your mock server URL
//
//            // Create JSON request
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                    Request.Method.POST,
//                    url,
//                    jobData,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            // Handle successful job post
//                            Toast.makeText(AddJobActivity.this, "Job posted successfully", Toast.LENGTH_SHORT).show();
//
//                            // Extract jobId from the response if available
//                            String jobId;
//                            try {
//                                jobId = response.getString("jobId"); // Adjust based on your server response
//                            } catch (JSONException e) {
//                                jobId = "unknown"; // Default value if not present
//                            }
//
//                            // Create a new PostedJobs object from the input fields
//                            PostedJobs newJob = new PostedJobs(
//                                    jobId, // Pass the jobId here
//                                    jobTitle,
//                                    jobDescription,
//                                    jobType,
//                                    salaryRequirements,
//                                    ageRequirement,
//                                    minimumGpa
//                            );
//
//                            // Create an Intent to return the new job to the CreateJobsActivity
//                            Intent resultIntent = new Intent();
//                            resultIntent.putExtra("newJob", newJob); // Ensure PostedJobs implements Serializable
//                            setResult(RESULT_OK, resultIntent);
//
//                            finish(); // Close AddJobActivity and go back to CreateJobsActivity
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            // Handle error
//                            Toast.makeText(AddJobActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//            );
//
//            // Add request to the Volley request queue
//            VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
//        });
//
//        // Back button click listener
//        backButton.setOnClickListener(view -> finish());
//    }
//}
