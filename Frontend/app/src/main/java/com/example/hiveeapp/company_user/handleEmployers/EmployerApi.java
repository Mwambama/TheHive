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

    private static final String BASE_URL = "https://0426e89a-dc0e-4f75-8adb-c324dd58c2a8.mock.pstmn.io/";
    private static final String EMPLOYERS_FILE = "employers.json";
    private static final String TAG = "EmployerApi";

    // Helper method to merge local and server data without duplicates
    private static JSONArray mergeEmployerData(JSONArray localData, JSONArray serverData) {
        JSONArray mergedArray = new JSONArray();

        // Copy all local data to the merged array
        for (int i = 0; i < localData.length(); i++) {
            mergedArray.put(localData.optJSONObject(i));
        }

        // Add server data to merged array, avoiding duplicates by checking IDs
        for (int i = 0; i < serverData.length(); i++) {
            JSONObject serverEmployer = serverData.optJSONObject(i);
            boolean isDuplicate = false;

            // Check for duplicates based on the "id" field
            for (int j = 0; j < localData.length(); j++) {
                JSONObject localEmployer = localData.optJSONObject(j);
                if (localEmployer != null && serverEmployer != null &&
                        localEmployer.optInt("id") == serverEmployer.optInt("id")) {
                    isDuplicate = true;
                    break;
                }
            }

            // If no duplicate, add to merged array
            if (!isDuplicate) {
                mergedArray.put(serverEmployer);
            }
        }

        return mergedArray;
    }

    // Helper method to read employers from file
    private static JSONArray readEmployersFromFile(Context context) {
        try {
            FileInputStream fis = context.openFileInput(EMPLOYERS_FILE);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            String jsonString = new String(data, "UTF-8");
            Log.d(TAG, "Employers loaded from " + EMPLOYERS_FILE);
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
            Log.d(TAG, "Employers saved to " + EMPLOYERS_FILE);
        } catch (Exception e) {
            Log.e(TAG, "Error writing employers to file: " + e.getMessage());
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
            Log.e(TAG, "Error generating new ID: " + e.getMessage());
        }
        return newId;
    }

    // Get Employers (READ)
    public static void getEmployers(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        // Read employers from local file
        JSONArray localEmployers = readEmployersFromFile(context);

        // Call the listener immediately with the local data
        listener.onResponse(localEmployers);

        // Fetch from the server and update the local file
        String url = BASE_URL + "get_all";
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    if (response != null && response.length() > 0) {
                        // Merge server data with local data, avoiding duplicates
                        JSONArray mergedEmployers = mergeEmployerData(localEmployers, response);

                        // Store the merged data locally
                        writeEmployersToFile(context, mergedEmployers);

                        // Send the merged data to the listener
                        listener.onResponse(mergedEmployers);
                    } else {
                        // Log that the server returned no data and continue with local data
                        Log.w(TAG, "Server returned an empty response. Using local data.");
                    }
                },
                error -> {
                    // Log the error but continue using local data
                    Log.w(TAG, "Error fetching employers from server, using local data: " + error.getMessage());
                    // Continue with local data already returned earlier
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the server request to the queue
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
            Log.e(TAG, "Error creating employer: " + e.getMessage());
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
                error -> {
                    Log.e(TAG, "Error adding employer to server: " + error.getMessage());
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
                Log.e(TAG, "Error: Employer not found with ID: " + employerId);
                errorListener.onErrorResponse(new VolleyError("Employer not found"));
                return;
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error updating employer: " + e.getMessage());
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
                error -> {
                    Log.e(TAG, "Error updating employer on server: " + error.getMessage());
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
                Log.e(TAG, "Error: Employer not found with ID: " + employerId);
                errorListener.onErrorResponse(new VolleyError("Employer not found"));
                return;
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error deleting employer: " + e.getMessage());
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
                error -> {
                    // Check if it's a 404 error
                    if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                        Log.e(TAG, "Error: Employer not found on server (404)");
                    } else {
                        Log.e(TAG, "Error deleting employer on server: " + error.getMessage());
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

        // Add request to queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}