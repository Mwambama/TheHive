package com.example.hiveeapp.employer_user.display;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.util.Arrays;
import java.util.List;

/**
 * AddJobActivity allows the user to add a new job posting by filling out a form.
 * The form includes fields like job title, description, requirements, etc., which are validated before submission.
 */
public class AddJobActivity extends AppCompatActivity {

    private EditText jobTitleField, jobDescriptionField, summaryField, jobTypeField, salaryRequirementsField,
            minimumGpaField, jobStartField, applicationStartField, applicationEndField;
    private Button postJobButton;
    private ImageButton backArrowIcon;

    private static final String USER_PREFS = "UserPreferences"; // SharedPreferences name

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
        String jobType = jobTypeField.getText().toString().trim().toUpperCase(); // Convert to uppercase to match backend enums
        String salaryRequirements = salaryRequirementsField.getText().toString().trim();
        String minimumGpa = minimumGpaField.getText().toString().trim();
        String jobStart = jobStartField.getText().toString().trim();
        String applicationStart = applicationStartField.getText().toString().trim();
        String applicationEnd = applicationEndField.getText().toString().trim();

        // Validate jobType against the allowed enum values
        List<String> validJobTypes = Arrays.asList("POST_GRADUATION_JOB", "CO_OP", "INTERNSHIP");
        if (!validJobTypes.contains(jobType)) {
            jobTypeField.setError("Invalid job type. Allowed values: POST_GRADUATION_JOB, CO_OP, INTERNSHIP.");
            Toast.makeText(this, "Invalid job type. Please enter a valid job type.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve employerId dynamically from SharedPreferences
        SharedPreferences preferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        int employerId = preferences.getInt("userId", -1);

        if (employerId == -1) {
            Toast.makeText(this, "Error: User not logged in. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Construct the job posting JSON object
        JSONObject jobData = new JSONObject();
        try {
            jobData.put("title", jobTitle);
            jobData.put("description", jobDescription);
            jobData.put("summary", summary);
            jobData.put("salary", Double.parseDouble(salaryRequirements)); // Assuming salary is a double
            jobData.put("jobType", jobType);
            jobData.put("minimumGpa", Double.parseDouble(minimumGpa)); // Assuming GPA is a double
            jobData.put("jobStart", jobStart);
            jobData.put("applicationStart", applicationStart);
            jobData.put("applicationEnd", applicationEnd);
            jobData.put("employerId", employerId);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating job data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send the job data to the server using the EmployerApis
        EmployerApis.addJob(this, jobData,
                response -> {
                    Toast.makeText(AddJobActivity.this, "Job posted successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity and return to the previous screen
                },
                error -> {
                    String errorMessage = getErrorMessage(error);
                    Toast.makeText(AddJobActivity.this, "Error posting job: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
    }

    private String getErrorMessage(VolleyError error) {
        String errorMsg = "An unexpected error occurred";
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String errorData = new String(error.networkResponse.data, "UTF-8");
                try {
                    JSONObject jsonError = new JSONObject(errorData);
                    errorMsg = jsonError.optString("message", errorMsg);
                } catch (JSONException e) {
                    errorMsg = errorData;
                }
            } catch (UnsupportedEncodingException e) {
                errorMsg = error.getMessage();
            }
        }
        return errorMsg;
    }
}
