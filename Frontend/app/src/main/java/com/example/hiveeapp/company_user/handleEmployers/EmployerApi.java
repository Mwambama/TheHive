package com.example.hiveeapp.company_user.handleEmployers;

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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class EmployerApi {

    private static final String BASE_URL = "";
    private static final String EMPLOYERS_FILE = "employers.json";

    // Helper method to read employers from file
    private static JSONArray readEmployersFromFile(Context context) {
        try {
            FileInputStream fis = context.openFileInput(EMPLOYERS_FILE);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            String jsonString = new String(data, "UTF-8");
            Log.d("FileRead", "Employers loaded from " + EMPLOYERS_FILE);
            return new JSONArray(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();  // Return empty array if file does not exist
        }
    }

    // Helper method to write employers to file
    private static void writeEmployersToFile(Context context, JSONArray employers) {
        try {
            String jsonString = employers.toString();
            FileOutputStream fos = context.openFileOutput(EMPLOYERS_FILE, Context.MODE_PRIVATE);
            fos.write(jsonString.getBytes("UTF-8"));
            fos.close();
            Log.d("FileWrite", "Employers saved to " + EMPLOYERS_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to generate a new ID
    private static int generateNewId(JSONArray employers) {
        int newId = 1;
        try {
            for (int i = 0; i < employers.length(); i++) {
                JSONObject employer = employers.getJSONObject(i);
                int id = employer.getInt("id");
                if (id >= newId) {
                    newId = id + 1;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newId;
    }

    // Get Employers (READ)
    public static void getEmployers(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        // Read employers from local file
        JSONArray localEmployers = readEmployersFromFile(context);
        listener.onResponse(localEmployers);

        // Optionally, you can also fetch from the server and update the local file
        String url = BASE_URL + "get_all";
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    // Store the server data locally
                    writeEmployersToFile(context, response);
                    listener.onResponse(response);
                },
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
        // Create the employer JSON object
        JSONObject employer = new JSONObject();
        try {
            JSONArray employers = readEmployersFromFile(context);
            int newId = generateNewId(employers);

            employer.put("id", newId);
            employer.put("name", name);
            employer.put("email", email);
            employer.put("phone", phone);

            JSONObject address = new JSONObject();
            address.put("street", street);
            address.put("city", city);
            address.put("state", state);
            address.put("zip_code", zip);

            employer.put("address", address);

            // Add to employers array
            employers.put(employer);

            // Write back to file
            writeEmployersToFile(context, employers);

        } catch (JSONException e) {
            e.printStackTrace();
            errorListener.onErrorResponse(new VolleyError(e.getMessage()));
            return;
        }

        //Sync with the server
        String url = BASE_URL + "add";
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
        JSONObject updatedEmployer = null;
        try {
            JSONArray employers = readEmployersFromFile(context);

            // Find the employer with the given ID
            boolean found = false;
            for (int i = 0; i < employers.length(); i++) {
                JSONObject employer = employers.getJSONObject(i);
                if (employer.getInt("id") == employerId) {
                    // Update the employer details
                    employer.put("name", name);
                    employer.put("email", email);
                    employer.put("phone", phone);

                    JSONObject address = new JSONObject();
                    address.put("street", street);
                    address.put("city", city);
                    address.put("state", state);
                    address.put("zip_code", zip);
                    employer.put("address", address);

                    updatedEmployer = employer;
                    found = true;
                    break;
                }
            }

            if (found) {
                // Write updated employers back to file
                writeEmployersToFile(context, employers);
            } else {
                errorListener.onErrorResponse(new VolleyError("Employer not found"));
                return;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            errorListener.onErrorResponse(new VolleyError(e.getMessage()));
            return;
        }

        // Optionally, sync with the server
        String url = BASE_URL + "update/" + employerId;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                updatedEmployer,
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
        try {
            JSONArray employers = readEmployersFromFile(context);

            // Find and remove the employer with the given ID
            boolean found = false;
            for (int i = 0; i < employers.length(); i++) {
                JSONObject employer = employers.getJSONObject(i);
                if (employer.getInt("id") == employerId) {
                    employers.remove(i);
                    found = true;
                    break;
                }
            }

            if (found) {
                // Write updated employers back to file
                writeEmployersToFile(context, employers);
            } else {
                errorListener.onErrorResponse(new VolleyError("Employer not found"));
                return;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            errorListener.onErrorResponse(new VolleyError(e.getMessage()));
            return;
        }

        //Sync with the server
        String url = BASE_URL + "delete/" + employerId;
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