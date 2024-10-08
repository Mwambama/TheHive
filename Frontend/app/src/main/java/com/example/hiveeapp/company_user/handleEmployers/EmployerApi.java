package com.example.hiveeapp.company_user.handleEmployers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class EmployerApi {

    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/employer";
    private static final String TAG = "EmployerApi";
    private static final String USER_PREFS = "UserPrefs"; // Shared preferences key

    // Flag to enable testing mode
    private static final boolean TESTING_MODE = true;

    // Helper method to get headers with authorization for the currently logged-in user
    private static Map<String, String> getHeaders(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        String username = null;
        String password = null;

        if (TESTING_MODE) {
            // For testing purposes, use hardcoded credentials
            username = "employer@example.com";
            password = "Test@1234";
        } else {
            // Retrieve username and password from SharedPreferences
            SharedPreferences preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
            username = preferences.getString("username", null); // Retrieve username
            password = preferences.getString("password", null); // Retrieve password
        }

        if (username != null && password != null) {
            String credentials = username + ":" + password;
            String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            headers.put("Authorization", auth);
        }

        return headers;
    }

    // Get Employers (READ)
    public static void getEmployers(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL;

        Log.d(TAG, "GET Employers Request URL: " + url);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    // Log the response for debugging
                    Log.d(TAG, "Response: " + response.toString());
                    listener.onResponse(response);
                },
                error -> {
                    errorListener.onErrorResponse(new VolleyError(getErrorMessage(error)));
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return EmployerApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Add Employer (CREATE)
    public static void addEmployer(Context context, JSONObject employerData,
                                   Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL;

        Log.d(TAG, "POST Employer Request URL: " + url);
        Log.d(TAG, "Request Payload: " + employerData.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                employerData,
                listener,
                error -> {
                    errorListener.onErrorResponse(new VolleyError(getErrorMessage(error)));
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return EmployerApi.getHeaders(context); // Include the authorization header
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Update Employer (UPDATE)
    public static void updateEmployer(Context context, JSONObject employerData,
                                      Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        long employerId = employerData.optLong("userId", -1);
        if (employerId == -1) {
            Log.e(TAG, "Invalid employerId for update");
            if (errorListener != null) {
                errorListener.onErrorResponse(new VolleyError("Invalid employerId"));
            }
            return;
        }
        String url = BASE_URL + "/" + employerId;

        Log.d(TAG, "PUT Employer Request URL: " + url);
        Log.d(TAG, "Request Payload: " + employerData.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                employerData,
                listener,
                error -> {
                    errorListener.onErrorResponse(new VolleyError(getErrorMessage(error)));
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return EmployerApi.getHeaders(context); // Include the authorization header
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Delete Employer (DELETE)
    public static void deleteEmployer(Context context, long employerId,
                                      Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/" + employerId;

        Log.d(TAG, "DELETE Employer Request URL: " + url);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                listener,
                error -> {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                        Log.e(TAG, "Error: Employer not found on server (404)");
                    } else {
                        errorListener.onErrorResponse(new VolleyError(getErrorMessage(error)));
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return EmployerApi.getHeaders(context); // Include the authorization header
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Helper method to handle and extract meaningful error messages
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