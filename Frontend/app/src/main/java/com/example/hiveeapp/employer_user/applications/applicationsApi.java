package com.example.hiveeapp.employer_user.applications;

import android.content.Context;
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


public class applicationsApi {

    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/applications?jobPostingId=16&status=PENDING";
    private static final String ACCEPT_URL = "http://coms-3090-063.class.las.iastate.edu:8080/applications/";
    private static final String REJECT_URL = "http://coms-3090-063.class.las.iastate.edu:8080/applications/";
    private static final String TAG = "applicationsApi";

    // Method to set up headers including authorization
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

    public static void getApplications(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL;  // The URL for fetching applications
        Log.d(TAG, "GET Applications Request URL: " + url);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    // Process each application and return the response with only application details (without student info)
                    listener.onResponse(response);
                },
                error -> handleErrorResponse("Error fetching applications", error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return applicationsApi.getHeaders(context);
            }
        };

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Method to fetch student details
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

    // Method to reject an application
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

    // Method to delete an employer (if needed)
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
                return applicationsApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Helper method to handle error responses
    private static void handleErrorResponse(String errorMessagePrefix, VolleyError error, Response.ErrorListener errorListener) {
        String errorMsg = getErrorMessage(error);
        String fullErrorMessage = errorMessagePrefix + ": " + errorMsg;
        Log.e(TAG, fullErrorMessage);
        errorListener.onErrorResponse(new VolleyError(fullErrorMessage));
    }

    // Helper method to parse error messages
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