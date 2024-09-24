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

    private static final String BASE_URL = "https://2e7fd141-9a29-4eae-ac7f-4996f5e64e96.mock.pstmn.io/invitations/";
    private static final String INVITATIONS_FILE = "invitations.json";

    // Helper method to read invitations from file
    private static JSONArray readInvitationsFromFile(Context context) {
        try {
            FileInputStream fis = context.openFileInput(INVITATIONS_FILE);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            String jsonString = new String(data, "UTF-8");
            Log.d("FileRead", "Invitations loaded from " + INVITATIONS_FILE);
            return new JSONArray(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
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
            Log.d("FileWrite", "Invitations saved to " + INVITATIONS_FILE);
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            }

        } catch (Exception e) {
            e.printStackTrace();
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
                    // On success, you can update any additional data if needed
                    listener.onResponse(response);
                },
                error -> {
                    // Even if there is an error, we have already deleted the invitation locally
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

        // Read invitations from local file
        try {
            JSONArray invitations = readInvitationsFromFile(context);

            // Call the success listener
            listener.onResponse(invitations);

        } catch (Exception e) {
            e.printStackTrace();
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
                errorListener.onErrorResponse(new VolleyError("Invitation not found"));
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
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
                    // On success, you can update any additional data if needed
                    listener.onResponse(response);
                },
                error -> {
                    // Even if there is an error, we have already updated the invitation locally
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