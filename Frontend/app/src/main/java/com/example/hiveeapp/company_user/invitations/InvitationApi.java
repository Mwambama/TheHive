package com.example.hiveeapp.company_user.invitations;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
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

import java.util.HashMap;
import java.util.Map;

public class InvitationApi {

    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/employer-invitation";
    private static final String TAG = "InvitationApi";
    private static final int COMPANY_ID = 1029; // Update as needed

    /**
     * Helper method to get headers with authorization for the currently logged-in user.
     *
     * @param context The application context.
     * @return A map containing the headers for the API request.
     */
    private static Map<String, String> getHeaders(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // Replace these with actual user credentials or fetch from SharedPreferences
        String username = "test@example.com";
        String password = "Test@example1234";

        String credentials = username + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        return headers;
    }

    /**
     * Retrieves a list of invitations from the server.
     *
     * @param context       The application context.
     * @param listener      Response listener for successful fetch.
     * @param errorListener Error listener for handling errors.
     */
    public static void getInvitations(Context context,
                                      Response.Listener<JSONArray> listener,
                                      Response.ErrorListener errorListener) {

        // Define the URL for retrieving invitations
        String url = BASE_URL;

        Log.d(TAG, "GET Invitations Request URL: " + url);

        // Create a JsonArrayRequest to fetch the invitations
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.d(TAG, "Invitations Response: " + response.toString()); // Log the response
                    listener.onResponse(response);
                },
                error -> {
                    String errorMsg = getErrorMessage(error);
                    Log.e(TAG, "Error fetching invitations: " + errorMsg);
                    errorListener.onErrorResponse(new VolleyError(errorMsg));
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

    /**
     * Deletes an invitation from the server.
     *
     * @param context       The application context.
     * @param invitationId  ID of the invitation to be deleted.
     * @param listener      Response listener for successful invitation deletion.
     * @param errorListener Error listener for handling errors.
     */
    public static void deleteInvitation(Context context, int invitationId,
                                        Response.Listener<String> listener,
                                        Response.ErrorListener errorListener) {

        // Define the URL for deleting the invitation
        String deleteUrl = BASE_URL + "/" + invitationId;

        Log.d(TAG, "DELETE Invitation Request URL: " + deleteUrl);

        // Create a StringRequest to delete the invitation
        StringRequest request = new StringRequest(
                Request.Method.DELETE,
                deleteUrl,
                response -> {
                    // Log success and pass the string response to the listener
                    Log.d(TAG, "Invitation deleted successfully: " + response);
                    listener.onResponse(response);
                },
                error -> {
                    // Handle error by extracting a meaningful error message
                    String errorMsg = getErrorMessage(error);
                    Log.e(TAG, "Error deleting invitation on server: " + errorMsg);
                    errorListener.onErrorResponse(new VolleyError(errorMsg));
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


    /**
     * Updates an existing invitation on the server.
     *
     * @param context       The application context.
     * @param invitationId  ID of the invitation to be updated.
     * @param newEmail      New email address.
     * @param newMessage    New message content.
     * @param listener      Response listener for successful invitation update.
     * @param errorListener Error listener for handling errors.
     */
    public static void updateInvitation(Context context, int invitationId, String newEmail, String newMessage,
                                        Response.Listener<JSONObject> listener,
                                        Response.ErrorListener errorListener) {

        JSONObject updatedInvitation = new JSONObject();

        try {
            updatedInvitation.put("employerInvitationId", invitationId);
            // Include 'companyId' directly at the root level
            updatedInvitation.put("companyId", COMPANY_ID); // COMPANY_ID should be 1029
            updatedInvitation.put("email", newEmail);
            updatedInvitation.put("message", newMessage); // Include 'message' if supported

            String updateUrl = BASE_URL;

            Log.d(TAG, "PUT Invitation Request URL: " + updateUrl);
            Log.d(TAG, "Updated Invitation Data Payload: " + updatedInvitation.toString());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT,
                    updateUrl,
                    updatedInvitation,
                    listener,
                    error -> {
                        String errorMsg = getErrorMessage(error);
                        Log.e(TAG, "Error updating invitation on server: " + errorMsg);
                        errorListener.onErrorResponse(new VolleyError(errorMsg));
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    return InvitationApi.getHeaders(context);
                }
            };

            VolleySingleton.getInstance(context).addToRequestQueue(request);

        } catch (JSONException e) {
            Log.e(TAG, "Error updating invitation: " + e.getMessage());
            errorListener.onErrorResponse(new VolleyError(e.getMessage()));
        }
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

        JSONObject newInvitation = new JSONObject();
        try {
            // Include 'companyId' directly at the root level
            newInvitation.put("companyId", COMPANY_ID); // COMPANY_ID should be 1029
            newInvitation.put("email", email);
            newInvitation.put("message", message); // Include 'message' if supported

        } catch (JSONException e) {
            Log.e(TAG, "Error creating invitation: " + e.getMessage());
            errorListener.onErrorResponse(new VolleyError(e.getMessage()));
            return;
        }

        String invitationUrl = BASE_URL;
        Log.d(TAG, "POST Invitation Request URL: " + invitationUrl);
        Log.d(TAG, "Invitation Data Payload: " + newInvitation.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                invitationUrl,
                newInvitation,
                listener,
                error -> {
                    String errorMsg = getErrorMessage(error);
                    Log.e(TAG, "Error sending invitation to server: " + errorMsg);
                    errorListener.onErrorResponse(new VolleyError(errorMsg));
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return InvitationApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }


    /**
     * Extracts and returns a meaningful error message from a VolleyError.
     *
     * @param error The VolleyError object containing the error details.
     * @return A user-friendly error message.
     */
    private static String getErrorMessage(VolleyError error) {
        String errorMsg = "An unexpected error occurred";
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String errorData = new String(error.networkResponse.data, "UTF-8");
                JSONObject jsonError = new JSONObject(errorData);
                errorMsg = jsonError.optString("message", errorMsg);
            } catch (Exception e) {
                e.printStackTrace();
                errorMsg = "Error parsing error message";
            }
        } else if (error.getMessage() != null) {
            errorMsg = error.getMessage();
        } else if (error instanceof AuthFailureError) {
            errorMsg = "Authentication failed. Please check your credentials.";
        }
        return errorMsg;
    }
}