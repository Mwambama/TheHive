package com.example.hiveeapp.student_user.setting;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class StudentApi {

    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/student";
    private static final String TAG = "StudentApi";

    /**
     * Generates the headers for API requests with authorization.
     *
     * @param context The application context used to retrieve user credentials.
     * @return A map of headers including content type and authorization credentials.
     */
    public static Map<String, String> getHeaders(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // Mocked username and password for testing purposes
        String username = "test@example.com";
        String password = "Test@example1234";

        String credentials = username + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        return headers;
    }

    /**
     * Retrieves a list of students from the server.
     *
     * @param context       The application context.
     * @param listener      Response listener for successful fetch.
     * @param errorListener Error listener for handling errors.
     */
    public static void getStudents(Context context, int userId, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/" + userId;
        Log.d(TAG, "GET Student Request URL: " + url);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                listener,
                error -> handleErrorResponse("Error fetching student", error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return StudentApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }


    /**
     * Adds a new student to the server.
     *
     * @param context       The application context.
     * @param studentData   JSON object containing student details.
     * @param listener      Response listener for successful student creation.
     * @param errorListener Error listener for handling errors.
     */
    public static void addStudent(Context context, JSONObject studentData, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL;
        Log.d(TAG, "POST Student Request URL: " + url);
        Log.d(TAG, "Request Payload: " + studentData.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                studentData,
                listener,
                error -> handleErrorResponse("Error adding student", error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return StudentApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Updates an existing student.
     *
     * @param context       The application context.
     * @param studentData   JSON object containing student details.
     * @param listener      Response listener for successful student update.
     * @param errorListener Error listener for handling errors.
     */
    public static void updateStudent(Context context, JSONObject studentData, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL;
        Log.d(TAG, "PUT Student Request URL: " + url);
        Log.d(TAG, "Student Data Payload: " + studentData.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                studentData,
                listener,
                error -> handleErrorResponse("Error updating student", error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return StudentApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Deletes a student from the server.
     *
     * @param context       The application context.
     * @param studentId     ID of the student to be deleted.
     * @param listener      Response listener for successful student deletion.
     * @param errorListener Error listener for handling errors.
     */
    public static void deleteStudent(Context context, long studentId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/" + studentId;
        Log.d(TAG, "DELETE Student Request URL: " + url);

        // Create a StringRequest for the DELETE method
        StringRequest request = new StringRequest(
                Request.Method.DELETE,
                url,
                response -> {
                    Log.d(TAG, "Student deleted successfully: " + response);
                    listener.onResponse(response);  // Notify the listener of success
                },
                error -> {
                    String errorMsg = getErrorMessage(error);
                    handleErrorResponse("Error deleting student: " + errorMsg, error, errorListener);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return StudentApi.getHeaders(context);
            }
        };

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Handles error responses from the server, logs the details, and invokes the error listener.
     *
     * @param errorMessagePrefix Prefix for the error message to log.
     * @param error              The VolleyError object.
     * @param errorListener      Error listener to handle the error response.
     */
    private static void handleErrorResponse(String errorMessagePrefix, VolleyError error, Response.ErrorListener errorListener) {
        String errorMsg = getErrorMessage(error);
        String fullErrorMessage = errorMessagePrefix + ": " + errorMsg;
        Log.e(TAG, fullErrorMessage);
        errorListener.onErrorResponse(new VolleyError(fullErrorMessage));
    }

    /**
     * Extracts a meaningful error message from a VolleyError.
     *
     * @param error The VolleyError object.
     * @return A string containing the error message.
     */
    private static String getErrorMessage(VolleyError error) {
        String errorMsg = "An unexpected error occurred";
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String errorData = new String(error.networkResponse.data, "UTF-8");

                // Attempt to parse errorData as JSON
                try {
                    JSONObject jsonError = new JSONObject(errorData);
                    errorMsg = jsonError.optString("message", errorMsg);
                } catch (JSONException jsonException) {
                    // If parsing fails, use the raw errorData
                    errorMsg = errorData;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                errorMsg = "Error parsing error message";
            }
        } else if (error.getMessage() != null) {
            errorMsg = error.getMessage();
        }
        return errorMsg;
    }
}
