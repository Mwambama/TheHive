package com.example.hiveeapp.company_user.invitations;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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

    private static final String BASE_URL = "https://83b448a1-640e-45cc-bf06-bbb5383d4f84.mock.pstmn.io/invitations/";
    private static final String INVITATIONS_FILE = "invitations.json";

    // Helper method to read invitations from file
    private static JSONArray readInvitationsFromFile(Context context) {
        try {
            FileInputStream fis = context.openFileInput(INVITATIONS_FILE);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            String jsonString = new String(data, "UTF-8");
            return new JSONArray(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            // If file does not exist or is empty, return an empty array
            return new JSONArray();
        }
    }

    // Helper method to write invitations to file
    private static void writeInvitationsToFile(Context context, JSONArray invitations) {
        try {
            String jsonString = invitations.toString();
            FileOutputStream fos = context.openFileOutput(INVITATIONS_FILE, Context.MODE_PRIVATE);
            fos.write(jsonString.getBytes("UTF-8"));
            fos.close();
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

    // Method to send an invitation
    public static void sendInvitation(Context context, int companyId, String email, String message,
                                      Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errorListener) {

        // Create the JSON payload for the invitation
        JSONObject invitationPayload = new JSONObject();
        try {
            invitationPayload.put("company_id", companyId);
            invitationPayload.put("email", email);
            invitationPayload.put("message", message);  // Include message
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Define the URL for sending the invitation
        String invitationUrl = BASE_URL + "send";

        // Create a JsonObjectRequest to send the invitation
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                invitationUrl,
                invitationPayload,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // On success, save the invitation locally
                        new Thread(() -> {
                            try {
                                JSONArray invitations = readInvitationsFromFile(context);

                                // Create new invitation object locally
                                JSONObject newInvitation = new JSONObject();
                                int newId = generateNewId(invitations);
                                newInvitation.put("id", newId);
                                newInvitation.put("company_id", companyId);
                                newInvitation.put("email", email);
                                newInvitation.put("message", message);

                                // Add to invitations array
                                invitations.put(newInvitation);

                                // Write back to file
                                writeInvitationsToFile(context, invitations);

                                // Call the success listener on the main thread
                                new Handler(Looper.getMainLooper()).post(() -> listener.onResponse(newInvitation));

                            } catch (Exception e) {
                                e.printStackTrace();
                                // Call error listener on main thread
                                new Handler(Looper.getMainLooper()).post(() ->
                                        errorListener.onErrorResponse(new VolleyError(e.getMessage()))
                                );
                            }
                        }).start();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Call error listener on main thread
                        new Handler(Looper.getMainLooper()).post(() -> errorListener.onErrorResponse(error));
                    }
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

    // Method to delete an invitation
    public static void deleteInvitation(Context context, int invitationId,
                                        Response.Listener<JSONObject> listener,
                                        Response.ErrorListener errorListener) {

        // Define the URL for deleting the invitation
        String deleteUrl = BASE_URL + "delete/" + invitationId;

        // Create a JsonObjectRequest to delete the invitation
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                deleteUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // On success, delete the invitation locally
                        new Thread(() -> {
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

                                // Call the success listener on the main thread
                                new Handler(Looper.getMainLooper()).post(() -> listener.onResponse(response));

                            } catch (Exception e) {
                                e.printStackTrace();
                                // Call error listener on main thread
                                new Handler(Looper.getMainLooper()).post(() ->
                                        errorListener.onErrorResponse(new VolleyError(e.getMessage()))
                                );
                            }
                        }).start();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Call error listener on main thread
                        new Handler(Looper.getMainLooper()).post(() -> errorListener.onErrorResponse(error));
                    }
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

    // Method to get invitations
    public static void getInvitations(Context context,
                                      Response.Listener<JSONArray> listener,
                                      Response.ErrorListener errorListener) {

        // Read invitations from local file
        new Thread(() -> {
            try {
                JSONArray invitations = readInvitationsFromFile(context);

                // Call the success listener on the main thread
                new Handler(Looper.getMainLooper()).post(() -> listener.onResponse(invitations));

            } catch (Exception e) {
                e.printStackTrace();
                // Call error listener on main thread
                new Handler(Looper.getMainLooper()).post(() ->
                        errorListener.onErrorResponse(new VolleyError(e.getMessage()))
                );
            }
        }).start();
    }

    // Method to update an invitation
    public static void updateInvitation(Context context, int invitationId, String newEmail,
                                        Response.Listener<JSONObject> listener,
                                        Response.ErrorListener errorListener) {

        // Create the JSON payload for the updated invitation
        JSONObject updatePayload = new JSONObject();
        try {
            updatePayload.put("email", newEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Define the URL for updating the invitation
        String updateUrl = BASE_URL + "update/" + invitationId;

        // Create a JsonObjectRequest to update the invitation
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                updateUrl,
                updatePayload,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // On success, update the invitation locally
                        new Thread(() -> {
                            try {
                                JSONArray invitations = readInvitationsFromFile(context);

                                // Find the invitation with the given id
                                boolean found = false;
                                for (int i = 0; i < invitations.length(); i++) {
                                    JSONObject invitation = invitations.getJSONObject(i);
                                    if (invitation.getInt("id") == invitationId) {
                                        // Update the email
                                        invitation.put("email", newEmail);
                                        found = true;
                                        break;
                                    }
                                }

                                if (found) {
                                    // Write back to file
                                    writeInvitationsToFile(context, invitations);
                                }

                                // Call the success listener on the main thread
                                new Handler(Looper.getMainLooper()).post(() -> listener.onResponse(response));

                            } catch (Exception e) {
                                e.printStackTrace();
                                // Call error listener on main thread
                                new Handler(Looper.getMainLooper()).post(() ->
                                        errorListener.onErrorResponse(new VolleyError(e.getMessage()))
                                );
                            }
                        }).start();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Call error listener on main thread
                        new Handler(Looper.getMainLooper()).post(() -> errorListener.onErrorResponse(error));
                    }
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