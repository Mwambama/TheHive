//package com.example.hiveeapp.employer_user.applications;
//
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import com.android.volley.VolleyError;
//import com.example.hiveeapp.R;
//import com.example.hiveeapp.employer_user.display.EmployerApis;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import java.io.UnsupportedEncodingException;
//
///**
// * AddJobActivity allows the user to add a new job posting by filling out a form.
// * The form includes fields like job title, description, requirements, etc., which are validated before submission.
// */
//public class AddJobActivity extends AppCompatActivity {
//
//    private EditText jobTitleField, jobDescriptionField, summaryField, jobTypeField, salaryRequirementsField,
//            minimumGpaField, jobStartField, applicationStartField, applicationEndField;
//    private Button postJobButton;
//    private ImageButton backArrowIcon;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_job); // Ensure this matches your XML layout file name
//
//        // Initialize the input fields and buttons
//        initViews();
//
//        // Set up back arrow functionality to close the current activity and return to the previous one
//        backArrowIcon.setOnClickListener(v -> finish());
//
//        // Handle the "Post Job" button click and validate inputs
//        postJobButton.setOnClickListener(v -> {
//            if (validateInput()) {
//                addJobs();  // If inputs are valid, proceed to add the job
//            }
//        });
//    }
//
//    /**
//     * Initializes the input fields and buttons in the activity layout.
//     */
//    private void initViews() {
//        jobTitleField = findViewById(R.id.jobTitleField); // Ensure these IDs match your layout
//        jobDescriptionField = findViewById(R.id.jobDescriptionField);
//        summaryField = findViewById(R.id.summaryField);
//        jobTypeField = findViewById(R.id.jobTypeField);
//        salaryRequirementsField = findViewById(R.id.salaryRequirementsField);
//        minimumGpaField = findViewById(R.id.minimumGpaField);
//        jobStartField = findViewById(R.id.jobStartDateField);
//        applicationStartField = findViewById(R.id.applicationStartDateField);
//        applicationEndField = findViewById(R.id.applicationEndDateField);
//        postJobButton = findViewById(R.id.addJobButton);
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
//        jobTitleField.setError(null);
//        jobDescriptionField.setError(null);
//        summaryField.setError(null);
//        jobTypeField.setError(null);
//        salaryRequirementsField.setError(null);
//        minimumGpaField.setError(null);
//        jobStartField.setError(null);
//        applicationStartField.setError(null);
//        applicationEndField.setError(null);
//
//        // Get the input values from the fields
//        String jobTitle = jobTitleField.getText().toString().trim();
//        String jobDescription = jobDescriptionField.getText().toString().trim();
//        String summary = summaryField.getText().toString().trim();
//        String jobType = jobTypeField.getText().toString().trim();
//        String salaryRequirements = salaryRequirementsField.getText().toString().trim();
//        String minimumGpa = minimumGpaField.getText().toString().trim();
//        String jobStart = jobStartField.getText().toString().trim();
//        String applicationStart = applicationStartField.getText().toString().trim();
//        String applicationEnd = applicationEndField.getText().toString().trim();
//
//        // Validate the job title field
//        if (jobTitle.isEmpty()) {
//            jobTitleField.setError("Job title is required");
//            isValid = false;
//        }
//
//        // Validate the job description field
//        if (jobDescription.isEmpty()) {
//            jobDescriptionField.setError("Job description is required");
//            isValid = false;
//        }
//
//        // Validate the summary field
//        if (summary.isEmpty()) {
//            summaryField.setError("Summary is required");
//            isValid = false;
//        }
//
//        // Validate the job type field
//        if (jobType.isEmpty()) {
//            jobTypeField.setError("Job type is required");
//            isValid = false;
//        }
//
//        // Validate the salary requirements field
//        if (salaryRequirements.isEmpty()) {
//            salaryRequirementsField.setError("Salary requirements are required");
//            isValid = false;
//        }
//
//        // Validate the minimum GPA field
//        if (minimumGpa.isEmpty()) {
//            minimumGpaField.setError("Minimum GPA is required");
//            isValid = false;
//        }
//
//        // Validate the job start date field
//        if (jobStart.isEmpty()) {
//            jobStartField.setError("Job start date is required");
//            isValid = false;
//        }
//
//        // Validate the application start date field
//        if (applicationStart.isEmpty()) {
//            applicationStartField.setError("Application start date is required");
//            isValid = false;
//        }
//
//        // Validate the application end date field
//        if (applicationEnd.isEmpty()) {
//            applicationEndField.setError("Application end date is required");
//            isValid = false;
//        }
//
//        return isValid;
//    }
//
//    /**
//     * Constructs a JSON object with job details and sends a request to add a new job posting.
//     */
//    private void addJobs() {
//        // Get the values from the input fields
//        String jobTitle = jobTitleField.getText().toString().trim();
//        String jobDescription = jobDescriptionField.getText().toString().trim();
//        String summary = summaryField.getText().toString().trim();
//        String jobType = jobTypeField.getText().toString().trim();
//        String salaryRequirements = salaryRequirementsField.getText().toString().trim();
//        String minimumGpa = minimumGpaField.getText().toString().trim();
//        String jobStart = jobStartField.getText().toString().trim();
//        String applicationStart = applicationStartField.getText().toString().trim();
//        String applicationEnd = applicationEndField.getText().toString().trim();
//
//        // Construct the job posting JSON object
//        JSONObject jobData = new JSONObject();
//        try {
//            // jobData.put("jobPostingId", 625); // Hardcoded for now; adjust as necessary
//            jobData.put("title", jobTitle);
//            jobData.put("description", jobDescription);
//            jobData.put("summary", summary);
//            jobData.put("salary", Double.parseDouble(salaryRequirements)); // Assuming salary is an integer // change to double
//            jobData.put("jobType", jobType); // Ensure jobType matches your enum or backend requirement
//            jobData.put("minimumGpa", Double.parseDouble(minimumGpa)); // Assuming GPA is a double
//            jobData.put("jobStart", jobStart);
//            jobData.put("applicationStart", applicationStart);
//            jobData.put("applicationEnd", applicationEnd);
//
//            // Add employerId logic (replace with actual logic to retrieve employerId)
//            int employerId = 373; // Hardcoded for now; replace with your logic
//            jobData.put("employerId", employerId);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Error creating job data", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Send the job data to the server using the EmployerApis
//
//        EmployerApis.addJob(this, jobData,
//                response -> {
//                    // Handle successful response
//                    Toast.makeText(com.example.hiveeapp.employer_user.display.AddJobActivity.this, "Job posted successfully!", Toast.LENGTH_SHORT).show();
//                    finish();  // Close the activity and return to the previous screen
//                },
//                error -> {
//                    // Handle error response and display user-friendly message
//                    String errorMessage = getErrorMessage(error);
//                    Toast.makeText(com.example.hiveeapp.employer_user.display.AddJobActivity.this, "Error posting job: " + errorMessage, Toast.LENGTH_SHORT).show();
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
//                errorMsg = error.getMessage();
//            }
//        }
//        return errorMsg;
//    }
//}
//
