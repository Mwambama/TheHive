package com.example.hiveeapp.employer_user.display;

import android.content.Context;
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
import java.util.HashMap;
import java.util.Map;

public class EmployerApis {

    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/job-posting";

    private static final String TAG = "EmployerApis";

    /**
     * Generates the headers for API requests with authorization.
     *
     * @param context The application context used to retrieve user credentials.
     * @return A map of headers including content type and authorization credentials.
     */
    public static Map<String, String> getHeaders(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // Mocked username and password for testing purposes
        String username = "iiik@gmail.com";
        String password = "Anondwdb##444fedo";

        String credentials = username + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        return headers;
    }

    /**
     * Retrieves a list of employers from the server.
     *
     * @param context       The application context.
     * @param listener      Response listener for successful fetch.
     * @param errorListener Error listener for handling errors.
     */
    public static void getJobs(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "?employerId=521";
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
                return com.example.hiveeapp.employer_user.display.EmployerApis.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
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
                return com.example.hiveeapp.employer_user.display.EmployerApis.getHeaders(context);
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
        // Validate employer data before proceeding
        String validationError = validateJobData(employerData);
        if (validationError != null) {
            // Show a personalized error message if validation fails
            Toast.makeText(context, validationError, Toast.LENGTH_LONG).show();
            errorListener.onErrorResponse(new VolleyError(validationError));
            return; // Validation failed, do not proceed
        }
                 //  continues to add the job
            performJobUpdate(context, employerData, listener, errorListener);
    }


    /**
     * Validates the jobs data before sending it to the server.
     *
     * @param employerData The job JSON object.
     * @return A string containing the validation error message, or null if validation passes.
     */

    public static String validateJobData(JSONObject employerData) {
        try {
            // Validate job title field
            String jobTitle = employerData.optString("jobTitle", "");
            if (jobTitle.isEmpty()) {
                return "Job title is required.";
            }

            // Validate job description field
            String jobDescription = employerData.optString("jobDescription", "");
            if (jobDescription.isEmpty()) {
                return "Job description is required.";
            }

            // Validate summary field
            String summary = employerData.optString("summary", "");
            if (summary.isEmpty()) {
                return "Summary is required.";
            }

            // Validate job type field
            String jobType = employerData.optString("jobType", "");
            if (jobType.isEmpty()) {
                return "Job type is required.";
            }

            // Validate salary requirements field
            String salaryRequirements = employerData.optString("salaryRequirements", "");
            if (salaryRequirements.isEmpty()) {
                return "Salary requirements are required.";
            }

            // Validate minimum GPA field
            String minimumGpa = employerData.optString("minimumGpa", "");
            if (minimumGpa.isEmpty()) {
                return "Minimum GPA is required.";
            }

            // Validate job start date field
            String jobStart = employerData.optString("jobStart", "");
            if (jobStart.isEmpty()) {
                return "Job start date is required.";
            }

            // Validate application start date field
            String applicationStart = employerData.optString("applicationStart", "");
            if (applicationStart.isEmpty()) {
                return "Application start date is required.";
            }

            // Validate application end date field
            String applicationEnd = employerData.optString("applicationEnd", "");
            if (applicationEnd.isEmpty()) {
                return "Application end date is required.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error validating employer data.";
        }

        return null; // Validation passed
    }

    /**
     * Internal method to perform employer update after any address updates.
     *
     * @param context       The application context.
     * @param employerData  JSON object containing employer details.
     * @param listener      Response listener for successful employer update.
     * @param errorListener Error listener for handling errors.
     */
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
                return com.example.hiveeapp.employer_user.display.EmployerApis.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Deletes an employer from the server.
     *
     * @param context       The application context.
     * @param employerId    ID of the employer to be deleted.
     * @param listener      Response listener for successful employer deletion.
     * @param errorListener Error listener for handling errors.
     */
    public static void deleteEmployer(Context context, long employerId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/" + employerId;
        Log.d(TAG, "DELETE Employer Request URL: " + url);

        // Create a StringRequest for the DELETE method
        StringRequest request = new StringRequest(
                Request.Method.DELETE,
                url,
                response -> {
                    Log.d(TAG, "Employer deleted successfully: " + response);
                    listener.onResponse(response);  // Notify the listener of success
                },
                error -> {
                    String errorMsg = getErrorMessage(error);
                    handleErrorResponse("Error deleting employer: " + errorMsg, error, errorListener);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return com.example.hiveeapp.employer_user.display.EmployerApis.getHeaders(context);
            }
        };

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Handles error responses from the server, logs the details, and invokes the error listener.
     *
     * @param errorMessagePrefix Prefix for the error message to log.
     * @param error              The VolleyError object.
     * @param errorListener      Error listener to handle the error response.
     */
    private static void handleErrorResponse(String errorMessagePrefix, VolleyError error, Response.ErrorListener errorListener) {
        String errorMsg = getErrorMessage(error);
        String fullErrorMessage = errorMessagePrefix + ": " + errorMsg;
        Log.e(TAG, fullErrorMessage);
        errorListener.onErrorResponse(new VolleyError(fullErrorMessage));
    }

    /**
     * Extracts a meaningful error message from a VolleyError.
     *
     * @param error The VolleyError object.
     * @return A string containing the error message.
     */
    private static String getErrorMessage(VolleyError error) {
        String errorMsg = "An unexpected error occurred";
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String errorData = new String(error.networkResponse.data, "UTF-8");

                // Attempt to parse errorData as JSON
                try {
                    JSONObject jsonError = new JSONObject(errorData);
                    errorMsg = jsonError.optString("message", errorMsg);
                } catch (JSONException jsonException) {
                    // If parsing fails, use the raw errorData
                    errorMsg = errorData;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                errorMsg = "Error parsing error message";
            }
        } else if (error.getMessage() != null) {
            errorMsg = error.getMessage();
        }
        return errorMsg;
    }
}