package com.example.hiveeapp.company_user.invitations;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class InvitationApi {

    private static final String BASE_URL = "https://0426e89a-dc0e-4f75-8adb-c324dd58c2a8.mock.pstmn.io/";
    private static final String INVITATIONS_FILE = "invitations.json";
    private static final String TAG = "InvitationApi";

    // Helper method to read invitations from file
    private static JSONArray readInvitationsFromFile(Context context) {
        try {
            FileInputStream fis = context.openFileInput(INVITATIONS_FILE);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            String jsonString = new String(data, "UTF-8");
            Log.d(TAG, "Invitations loaded from " + INVITATIONS_FILE);
            return new JSONArray(jsonString);
        } catch (Exception e) {
            Log.e(TAG, "Error reading invitations from file: " + e.getMessage());
            return new JSONArray();  // Return empty array if file does not exist
        }
    }

    // Helper method to write invitations to file
    private static void writeInvitationsToFile(Context context, JSONArray invitations) {
        try {
            String jsonString = invitations.toString();
            FileOutputStream fos = context.openFileOutput(INVITATIONS_FILE, Context.MODE_PRIVATE);
            fos.write(jsonString.getBytes("UTF-8"));
            fos.close();
            Log.d(TAG, "Invitations saved to " + INVITATIONS_FILE);
        } catch (Exception e) {
            Log.e(TAG, "Error writing invitations to file: " + e.getMessage());
        }
    }

    // Helper method to generate a new ID
    private static int generateNewId(JSONArray invitations) {
        int newId = 1;
        try {
            for (int i = 0; i < invitations.length(); i++) {
                JSONObject invitation = invitations.getJSONObject(i);
                int id = invitation.getInt("id");
                if (id >= newId) {
                    newId = id + 1;
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error generating new ID: " + e.getMessage());
        }
        return newId;
    }

    // Method to send an invitation (Create)
    public static void sendInvitation(Context context, int companyId, String email, String message,
                                      Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errorListener) {

        // Create new invitation object locally
        JSONObject newInvitation = new JSONObject();
        try {
            JSONArray invitations = readInvitationsFromFile(context);
            int newId = generateNewId(invitations);
            newInvitation.put("id", newId);
            newInvitation.put("company_id", companyId);
            newInvitation.put("email", email);
            newInvitation.put("message", message);

            // Add to invitations array
            invitations.put(newInvitation);

            // Write back to file
            writeInvitationsToFile(context, invitations);

        } catch (Exception e) {
            Log.e(TAG, "Error creating invitation: " + e.getMessage());
            errorListener.onErrorResponse(new VolleyError(e.getMessage()));
            return;
        }

        // Define the URL for sending the invitation
        String invitationUrl = BASE_URL + "send";

        // Create a JsonObjectRequest to send the invitation
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                invitationUrl,
                newInvitation,
                response -> {
                    listener.onResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error sending invitation to server: " + error.getMessage());
                    errorListener.onErrorResponse(error);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Method to delete an invitation (Delete)
    public static void deleteInvitation(Context context, int invitationId,
                                        Response.Listener<JSONObject> listener,
                                        Response.ErrorListener errorListener) {

        try {
            JSONArray invitations = readInvitationsFromFile(context);

            // Find and remove invitation with the given id
            boolean found = false;
            for (int i = 0; i < invitations.length(); i++) {
                JSONObject invitation = invitations.getJSONObject(i);
                if (invitation.getInt("id") == invitationId) {
                    invitations.remove(i);
                    found = true;
                    break;
                }
            }

            if (found) {
                // Write back to file
                writeInvitationsToFile(context, invitations);
            } else {
                Log.e(TAG, "Error: Invitation not found with ID: " + invitationId);
                errorListener.onErrorResponse(new VolleyError("Invitation not found"));
                return;
            }

        } catch (Exception e) {
            Log.e(TAG, "Error deleting invitation: " + e.getMessage());
            errorListener.onErrorResponse(new VolleyError(e.getMessage()));
            return;
        }

        // Define the URL for deleting the invitation
        String deleteUrl = BASE_URL + "delete/" + invitationId;

        // Create a JsonObjectRequest to delete the invitation
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                deleteUrl,
                null,
                response -> {
                    listener.onResponse(response);
                },
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
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Method to get invitations (Read)
    public static void getInvitations(Context context,
                                      Response.Listener<JSONArray> listener,
                                      Response.ErrorListener errorListener) {

        try {
            // Read invitations from local file
            JSONArray invitations = readInvitationsFromFile(context);
            listener.onResponse(invitations);
        } catch (Exception e) {
            Log.e(TAG, "Error reading invitations: " + e.getMessage());
            errorListener.onErrorResponse(new VolleyError(e.getMessage()));
        }
    }

    // Method to update an invitation (Update)
    public static void updateInvitation(Context context, int invitationId, String newEmail, String newMessage,
                                        Response.Listener<JSONObject> listener,
                                        Response.ErrorListener errorListener) {

        JSONObject updatedInvitation = null;

        try {
            JSONArray invitations = readInvitationsFromFile(context);

            // Find the invitation with the given id
            boolean found = false;
            for (int i = 0; i < invitations.length(); i++) {
                JSONObject invitation = invitations.getJSONObject(i);
                if (invitation.getInt("id") == invitationId) {
                    // Update the email and message
                    invitation.put("email", newEmail);
                    invitation.put("message", newMessage);
                    updatedInvitation = invitation;
                    found = true;
                    break;
                }
            }

            if (found) {
                // Write back to file
                writeInvitationsToFile(context, invitations);
            } else {
                Log.e(TAG, "Error: Invitation not found with ID: " + invitationId);
                errorListener.onErrorResponse(new VolleyError("Invitation not found"));
                return;
            }

        } catch (Exception e) {
            Log.e(TAG, "Error updating invitation: " + e.getMessage());
            errorListener.onErrorResponse(new VolleyError(e.getMessage()));
            return;
        }

        // Define the URL for updating the invitation
        String updateUrl = BASE_URL + "update/" + invitationId;

        // Create a JsonObjectRequest to update the invitation
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                updateUrl,
                updatedInvitation,
                response -> {
                    listener.onResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error updating invitation on server: " + error.getMessage());
                    errorListener.onErrorResponse(error);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}