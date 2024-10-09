package com.example.hiveeapp.volley;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonArrReqActivity extends AppCompatActivity {
    TextView textView;
    private static final boolean TESTING_MODE = true;
    private static final String USER_PREFS = "user_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        textView = findViewById(R.id.textView);

        // Fetch job posts from the server
        fetchJobPosts();
    }

    // Fetch job posts from the backend
    private void fetchJobPosts() {
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/job-posting"; // Replace with your server URL

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        textView.setText("Error fetching job posts");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return createHeaders(); // Use the new method to get headers
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    // Helper method to create headers with authorization for the currently logged-in user
    private Map<String, String> createHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        String username;
        String password;

        if (TESTING_MODE) {
            // For testing purposes, use hardcoded credentials
            username = "employer@example.com";
            password = "Test@1234";
        } else {
            // Retrieve username and password from SharedPreferences
            SharedPreferences preferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
            username = preferences.getString("username", null);
            password = preferences.getString("password", null);
        }

        if (username != null && password != null) {
            String credentials = username + ":" + password;
            String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            headers.put("Authorization", auth);
        }

        return headers;
    }
}
