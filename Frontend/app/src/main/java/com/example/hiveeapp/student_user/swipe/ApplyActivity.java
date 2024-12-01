package com.example.hiveeapp.student_user.swipe;

import static com.example.hiveeapp.student_user.swipe.JobSwipeFragment.createAuthorizationHeaders;

import android.content.Context;
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
import java.util.Map;

public class ApplyActivity extends AppCompatActivity {

    private static final String TAG = "ApplyActivity";
    private int studentId;
    private int jobPostingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Updated layout file reference
        setContentView(R.layout.activity_swipe);

        // Get the studentId and jobPostingId from Intent extras
        studentId = getIntent().getIntExtra("studentId", -1);
        jobPostingId = getIntent().getIntExtra("jobPostingId", -1);

        // Handle the application process when needed
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
            e.printStackTrace();
            Toast.makeText(this, "Error creating application request.", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    try {
                        // Parse the plain string response
                        String message = response.toString();
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing server response", e);
                    }
                },
                error -> {
                    String errorMessage = "Failed to apply for job.";
                    if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                        errorMessage = "Server error: Unable to process the application. Please try again later.";
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error applying for job: ", error);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json"); // Ensure JSON content type is set
                headers.putAll(JobSwipeFragment.createAuthorizationHeaders(ApplyActivity.this)); // Add authorization headers
                return headers;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }


    /**
     * Generates headers for API requests with authorization.
     */
    public static Map<String, String> createAuthorizationHeaders(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        String username = "teststudent1@example.com";
        String password = "TestStudent1234@";

        String credentials = username + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        return headers;
    }

    private void handleError(VolleyError error) {
        if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
            Toast.makeText(this, "You have already applied for this job.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to apply. Please try again.", Toast.LENGTH_SHORT).show();
        }
        Log.e(TAG, "Error applying for job: ", error);
    }
}
