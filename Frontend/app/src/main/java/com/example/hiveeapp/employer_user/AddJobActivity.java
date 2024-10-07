package com.example.hiveeapp.employer_user;
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
        private EditText jobTypeEditText;
        private EditText salaryRequirementsEditText;
        private EditText ageRequirementEditText;
        private EditText minimumGpaEditText;
        private Button postJobButton;
        private Button backButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_job);

            // Initialize EditText fields
            jobTitleEditText = findViewById(R.id.job_title);
            jobDescriptionEditText = findViewById(R.id.job_description);
            jobTypeEditText = findViewById(R.id.job_type);
            salaryRequirementsEditText = findViewById(R.id.salary_requirements);
            ageRequirementEditText = findViewById(R.id.age_requirement);
            minimumGpaEditText = findViewById(R.id.minimum_gpa);
            postJobButton = findViewById(R.id.post_job_button);
            backButton = findViewById(R.id.back_button);

            // Post Job button click listener
            postJobButton.setOnClickListener(view -> {
                String jobTitle = jobTitleEditText.getText().toString();
                String jobDescription = jobDescriptionEditText.getText().toString();
                String jobType = jobTypeEditText.getText().toString();
                String salaryRequirements = salaryRequirementsEditText.getText().toString();
                String ageRequirement = ageRequirementEditText.getText().toString();
                String minimumGpa = minimumGpaEditText.getText().toString();

                if (jobTitle.isEmpty() || jobDescription.isEmpty() || jobType.isEmpty() || salaryRequirements.isEmpty() ||
                        ageRequirement.isEmpty() || minimumGpa.isEmpty()) {
                    Toast.makeText(AddJobActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create JSON object with job data
                JSONObject jobData = new JSONObject();
                try {
                    jobData.put("jobTitle", jobTitle);
                    jobData.put("jobDescription", jobDescription);
                    jobData.put("jobType", jobType);
                    jobData.put("salaryRequirements", salaryRequirements);
                    jobData.put("ageRequirement", ageRequirement);
                    jobData.put("minimumGpa", minimumGpa);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // URL for the Postman mock server
                String url = "https://8c5d8b24-4a9a-4ce2-bf22-1aa5316f76a2.mock.pstmn.io/employer_user/post"; // Replace with your mock server URL

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
                                finish(); // Close AddJobActivity and go back to EmployerMainActivity
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


