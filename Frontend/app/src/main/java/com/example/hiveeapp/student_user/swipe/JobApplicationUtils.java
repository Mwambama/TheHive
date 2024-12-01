package com.example.hiveeapp.student_user.swipe;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JobApplicationUtils {
    private static final String TAG = "JobApplicationUtils";
    private static final String APPLY_URL = "http://coms-3090-063.class.las.iastate.edu:8080/applications/apply";

    public static void applyForJob(Context context, int studentId, int jobPostingId) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("studentId", studentId);
            jsonObject.put("jobPostingId", jobPostingId);

            String requestBody = jsonObject.toString();

            StringRequest request = new StringRequest(Request.Method.POST, APPLY_URL,
                    response -> Toast.makeText(context, "Application submitted successfully!", Toast.LENGTH_SHORT).show(),
                    error -> handleError(context, error)) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.putAll(createAuthorizationHeaders(context));
                    return headers;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return requestBody.getBytes();
                }
            };

            VolleySingleton.getInstance(context).addToRequestQueue(request);
        } catch (Exception e) {
            Log.e(TAG, "Error creating application request", e);
            Toast.makeText(context, "Error creating application request.", Toast.LENGTH_SHORT).show();
        }
    }

    private static void handleError(Context context, VolleyError error) {
        String errorMessage = "Failed to apply for job.";
        if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
            errorMessage = "Server error: Unable to process the application. Please try again later.";
        }
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Error applying for job: ", error);
    }

    private static Map<String, String> createAuthorizationHeaders(Context context) {
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

