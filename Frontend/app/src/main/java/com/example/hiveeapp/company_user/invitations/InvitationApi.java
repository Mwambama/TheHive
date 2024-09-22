package com.example.hiveeapp.company_user.invitations;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class InvitationApi {

    private static final String BASE_URL = "https://39105a8a-a3d7-41a3-b90b-9e4031e56567.mock.pstmn.io/invitations/";

    // Method to send an invitation
    public static void sendInvitation(Context context, int companyId, String email,
                                      Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errorListener) {

        // Create the JSON payload for the invitation
        JSONObject invitationPayload = new JSONObject();
        try {
            invitationPayload.put("company_id", companyId);
            invitationPayload.put("email", email);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Define the URL for sending the invitation
        String invitationUrl = BASE_URL + "send";

        // Create a JsonObjectRequest to send the invitation
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                invitationUrl,
                invitationPayload,
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

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Method to delete an invitation
    public static void deleteInvitation(Context context, int invitationId,
                                        Response.Listener<JSONObject> responseListener,
                                        Response.ErrorListener errorListener) {

        // Define the URL for deleting the invitation
        String deleteUrl = BASE_URL + "delete/" + invitationId;

        // Create a JsonObjectRequest to delete the invitation
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                deleteUrl,
                null,
                responseListener,
                errorListener
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
                                      Response.Listener<JSONArray> responseListener,
                                      Response.ErrorListener errorListener) {

        // Define the URL for retrieving invitations
        String getUrl = BASE_URL + "get";

        // Create a JsonArrayRequest to get the invitations
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                getUrl,
                null,
                responseListener,
                errorListener
        );

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Method to update an invitation
    public static void updateInvitation(Context context, int invitationId, String newEmail,
                                        Response.Listener<JSONObject> responseListener,
                                        Response.ErrorListener errorListener) {

        // Create the JSON payload for the updated invitation
        JSONObject updatePayload = new JSONObject();
        try {
            updatePayload.put("email", newEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Define the URL for updating the invitation
        String updateUrl = BASE_URL + "update/" + invitationId;

        // Create a JsonObjectRequest to update the invitation
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                updateUrl,
                updatePayload,
                responseListener,
                errorListener
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