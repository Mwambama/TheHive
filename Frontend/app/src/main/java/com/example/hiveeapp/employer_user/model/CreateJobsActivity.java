package com.example.hiveeapp.employer_user.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.display.PostedJobs;
import com.example.hiveeapp.volley.VolleySingleton;
import com.example.hiveeapp.employer_user.display.AddJobActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateJobsActivity extends AppCompatActivity {
    private TextView textView;
    private static final String USER_PREFS = "user_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_jobs);

        textView = findViewById(R.id.posted_job_text_view);
        Button backButton = findViewById(R.id.back_button);
        Button addJobButton = findViewById(R.id.add_job_button);

        backButton.setOnClickListener(v -> finish()); // Handle back button click

        addJobButton.setOnClickListener(v -> {
            // Handle add job button click
            Intent intent = new Intent(CreateJobsActivity.this, AddJobActivity.class);
            startActivity(intent);
        });

        // Fetch job posts when the activity is created
        fetchJobPosts();
    }

    // Fetch job posts from the backend
    private void fetchJobPosts() {
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/job-posting"; // Replace with your mock server URL

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseJobPosts(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(CreateJobsActivity.this, "Error fetching job posts", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return CreateJobsActivity.getHeaders(CreateJobsActivity.this);
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    // Helper method to parse the job posts from the response
    private void parseJobPosts(JSONArray response) {
        try {
            StringBuilder jsonData = new StringBuilder();

            for (int i = 0; i < response.length(); i++) {
                JSONObject job = response.getJSONObject(i);

                // Get job info
                String jobTitle = job.getString("jobTitle");
                String jobDescription = job.getString("jobDescription");
                String jobType = job.getString("jobType");
                String salaryRequirements = job.getString("salaryRequirements");
                String ageRequirement = job.getString("ageRequirement");
                String minimumGPA = job.getString("minimumGPA");

                // Build the display string
                jsonData.append("Job Title: ").append(jobTitle)
                        .append("\nJob Description: ").append(jobDescription)
                        .append("\nJob Type: ").append(jobType)
                        .append("\nSalary Requirements: ").append(salaryRequirements)
                        .append("\nAge Requirement: ").append(ageRequirement)
                        .append("\nMinimum GPA: ").append(minimumGPA)
                        .append("\n\n");
            }

            // Display the data in the TextView
            textView.setText(jsonData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            textView.setText("Error parsing JSON data");
        }
    }

    // Create a new job post
    private void createJobPost(JSONObject jobData) {
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/job-posting"; // Replace with your mock server URL

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jobData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(CreateJobsActivity.this, "Job posted successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(CreateJobsActivity.this, "Error posting job", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return CreateJobsActivity.getHeaders(CreateJobsActivity.this);
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    // Update an existing job post
    // the get  jobId propertyis here to store the unique identifier for each job.
    private void updateJobPost(PostedJobs job) {
        String url = "https://8c5d8b24-4a9a-4ce2-bf22-1aa5316f76a2.mock.pstmn.io/employer_user/update/" + job.getJobId(); // Replace with your mock server URL

        JSONObject jobData = new JSONObject();
        try {
            jobData.put("jobTitle", job.getJobTitle());
            jobData.put("jobDescription", job.getJobDescription());
            jobData.put("jobType", job.getJobType());
            jobData.put("salaryRequirements", job.getSalaryRequirements());
            jobData.put("ageRequirement", job.getAgeRequirement());
            jobData.put("minimumGPA", job.getMinimumGpa());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                jobData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(CreateJobsActivity.this, "Job updated successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(CreateJobsActivity.this, "Error updating job", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return CreateJobsActivity.getHeaders(CreateJobsActivity.this);
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    // Delete a job post
    private void deleteJobPost(String jobId) {
        String url = "https://8c5d8b24-4a9a-4ce2-bf22-1aa5316f76a2.mock.pstmn.io/employer_user/delete/" + jobId; // Replace with your mock server URL

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(CreateJobsActivity.this, "Job deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(CreateJobsActivity.this, "Error deleting job", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return CreateJobsActivity.getHeaders(CreateJobsActivity.this);
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    // Helper method to get headers with authorization for the currently logged-in user
    private static Map<String, String> getHeaders(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        String username;
        String password;

        // Retrieve username and password from SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        username = preferences.getString("username", null);
        password = preferences.getString("password", null);

        if (username != null && password != null) {
            String credentials = username + ":" + password;
            String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            headers.put("Authorization", auth);
        }

        return headers;
    }
}
