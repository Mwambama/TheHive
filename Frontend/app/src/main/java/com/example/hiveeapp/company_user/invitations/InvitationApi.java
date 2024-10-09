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
    private static final int COMPANY_ID = 1029; // Hardcoded companyId matching EmployerApi


    /**
     * Helper method to get headers with authorization for the currently logged-in user.
     *
     * @param context The application context.
     * @return A map containing the headers for the API request.
     */
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

    /**
     * Sends an employer invitation to a specified email.
     *
     * @param context       The application context.
     * @param email         The email address to send the invitation to.
     * @param message       The message to include with the invitation.
     * @param listener      Response listener for successful invitation creation.
     * @param errorListener Error listener for handling errors.
     */
    public static void sendInvitation(Context context, String email, String message,
                                      Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errorListener) {

        // Create new invitation object
        JSONObject newInvitation = new JSONObject();
        try {
            // Create company object with userId
            JSONObject company = new JSONObject();
            company.put("userId", COMPANY_ID);

            // Include company object in the invitation JSON
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
        Log.d(TAG, "POST Invitation Request URL: " + invitationUrl);
        Log.d(TAG, "Invitation Data Payload: " + newInvitation.toString());

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