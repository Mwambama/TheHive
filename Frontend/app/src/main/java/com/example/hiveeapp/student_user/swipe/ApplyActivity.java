package com.example.hiveeapp.student_user.swipe;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ApplyActivity extends AppCompatActivity {

    private static final String TAG = "ApplyActivity";
    private static final String PREFERENCES_NAME = "JobSwipePreferences";
    private static final String APPLIED_JOBS_KEY = "AppliedJobs";

    private int studentId;
    private int jobPostingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        // Get the studentId and jobPostingId from Intent extras
        studentId = getIntent().getIntExtra("studentId", -1);
        jobPostingId = getIntent().getIntExtra("jobPostingId", -1);

        if (studentId != -1 && jobPostingId != -1) {
            applyForJob(studentId, jobPostingId);
        } else {
            Toast.makeText(this, "Invalid data provided for job application.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Invalid studentId or jobPostingId");
        }
    }

    private void applyForJob(int studentId, int jobPostingId) {
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/applications/apply";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("studentId", studentId);
            jsonObject.put("jobPostingId", jobPostingId);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON object for application request", e);
            Toast.makeText(this, "Error creating application request.", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    try {
                        // Parse and display the server response
                        String message = response.optString("message", "Application submitted successfully!");
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        saveAppliedJob(jobPostingId); // Persist the applied job
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing server response", e);
                    }
                },
                error -> {
                    String errorMessage = "Failed to apply for job.";
                    if (error.networkResponse != null) {
                        if (error.networkResponse.statusCode == 500) {
                            errorMessage = "Server error: Unable to process the application. Please try again later.";
                        } else if (error.networkResponse.statusCode == 401) {
                            errorMessage = "Unauthorized access. Please log in again.";
                        }
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error applying for job: ", error);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.putAll(createAuthorizationHeaders());
                return headers;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void saveAppliedJob(int jobPostingId) {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Set<String> appliedJobs = preferences.getStringSet(APPLIED_JOBS_KEY, new HashSet<>());
        appliedJobs.add(String.valueOf(jobPostingId));

        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(APPLIED_JOBS_KEY, appliedJobs);
        editor.apply();

        Log.d(TAG, "Job ID " + jobPostingId + " saved as applied.");
    }

    private Map<String, String> createAuthorizationHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        String username = "teststudent1@example.com";
        String password = "TestStudent1234@";

        String credentials = username + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        return headers;
    }
}
