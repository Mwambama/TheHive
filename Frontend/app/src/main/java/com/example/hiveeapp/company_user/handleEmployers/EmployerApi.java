package com.example.hiveeapp.company_user.handleEmployers;

import android.content.Context;
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

    private static final String BASE_URL = "";

    // Get Employers (READ)
    public static void getEmployers(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "get_all";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                listener,
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add request to queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Add Employer (CREATE)
    public static void addEmployer(Context context, String name, String email, String phone, String street, String city, String state, String zip,
                                   Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "add";

        // Create the employer JSON object
        JSONObject employer = new JSONObject();
        try {
            employer.put("name", name);
            employer.put("email", email);
            employer.put("phone", phone);

            JSONObject address = new JSONObject();
            address.put("street", street);
            address.put("city", city);
            address.put("state", state);
            address.put("zip_code", zip);

            employer.put("address", address);

        } catch (JSONException e) {
            e.printStackTrace();
            errorListener.onErrorResponse(new VolleyError(e.getMessage()));
            return;
        }

        // Create the request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                employer,
                listener,
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add request to queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Update Employer (UPDATE)
    public static void updateEmployer(Context context, int employerId, String name, String email, String phone, String street, String city, String state, String zip,
                                      Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "update/" + employerId;

        // Create the updated employer JSON object
        JSONObject employer = new JSONObject();
        try {
            employer.put("name", name);
            employer.put("email", email);
            employer.put("phone", phone);

            JSONObject address = new JSONObject();
            address.put("street", street);
            address.put("city", city);
            address.put("state", state);
            address.put("zip_code", zip);

            employer.put("address", address);

        } catch (JSONException e) {
            e.printStackTrace();
            errorListener.onErrorResponse(new VolleyError(e.getMessage()));
            return;
        }

        // Create the request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                employer,
                listener,
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add request to queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Delete Employer (DELETE)
    public static void deleteEmployer(Context context, int employerId, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "delete/" + employerId;

        // Create the request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                listener,
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add request to queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}