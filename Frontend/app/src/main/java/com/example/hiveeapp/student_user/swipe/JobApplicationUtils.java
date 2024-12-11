package com.example.hiveeapp.student_user.swipe;

import android.content.Context;
import android.content.SharedPreferences;
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
    private static final String MOCK_EMAIL = "test643@example.com";
    private static final String MOCK_PASSWORD = "Test$1234";

    /**
     * Submits a job application for the given student ID and job posting ID.
     *
     * @param context      The context used for network requests.
     * @param studentId    The ID of the student applying for the job.
     * @param jobPostingId The ID of the job being applied to.
     * @param onSuccess    Runnable to execute on a successful request.
     * @param onError      Runnable to execute on a failed request.
     */
    public static void applyForJob(Context context, int studentId, int jobPostingId, Runnable onSuccess, Runnable onError) {
        try {
            // Create the JSON body for the request
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("studentId", studentId);
            jsonObject.put("jobPostingId", jobPostingId);

            StringRequest request = new StringRequest(Request.Method.POST, APPLY_URL,
                    response -> {
                        Log.d(TAG, "Application response: " + response);
                        Toast.makeText(context, "Application submitted successfully!", Toast.LENGTH_SHORT).show();
                        if (onSuccess != null) onSuccess.run();
                    },
                    error -> {
                        handleError(context, error);
                        if (onError != null) onError.run();
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return createAuthorizationHeaders(context);
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return jsonObject.toString().getBytes();
                }
            };

            VolleySingleton.getInstance(context).addToRequestQueue(request);
        } catch (Exception e) {
            Log.e(TAG, "Error creating application request", e);
            Toast.makeText(context, "Error creating application request.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handles errors returned from the job application request.
     *
     * @param context The context for displaying error messages.
     * @param error   The VolleyError returned from the network request.
     */
    private static void handleError(Context context, VolleyError error) {
        String errorMessage = "Failed to apply for job.";
        if (error.networkResponse != null) {
            if (error.networkResponse.statusCode == 500) {
                errorMessage = "Server error: Unable to process the application. Please try again later.";
            } else if (error.networkResponse.statusCode == 401) {
                errorMessage = "Unauthorized access. Please log in again.";
            }
        }
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Error applying for job: ", error);
    }

    /**
     * Creates headers for the job application request, including authorization credentials.
     *
     * @param context The context used to access SharedPreferences for user credentials.
     * @return A map of headers.
     */
    private static Map<String, String> createAuthorizationHeaders(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        SharedPreferences preferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        String email = preferences.getString("userEmail", MOCK_EMAIL);
        String password = preferences.getString("userPassword", MOCK_PASSWORD);

        String credentials = email + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        Log.d(TAG, "Authorization Header: " + auth);
        return headers;
    }
}
