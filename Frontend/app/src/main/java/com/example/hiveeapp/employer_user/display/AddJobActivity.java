package com.example.hiveeapp.employer_user.display;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;
import org.json.JSONException;
import org.json.JSONObject;

public class AddJobActivity extends AppCompatActivity {
    private EditText jobTitleEditText;
    private EditText jobDescriptionEditText;
    private EditText summaryEditText;
    private EditText jobTypeEditText;
    private EditText salaryRequirementsEditText;
    private EditText ageRequirementEditText;
    private EditText minimumGpaEditText;
    private EditText jobStartEditText;
    private EditText applicationStartEditText;
    private EditText applicationEndEditText;
    private Button postJobButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        // Initialize EditText fields
        jobTitleEditText = findViewById(R.id.job_title);
        jobDescriptionEditText = findViewById(R.id.job_description);
        summaryEditText = findViewById(R.id.summary);
        jobTypeEditText = findViewById(R.id.job_type);
        salaryRequirementsEditText = findViewById(R.id.salary_requirements);
        ageRequirementEditText = findViewById(R.id.age_requirement);
        minimumGpaEditText = findViewById(R.id.minimum_gpa);
        jobStartEditText = findViewById(R.id.job_start);
        applicationStartEditText = findViewById(R.id.application_start);
        applicationEndEditText = findViewById(R.id.application_end);
        postJobButton = findViewById(R.id.post_job_button);
        backButton = findViewById(R.id.back_button);

        // Post Job button click listener
        postJobButton.setOnClickListener(view -> {
            String jobTitle = jobTitleEditText.getText().toString();
            String jobDescription = jobDescriptionEditText.getText().toString();
            String summary = summaryEditText.getText().toString();
            String jobType = jobTypeEditText.getText().toString();
            String salaryRequirements = salaryRequirementsEditText.getText().toString();
            String ageRequirement = ageRequirementEditText.getText().toString();
            String minimumGpa = minimumGpaEditText.getText().toString();
            String jobStart = jobStartEditText.getText().toString();
            String applicationStart = applicationStartEditText.getText().toString();
            String applicationEnd = applicationEndEditText.getText().toString();

            if (jobTitle.isEmpty() || jobDescription.isEmpty() || summary.isEmpty() || jobType.isEmpty() ||
                    salaryRequirements.isEmpty() || ageRequirement.isEmpty() || minimumGpa.isEmpty() ||
                    jobStart.isEmpty() || applicationStart.isEmpty() || applicationEnd.isEmpty()) {
                Toast.makeText(AddJobActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create JSON object with job data
            JSONObject jobData = new JSONObject();
            try {
                jobData.put("title", jobTitle);
                jobData.put("description", jobDescription);
                jobData.put("summary", summary);
                jobData.put("jobType", jobType);
                jobData.put("salary", salaryRequirements);
                jobData.put("ageRequirement", ageRequirement);
                jobData.put("minimumGpa", minimumGpa);
                jobData.put("jobStart", jobStart);
                jobData.put("applicationStart", applicationStart);
                jobData.put("applicationEnd", applicationEnd);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // URL for the Postman mock server
            String url = "http://coms-3090-063.class.las.iastate.edu:8080/job-posting"; // Replace with your mock server URL

            // Create JSON request
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jobData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Handle successful job post
                            Toast.makeText(AddJobActivity.this, "Job posted successfully", Toast.LENGTH_SHORT).show();

                            // Extract jobId from the response if available
                            String jobId;
                            try {
                                jobId = response.getString("jobId"); // Adjust based on your server response
                            } catch (JSONException e) {
                                jobId = "unknown"; // Default value if not present
                            }

                            // Create a new PostedJobs object from the input fields
                            PostedJobs newJob = new PostedJobs(
                                    jobId, // Pass the jobId here
                                    jobTitle,
                                    jobDescription,
                                    jobType,
                                    salaryRequirements,
                                    ageRequirement,
                                    minimumGpa
                            );

                            // Create an Intent to return the new job to the CreateJobsActivity
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("newJob", newJob); // Ensure PostedJobs implements Serializable
                            setResult(RESULT_OK, resultIntent);

                            finish(); // Close AddJobActivity and go back to CreateJobsActivity
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error
                            Toast.makeText(AddJobActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            // Add request to the Volley request queue
            VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        });

        // Back button click listener
        backButton.setOnClickListener(view -> finish());
    }
}
