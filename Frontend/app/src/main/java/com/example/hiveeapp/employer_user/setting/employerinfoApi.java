package com.example.hiveeapp.employer_user.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class employerinfoApi {

    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/employer";
    private static final String TAG = "EmployerApi";
    private static final String AUTH_PREFERENCES = "UserPreferences";

    /**
     * Generates the headers for API requests with authorization.
     *
     * @param context The application context used to retrieve user credentials.
     * @return A map of headers including content type and authorization credentials.
     */
    public static Map<String, String> getHeaders(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        SharedPreferences preferences = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        String username = preferences.getString("email", "");
        String password = preferences.getString("password", "");

        if (!username.isEmpty() && !password.isEmpty()) {
            String credentials = username + ":" + password;
            String auth = "Basic " + Base64.encodeToString(credentials.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
            headers.put("Authorization", auth);
        } else {
            Log.e(TAG, "Missing username or password in SharedPreferences.");
        }

        return headers;
    }

    /**
     * Retrieves employer profile from the server.
     *
     * @param context       The application context.
     * @param userId        The ID of the user (employer).
     * @param listener      Response listener for successful fetch.
     * @param errorListener Error listener for handling errors.
     */
    public static void getEmployer(Context context, int userId, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/" + userId;
        Log.d(TAG, "GET employer Request URL: " + url);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                listener,
                error -> handleErrorResponse("Error fetching employer", error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return employerinfoApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Updates an existing employer profile.
     *
     * @param context       The application context.
     * @param employerData  JSON object containing employer details.
     * @param listener      Response listener for successful employer update.
     * @param errorListener Error listener for handling errors.
     */
    public static void updateEmployer(Context context, JSONObject employerData, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        SharedPreferences preferences = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        int companyId = preferences.getInt("companyId", -1); // Fetch the stored companyId, default to -1 if not found

        if (companyId == -1) {
            Log.e(TAG, "Invalid companyId. Ensure it is saved during login.");
            listener.onResponse(new JSONObject()); // Notify listener with an empty response
            return;
        }

        try {
            employerData.put("companyId", companyId); // Add companyId dynamically to the payload
        } catch (JSONException e) {
            Log.e(TAG, "Error adding companyId to employerData", e);
        }

        String url = BASE_URL;
        Log.d(TAG, "PUT employer Request URL: " + url);
        Log.d(TAG, "Employer Data Payload: " + employerData.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                employerData,
                listener,
                error -> handleErrorResponse("Error updating employer", error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/hal+json");
                headers.putAll(employerinfoApi.getHeaders(context));
                return headers;
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
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String errorData = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                Log.e(TAG, "Server response: " + errorData);
            } catch (Exception e) {
                Log.e(TAG, "Error parsing server response", e);
            }
        }
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
                String errorData = new String(error.networkResponse.data, StandardCharsets.UTF_8);

                // Attempt to parse errorData as JSON
                try {
                    JSONObject jsonError = new JSONObject(errorData);
                    errorMsg = jsonError.optString("message", errorMsg);
                } catch (JSONException jsonException) {
                    // If parsing fails, use the raw errorData
                    errorMsg = errorData;
                }

            } catch (Exception e) {
                Log.e(TAG, "Error parsing error message", e);
                errorMsg = "Error parsing error message";
            }
        } else if (error.getMessage() != null) {
            errorMsg = error.getMessage();
        }
        return errorMsg;
    }
}
