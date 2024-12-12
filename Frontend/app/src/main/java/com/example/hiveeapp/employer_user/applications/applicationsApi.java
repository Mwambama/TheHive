package com.example.hiveeapp.employer_user.applications;

import static com.example.hiveeapp.employer_user.applications.ApplicationListActivity.USER_PREFS;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

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

/**
 * The applicationsApi class provides methods for managing job applications,
 * including fetching applications, student details, accepting, rejecting, and deleting applications.
 */
public class applicationsApi {

    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/applications?jobPostingId=1008&status=PENDING";
    private static final String ACCEPT_URL = "http://coms-3090-063.class.las.iastate.edu:8080/applications/";
    private static final String REJECT_URL = "http://coms-3090-063.class.las.iastate.edu:8080/applications/";
    private static final String TAG = "applicationsApi";

    /**
     * Sets up headers including authorization for API requests.
     *
     * @param context The application context.
     * @return A map of headers including content type and authorization credentials.
     */
    public static Map<String, String> getHeaders(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        String username = "iiik@gmail.com";
        String password = "Anondwdb##444fedo";

        String credentials = username + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        return headers;
    }

    /**
     * Retrieves a list of job postings for a specific employer from the server.
     *
     * @param context       The application context.
     * @param listener      Response listener for successful fetch.
     * @param errorListener Error listener for handling errors.
     */
    public static void getJobPostings(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        // Retrieve employer ID from SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        int employerId = preferences.getInt("userId", -1);

        if (employerId == -1) {
            Log.e(TAG, "Employer ID not found in SharedPreferences.");
            errorListener.onErrorResponse(new VolleyError("Employer ID not found. Please log in again."));
            return;
        }
        // Construct the URL dynamically
        String url = BASE_URL + employerId;
        Log.d(TAG, "GET Job Postings Request URL: " + url);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                listener,
                error -> handleErrorResponse("Error fetching job postings", error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return applicationsApi.getHeaders(context);
            }
        };

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Retrieves a list of applications from the server.
     *
     * @param context       The application context.
     * @param listener      Response listener for successful fetch.
     * @param errorListener Error listener for handling errors.
     */
    public static void getApplications(Context context, String apiUrl, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        Log.d(TAG, "GET Applications Request URL: " + apiUrl);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                null,
                listener,
                error -> handleErrorResponse("Error fetching applications", error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return applicationsApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Fetches student details for a given student ID.
     *
     * @param context       The application context.
     * @param studentId     The ID of the student.
     * @param listener      Response listener for successful fetch.
     * @param errorListener Error listener for handling errors.
     */
    public static void getStudentDetails(Context context, long studentId, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/student/" + studentId;
        Log.d(TAG, "GET Student Details Request URL: " + url);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.d(TAG, "Student Details Response: " + response.toString());
                    listener.onResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error in getStudentDetails request", error);
                    handleErrorResponse("Error fetching student details", error, errorListener);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return applicationsApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Accepts a job application.
     *
     * @param context       The application context.
     * @param applicationId The ID of the application to accept.
     * @param listener      Response listener for successful acceptance.
     * @param errorListener Error listener for handling errors.
     */
    // Method to accept an application
    public static void AcceptApplication(Context context, long applicationId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = ACCEPT_URL + applicationId + "/accept";
        Log.d(TAG, "POST Application Accept Request URL: " + url);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                listener,
                error -> handleErrorResponse("Error accepting application", error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return applicationsApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Rejects a job application.
     *
     * @param context       The application context.
     * @param applicationId The ID of the application to reject.
     * @param listener      Response listener for successful rejection.
     * @param errorListener Error listener for handling errors.
     */
    public static void RejectApplication(Context context, long applicationId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = REJECT_URL + applicationId + "/reject";
        Log.d(TAG, "POST Application Reject Request URL: " + url);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                listener,
                error -> handleErrorResponse("Error rejecting application", error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return applicationsApi.getHeaders(context);
            }
        };

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
                try {
                    JSONObject jsonError = new JSONObject(errorData);
                    errorMsg = jsonError.optString("message", errorMsg);
                } catch (JSONException jsonException) {
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