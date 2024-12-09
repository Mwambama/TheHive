package com.example.hiveeapp.employer_user.display;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class EmployerApis {

    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/job-posting";
    private static final String TAG = "EmployerApis";
    private static final String USER_PREFS = "UserPreferences"; // SharedPreferences name

    /**
     * Generates the headers for API requests with authorization.
     *
     * @param context The application context used to retrieve user credentials.
     * @return A map of headers including content type and authorization credentials.
     */
    public static Map<String, String> getHeaders(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        String email = preferences.getString("email", ""); // Retrieved during login
        String password = preferences.getString("password", ""); // Retrieved during login

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        if (!email.isEmpty() && !password.isEmpty()) {
            String credentials = email + ":" + password;
            String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            headers.put("Authorization", auth);
        }

        return headers;
    }

    /**
     * Retrieves a list of jobs for the logged-in employer.
     *
     * @param context       The application context.
     * @param listener      Response listener for successful fetch.
     * @param errorListener Error listener for handling errors.
     */
    public static void getJobs(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        SharedPreferences preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1); // Retrieve the logged-in user ID

        if (userId == -1) {
            Log.e(TAG, "getJobs: User ID not found in SharedPreferences.");
            Toast.makeText(context, "User not logged in. Please log in again.", Toast.LENGTH_SHORT).show();
            errorListener.onErrorResponse(new VolleyError("User ID missing"));
            return;
        }

        String url = BASE_URL + "?employerId=" + userId; // Use dynamic employerId
        Log.d(TAG, "GET Employers Request URL: " + url);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                listener,
                error -> handleErrorResponse("Error fetching employers", error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return EmployerApis.getHeaders(context); // Include authorization headers
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void getGraphImage(Context context, long jobId, Response.Listener<byte[]> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/analytics/" + jobId; // Backend endpoint for fetching the graph
        Log.d(TAG, "GET Graph Request URL: " + url);

        // Use the ByteRequest to fetch binary data
        analyticRequest byteRequest = new analyticRequest(
                Request.Method.GET,
                url,
                listener,
                error -> handleErrorResponse("Error fetching graph image", error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return EmployerApis.getHeaders(context); // Use authorization headers
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(byteRequest);
    }

    /**
     * Internal method to add a new employer (called after address creation).
     *
     * @param context       The application context.
     * @param employerData  JSON object containing employer details.
     * @param listener      Response listener for successful employer creation.
     * @param errorListener Error listener for handling errors.
     */
    static void addJob(Context context, JSONObject employerData, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL;
        Log.d(TAG, "POST Employer Request URL: " + url);
        Log.d(TAG, "Employer Data Payload: " + employerData.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                employerData,
                listener,
                error -> handleErrorResponse("Error adding employer: " + getErrorMessage(error), error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return EmployerApis.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Updates an existing employer with or without an address.
     *
     * @param context       The application context.
     * @param employerData  JSON object containing employer details.
     * @param listener      Response listener for successful employer update.
     * @param errorListener Error listener for handling errors.
     */
    public static void updateJob(Context context, JSONObject employerData, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String validationError = validateJobData(employerData);
        if (validationError != null) {
            Toast.makeText(context, validationError, Toast.LENGTH_LONG).show();
            errorListener.onErrorResponse(new VolleyError(validationError));
            return;
        }

        performJobUpdate(context, employerData, listener, errorListener);
    }

    public static String validateJobData(JSONObject employerData) {
        try {
            if (employerData.optString("jobTitle", "").isEmpty()) return "Job title is required.";
            if (employerData.optString("jobDescription", "").isEmpty()) return "Job description is required.";
            if (employerData.optString("summary", "").isEmpty()) return "Summary is required.";
            if (employerData.optString("jobType", "").isEmpty()) return "Job type is required.";
            if (employerData.optString("salaryRequirements", "").isEmpty()) return "Salary requirements are required.";
            if (employerData.optString("minimumGpa", "").isEmpty()) return "Minimum GPA is required.";
            if (employerData.optString("jobStart", "").isEmpty()) return "Job start date is required.";
            if (employerData.optString("applicationStart", "").isEmpty()) return "Application start date is required.";
            if (employerData.optString("applicationEnd", "").isEmpty()) return "Application end date is required.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error validating employer data.";
        }

        return null;
    }

    static void performJobUpdate(Context context, JSONObject employerData, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL;
        Log.d(TAG, "PUT Employer Request URL: " + url);
        Log.d(TAG, "Employer Data Payload: " + employerData.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                employerData,
                listener,
                error -> handleErrorResponse("Error updating job: " + getErrorMessage(error), error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return EmployerApis.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void deleteEmployer(Context context, long employerId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/" + employerId;
        Log.d(TAG, "DELETE Employer Request URL: " + url);

        StringRequest request = new StringRequest(
                Request.Method.DELETE,
                url,
                response -> {
                    Log.d(TAG, "Employer deleted successfully: " + response);
                    listener.onResponse(response);
                },
                error -> handleErrorResponse("Error deleting employer", error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return EmployerApis.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    private static void handleErrorResponse(String errorMessagePrefix, VolleyError error, Response.ErrorListener errorListener) {
        String errorMsg = getErrorMessage(error);
        Log.e(TAG, errorMessagePrefix + ": " + errorMsg);
        errorListener.onErrorResponse(new VolleyError(errorMsg));
    }

    private static String getErrorMessage(VolleyError error) {
        String errorMsg = "An unexpected error occurred";
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String errorData = new String(error.networkResponse.data, "UTF-8");
                JSONObject jsonError = new JSONObject(errorData);
                errorMsg = jsonError.optString("message", errorMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (error.getMessage() != null) {
            errorMsg = error.getMessage();
        }
        return errorMsg;
    }
}
