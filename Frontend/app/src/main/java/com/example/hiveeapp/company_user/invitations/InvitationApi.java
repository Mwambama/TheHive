package com.example.hiveeapp.company_user.invitations;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

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

/**
 * InvitationApi handles all network requests related to invitations for employer users.
 * It includes functionality to send, update, retrieve, and delete invitations.
 */
public class InvitationApi {

    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/employer-invitation";
    private static final String TAG = "InvitationApi";

    /**
     * Generates the headers required for the API requests, including authorization.
     *
     * @param context The application context.
     * @return A map of headers with content type and authorization credentials.
     */
    private static Map<String, String> getHeaders(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        String userEmail = preferences.getString("email", "");
        String userPassword = preferences.getString("password", "");

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        if (!userEmail.isEmpty() && !userPassword.isEmpty()) {
            String credentials = userEmail + ":" + userPassword;
            String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            headers.put("Authorization", auth);
        } else {
            Log.e(TAG, "Error: Missing email or password for authorization.");
        }

        return headers;
    }

    /**
     * Fetches the company ID from SharedPreferences.
     *
     * @param context The application context.
     * @return The company ID or -1 if not found.
     */
    private static int getCompanyId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        return preferences.getInt("userId", -1);
    }

    /**
     * Fetches a list of employer invitations from the server.
     *
     * @param context       The application context.
     * @param listener      Listener for successful responses.
     * @param errorListener Listener for handling errors.
     */
    public static void getInvitations(Context context,
                                      Response.Listener<JSONArray> listener,
                                      Response.ErrorListener errorListener) {
        String url = BASE_URL;
        Log.d(TAG, "GET Invitations Request URL: " + url);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                listener,
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

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Deletes an invitation by its ID.
     *
     * @param context       The application context.
     * @param invitationId  ID of the invitation to be deleted.
     * @param listener      Listener for successful responses.
     * @param errorListener Listener for handling errors.
     */
    public static void deleteInvitation(Context context, int invitationId,
                                        Response.Listener<String> listener,
                                        Response.ErrorListener errorListener) {
        String deleteUrl = BASE_URL + "/" + invitationId;
        Log.d(TAG, "DELETE Invitation Request URL: " + deleteUrl);

        StringRequest request = new StringRequest(
                Request.Method.DELETE,
                deleteUrl,
                listener,
                error -> {
                    String errorMsg = getErrorMessage(error);
                    Log.e(TAG, "Error deleting invitation: " + errorMsg);
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
     * Updates an existing invitation.
     *
     * @param context       The application context.
     * @param invitationId  ID of the invitation to be updated.
     * @param newEmail      Updated email address.
     * @param newMessage    Updated message content.
     * @param listener      Listener for successful responses.
     * @param errorListener Listener for handling errors.
     */
    public static void updateInvitation(Context context, int invitationId, String newEmail, String newMessage,
                                        Response.Listener<JSONObject> listener,
                                        Response.ErrorListener errorListener) {
        JSONObject updatedInvitation = new JSONObject();
        int companyId = getCompanyId(context);

        if (companyId == -1) {
            Log.e(TAG, "Error: Company ID not found in SharedPreferences.");
            errorListener.onErrorResponse(new VolleyError("Invalid company ID."));
            return;
        }

        try {
            updatedInvitation.put("employerInvitationId", invitationId);
            updatedInvitation.put("companyId", companyId);
            updatedInvitation.put("email", newEmail);
            updatedInvitation.put("message", newMessage);

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
                        Log.e(TAG, "Error updating invitation: " + errorMsg);
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
     * Sends a new invitation.
     *
     * @param context       The application context.
     * @param email         Recipient email address.
     * @param message       Message to include with the invitation.
     * @param listener      Listener for successful responses.
     * @param errorListener Listener for handling errors.
     */
    public static void sendInvitation(Context context, String email, String message,
                                      Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errorListener) {
        JSONObject newInvitation = new JSONObject();
        int companyId = getCompanyId(context);

        if (companyId == -1) {
            Log.e(TAG, "Error: Company ID not found in SharedPreferences.");
            errorListener.onErrorResponse(new VolleyError("Invalid company ID."));
            return;
        }

        try {
            newInvitation.put("companyId", companyId);
            newInvitation.put("email", email);
            newInvitation.put("message", message);

            String invitationUrl = BASE_URL;
            Log.d(TAG, "POST Invitation Request URL: " + invitationUrl);
            Log.d(TAG, "Invitation Data Payload: " + newInvitation.toString());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    invitationUrl,
                    newInvitation,
                    response -> {
                        Log.d(TAG, "Invitation sent successfully: " + response.toString());

                        // Display success message
                        Toast.makeText(context, "Invitation sent successfully to " + email + "!", Toast.LENGTH_SHORT).show();

                        // Call the original listener for further actions
                        listener.onResponse(response);
                    },
                    error -> {
                        String errorMsg = getErrorMessage(error);
                        Log.e(TAG, "Error sending invitation: " + errorMsg);
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
            Log.e(TAG, "Error creating invitation: " + e.getMessage());
            errorListener.onErrorResponse(new VolleyError(e.getMessage()));
        }
    }

    /**
     * Extracts a meaningful error message from the given VolleyError.
     *
     * @param error The VolleyError object containing error details.
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
                Log.e(TAG, "Error parsing error message: " + e.getMessage());
            }
        } else if (error.getMessage() != null) {
            errorMsg = error.getMessage();
        } else if (error instanceof AuthFailureError) {
            errorMsg = "Authentication failed. Please check your credentials.";
        }
        return errorMsg;
    }
}
