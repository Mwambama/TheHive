package com.example.hiveeapp.company_user.invitations;

import android.content.Context;
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

public class InvitationApi {

    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/employer-invitation";
    private static final String TAG = "InvitationApi";

    // Helper method to get headers with authorization for the currently logged-in user
    private static Map<String, String> getHeaders(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // Mock username and password for authentication
        String username = "test@example.com";
        String password = "Test@example1234";
        String credentials = username + ":" + password;
        String auth = "Basic " + android.util.Base64.encodeToString(credentials.getBytes(), android.util.Base64.NO_WRAP);

        headers.put("Authorization", auth);
        return headers;
    }

    // Method to send an invitation (Create)
    public static void sendInvitation(Context context, int companyId, String email,
                                      String message,
                                      Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errorListener) {

        // Create new invitation object
        JSONObject newInvitation = new JSONObject();
        try {
            // Creating a mock company structure inside the invitation
            JSONObject company = new JSONObject();
            company.put("userId", companyId); // Setting company ID
            company.put("name", "Test Company"); // Adjust this accordingly
            company.put("email", "testcompany@example.com"); // Adjust this accordingly
            company.put("role", "COMPANY");

            // Creating an address object
            JSONObject address = new JSONObject();
            address.put("addressId", JSONObject.NULL); // Set to NULL for a new address
            address.put("street", "5 Debs Hill");
            address.put("complement", "Apt 563");
            address.put("city", "Newton");
            address.put("state", "MA");
            address.put("zipCode", "02458");
            company.put("address", address); // Attaching address to company

            // Add company and email to the invitation
            newInvitation.put("company", company);
            newInvitation.put("email", email);
            newInvitation.put("message", message);

        } catch (JSONException e) {
            Log.e(TAG, "Error creating invitation: " + e.getMessage());
            errorListener.onErrorResponse(new VolleyError(e.getMessage()));
            return;
        }

        // Define the URL for sending the invitation
        String invitationUrl = BASE_URL;

        // Create a JsonObjectRequest to send the invitation
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                invitationUrl,
                newInvitation,
                listener,
                error -> {
                    Log.e(TAG, "Error sending invitation to server: " + error.getMessage());
                    errorListener.onErrorResponse(error);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return InvitationApi.getHeaders(context);
            }
        };

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Method to delete an invitation (Delete)
    public static void deleteInvitation(Context context, int invitationId,
                                        Response.Listener<JSONObject> listener,
                                        Response.ErrorListener errorListener) {

        // Define the URL for deleting the invitation
        String deleteUrl = BASE_URL + "/" + invitationId;

        // Create a JsonObjectRequest to delete the invitation
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                deleteUrl,
                null,
                listener,
                error -> {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                        Log.e(TAG, "Error: Invitation not found on server (404)");
                    } else {
                        Log.e(TAG, "Error deleting invitation on server: " + error.getMessage());
                    }
                    errorListener.onErrorResponse(error);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return InvitationApi.getHeaders(context);
            }
        };

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Method to get invitations (Read)
    public static void getInvitations(Context context,
                                      Response.Listener<JSONArray> listener,
                                      Response.ErrorListener errorListener) {

        // Define the URL for retrieving invitations
        String url = BASE_URL;

        // Create a JsonArrayRequest to fetch the invitations
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                listener,
                error -> {
                    Log.e(TAG, "Error fetching invitations: " + error.getMessage());
                    errorListener.onErrorResponse(error);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return InvitationApi.getHeaders(context);
            }
        };

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Method to update an invitation (Update)
    public static void updateInvitation(Context context, int invitationId, String newEmail, String newMessage,
                                        Response.Listener<JSONObject> listener,
                                        Response.ErrorListener errorListener) {

        JSONObject updatedInvitation = new JSONObject();

        try {
            // Create updated invitation object
            updatedInvitation.put("email", newEmail);
            updatedInvitation.put("message", newMessage);

            // Define the URL for updating the invitation
            String updateUrl = BASE_URL + "/" + invitationId;

            // Create a JsonObjectRequest to update the invitation
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT,
                    updateUrl,
                    updatedInvitation,
                    listener,
                    error -> {
                        Log.e(TAG, "Error updating invitation on server: " + error.getMessage());
                        errorListener.onErrorResponse(error);
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    return InvitationApi.getHeaders(context);
                }
            };

            // Add the request to the Volley request queue
            VolleySingleton.getInstance(context).addToRequestQueue(request);

        } catch (JSONException e) {
            Log.e(TAG, "Error updating invitation: " + e.getMessage());
            errorListener.onErrorResponse(new VolleyError(e.getMessage()));
        }
    }
}