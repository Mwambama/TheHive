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
import java.util.HashMap;
import java.util.Map;

public class EmployerApi {

    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/employer-invitation";
    private static final String TAG = "EmployerApi";
    private static final String USER_PREFS = "UserPrefs"; // Shared preferences key

    // Helper method to get headers with authorization for the currently logged-in user
    private static Map<String, String> getHeaders(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // Retrieve username and password from SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        String username = preferences.getString("username", null); // Retrieve username
        String password = preferences.getString("password", null); // Retrieve password

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
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                listener,  // Directly passing the response to the listener
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return EmployerApi.getHeaders(context); // Include the authorization header
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Add Employer (CREATE)
    public static void addEmployer(Context context, String name, String email, String phone, String street, String complement, String city, String state, String zipCode,
                                   Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        // Create the employer JSON object
        JSONObject employer = new JSONObject();
        try {
            employer.put("name", name);
            employer.put("email", email);
            employer.put("phone", phone);
            employer.put("role", "EMPLOYER");  // Adjust role if necessary

            JSONObject address = new JSONObject();
            address.put("street", street);
            address.put("complement", complement);  // New field: complement
            address.put("city", city);
            address.put("state", state);
            address.put("zipCode", zipCode);

            employer.put("address", address);

        } catch (JSONException e) {
            Log.e(TAG, "Error creating employer: " + e.getMessage());
            errorListener.onErrorResponse(new VolleyError("Invalid employer data"));
            return;
        }

        // Send the employer data to the server
        String url = BASE_URL;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                employer,
                listener,
                error -> {
                    Log.e(TAG, "Error adding employer to server: " + error.getMessage());
                    errorListener.onErrorResponse(error);
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
        String url = BASE_URL;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                employerData,
                listener,
                error -> {
                    Log.e(TAG, "Error updating employer on server: " + error.getMessage());
                    errorListener.onErrorResponse(error);
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
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                listener,
                error -> {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                        Log.e(TAG, "Error: Employer not found on server (404)");
                    } else {
                        Log.e(TAG, "Error deleting employer on server: " + error.getMessage());
                    }
                    errorListener.onErrorResponse(error);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return EmployerApi.getHeaders(context); // Include the authorization header
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}