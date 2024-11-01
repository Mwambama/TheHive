package com.example.hiveeapp.employer_user.setting;

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

public class employerinfoApi {

    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/employer";
    private static final String TAG = "EmployerApi";
    private static final int MAX_PHONE_LENGTH = 10;
    private static final int MIN_PHONE_LENGTH = 7;

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
        String username = "iiik@gmail.com";
        String password = "Anondwdb##444fedo";

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
    public static void getStudents(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL;
        Log.d(TAG, "GET Students Request URL: " + url);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                listener,
                error -> handleErrorResponse("Error fetching students", error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return com.example.hiveeapp.employer_user.setting.employerinfoApi.getHeaders(context);
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
                return com.example.hiveeapp.employer_user.setting.employerinfoApi.getHeaders(context);
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
                return com.example.hiveeapp.employer_user.setting.employerinfoApi.getHeaders(context);
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
                return com.example.hiveeapp.employer_user.setting.employerinfoApi.getHeaders(context);
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





















//
//import android.content.Context;
//import android.util.Base64;
//import android.util.Log;
//import android.util.Patterns;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.StringRequest;
//import com.example.hiveeapp.volley.VolleySingleton;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.UnsupportedEncodingException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class employerinfoApi extends AppCompatActivity {
//
//    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/employer";
//    private static final String ADDRESS_URL = "http://coms-3090-063.class.las.iastate.edu:8080/address";
//    private static final String TAG = "employerinfoApi";
//    private static final int MAX_PHONE_LENGTH = 10;
//    private static final int MIN_PHONE_LENGTH = 7;
//    private static final int ZIP_CODE_LENGTH = 5;
//
//    public static Map<String, String> getHeaders(Context context) {
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type", "application/json");
//
//        // Mocked username and password for testing purposes
//        String username = "employer@example.com";
//        String password = "Test@1234";
//        String credentials = username + ":" + password;
//        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//        headers.put("Authorization", auth);
//
//        return headers;
//    }
//
//    public static void getEmployers(Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
//        String url = BASE_URL;
//        Log.d(TAG, "GET Employers Request URL: " + url);
//
//        JsonArrayRequest request = new JsonArrayRequest(
//                Request.Method.GET,
//                url,
//                null,
//                listener,
//                error -> handleErrorResponse("Error fetching employers", error, errorListener)
//        ) {
//            @Override
//            public Map<String, String> getHeaders() {
//                return employerinfoApi.getHeaders(context);
//            }
//        };
//
//        VolleySingleton.getInstance(context).addToRequestQueue(request);
//    }
//
//    public static void updateEmployer(Context context, JSONObject employerData, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
//        String validationError = validateEmployerData(employerData);
//        if (validationError != null) {
//            Toast.makeText(context, validationError, Toast.LENGTH_LONG).show();
//            errorListener.onErrorResponse(new VolleyError(validationError));
//            return;
//        }
//
//        JSONObject addressData = employerData.optJSONObject("address");
//
//        if (addressData != null && addressData.has("addressId")) {
//            updateAddress(context, addressData, addressResponse -> performEmployerUpdate(context, employerData, listener, errorListener),
//                    error -> handleErrorResponse("Error updating address: " + getErrorMessage(error), error, errorListener));
//        } else {
//            addAddress(context, addressData, addressResponse -> {
//                try {
//                    long addressId = addressResponse.getLong("addressId");
//                    addressData.put("addressId", addressId);
//                    employerData.put("address", addressData);
//                } catch (JSONException e) {
//                    handleJsonException("Error setting addressId in employerData.", e, errorListener);
//                    return;
//                }
//                performEmployerUpdate(context, employerData, listener, errorListener);
//            }, error -> handleErrorResponse("Error adding address: " + getErrorMessage(error), error, errorListener));
//        }
//    }
//
//    public static void deleteEmployer(Context context, long employerId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
//        String url = BASE_URL + "/" + employerId;
//        Log.d(TAG, "DELETE Employer Request URL: " + url);
//
//        StringRequest request = new StringRequest(
//                Request.Method.DELETE,
//                url,
//                response -> {
//                    Log.d(TAG, "Employer deleted successfully: " + response);
//                    listener.onResponse(response);
//                },
//                error -> handleErrorResponse("Error deleting employer: " + getErrorMessage(error), error, errorListener)
//        ) {
//            @Override
//            public Map<String, String> getHeaders() {
//                return employerinfoApi.getHeaders(context);
//            }
//        };
//
//        VolleySingleton.getInstance(context).addToRequestQueue(request);
//    }
//
//    private static void performEmployerUpdate(Context context, JSONObject employerData, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
//        String url = BASE_URL;
//        Log.d(TAG, "PUT Employer Request URL: " + url);
//        Log.d(TAG, "Employer Data Payload: " + employerData.toString());
//
//        JsonObjectRequest request = new JsonObjectRequest(
//                Request.Method.PUT,
//                url,
//                employerData,
//                listener,
//                error -> handleErrorResponse("Error updating employer: " + getErrorMessage(error), error, errorListener)
//        ) {
//            @Override
//            public Map<String, String> getHeaders() {
//                return employerinfoApi.getHeaders(context);
//            }
//        };
//
//        VolleySingleton.getInstance(context).addToRequestQueue(request);
//    }
//
//    public static void addAddress(Context context, JSONObject addressData, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
//        String url = ADDRESS_URL;
//        Log.d(TAG, "POST Address Request URL: " + url);
//        Log.d(TAG, "Request Payload: " + addressData.toString());
//
//        JsonObjectRequest request = new JsonObjectRequest(
//                Request.Method.POST,
//                url,
//                addressData,
//                listener,
//                error -> handleErrorResponse("Error adding address", error, errorListener)
//        ) {
//            @Override
//            public Map<String, String> getHeaders() {
//                return employerinfoApi.getHeaders(context);
//            }
//        };
//
//        VolleySingleton.getInstance(context).addToRequestQueue(request);
//    }
//
//    public static void updateAddress(Context context, JSONObject addressData, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
//        String url = ADDRESS_URL;
//        Log.d(TAG, "PUT Address Request URL: " + url);
//        Log.d(TAG, "Request Payload: " + addressData.toString());
//
//        JsonObjectRequest request = new JsonObjectRequest(
//                Request.Method.PUT,
//                url,
//                addressData,
//                listener,
//                error -> handleErrorResponse("Error updating address: " + getErrorMessage(error), error, errorListener)
//        ) {
//            @Override
//            public Map<String, String> getHeaders() {
//                return employerinfoApi.getHeaders(context);
//            }
//        };
//
//        VolleySingleton.getInstance(context).addToRequestQueue(request);
//    }
//
//    public static String validateEmployerData(JSONObject employerData) {
//        try {
//            String name = employerData.optString("name", "");
//            if (name.isEmpty()) {
//                return "Name is required.";
//            }
//
//            String email = employerData.optString("email", "");
//            if (email.isEmpty()) {
//                return "Email is required.";
//            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                return "Invalid email format.";
//            }
//
//            String phone = employerData.optString("phone", "");
//            if (phone.isEmpty()) {
//                return "Phone number is required.";
//            } else if (phone.length() > MAX_PHONE_LENGTH || phone.length() < MIN_PHONE_LENGTH || !phone.matches("\\d+")) {
//                return "Phone number must be between " + MIN_PHONE_LENGTH + " and " + MAX_PHONE_LENGTH + " digits and contain only numbers.";
//            }
//
//            JSONObject addressData = employerData.optJSONObject("address");
//            if (addressData != null) {
//                String street = addressData.optString("street", "");
//                if (street.isEmpty()) {
//                    return "Street address is required.";
//                }
//
//                String city = addressData.optString("city", "");
//                if (city.isEmpty()) {
//                    return "City is required.";
//                }
//
//                String state = addressData.optString("state", "");
//                if (state.isEmpty()) {
//                    return "State is required.";
//                } else if (!state.matches("^[A-Z]{2}$")) {
//                    return "State must be exactly 2 uppercase letters (e.g., IA, IL).";
//                }
//
//                String zipCode = addressData.optString("zipCode", "");
//                if (zipCode.isEmpty()) {
//                    return "Zip code is required.";
//                } else if (zipCode.length() != ZIP_CODE_LENGTH || !zipCode.matches("\\d{" + ZIP_CODE_LENGTH + "}")) {
//                    return "Zip code must be " + ZIP_CODE_LENGTH + " digits.";
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error validating employer data.";
//        }
//
//        return null;
//    }
//
//    private static void handleErrorResponse(String errorMessagePrefix, VolleyError error, Response.ErrorListener errorListener) {
//        String errorMsg = getErrorMessage(error);
//        String fullErrorMessage = errorMessagePrefix + ": " + errorMsg;
//        Log.e(TAG, fullErrorMessage);
//        errorListener.onErrorResponse(new VolleyError(fullErrorMessage));
//    }
//
//    private static void handleJsonException(String errorMessagePrefix, JSONException e, Response.ErrorListener errorListener) {
//        String fullErrorMessage = errorMessagePrefix + ": " + e.getMessage();
//        Log.e(TAG, fullErrorMessage);
//        errorListener.onErrorResponse(new VolleyError(fullErrorMessage));
//    }
//
//    private static String getErrorMessage(VolleyError error) {
//        String errorMsg = "Unknown error";
//        if (error.networkResponse != null && error.networkResponse.data != null) {
//            try {
//                errorMsg = new String(error.networkResponse.data, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
//        return errorMsg;
//    }
//}






















//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.example.hiveeapp.R;
//import com.example.hiveeapp.volley.VolleySingleton;
//import org.json.JSONException;
//import org.json.JSONObject;
//import java.util.HashMap;
//import java.util.Map;
//
//public class employerinfoActivity extends AppCompatActivity {
//
//    private EditText nameEditText;
//    private EditText emailEditText;
//    private EditText phoneEditText;
//    private EditText streetEditText;
//    private EditText complementEditText;
//    private EditText cityEditText;
//    private EditText stateEditText;
//    private EditText zipCodeEditText;
//    private EditText fieldEditText;
//
//    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/employer"; // Replace with your actual API URL
//    private String userId = "314"; // Hardcoded for testing
//    private String addressId; // To be fetched from the response
//    private String companyId; // To be fetched from the response
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_employer_info);
//
//        // Initialize EditTexts for employer information input
//        nameEditText = findViewById(R.id.name);
//        emailEditText = findViewById(R.id.email);
//        phoneEditText = findViewById(R.id.phone);
//        streetEditText = findViewById(R.id.street);
//        complementEditText = findViewById(R.id.complement);
//        cityEditText = findViewById(R.id.city);
//        stateEditText = findViewById(R.id.state);
//        zipCodeEditText = findViewById(R.id.zip_code);
//        fieldEditText = findViewById(R.id.field);
//
//        Button updateButton = findViewById(R.id.update_button);
//        Button deleteButton = findViewById(R.id.delete_button);
//
//        // Fetch employer info when activity starts
//        fetchEmployerInfo(userId);
//
//        // Set listeners for the buttons
//        updateButton.setOnClickListener(v -> updateEmployerInfo());
//        deleteButton.setOnClickListener(v -> deleteEmployerInfo());
//    }
//
//    // Fetch employer information from the server
//    private void fetchEmployerInfo(String userId) {
//        String url = BASE_URL + "/get/" + userId; // Construct URL for fetching
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.GET,
//                url,
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        populateEmployerInfo(response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        Toast.makeText(employerinfoActivity.this, "Error fetching employer info", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                String credentials = "employer@example.com:Test@1234"; // Use actual credentials
//                String auth = "Basic " + android.util.Base64.encodeToString(credentials.getBytes(), android.util.Base64.NO_WRAP);
//                headers.put("Authorization", auth);
//                return headers;
//            }
//        };
//
//        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
//    }
//
//    // Populate EditTexts with fetched employer info
//    private void populateEmployerInfo(JSONObject response) {
//        try {
//            nameEditText.setText(response.getString("name"));
//            emailEditText.setText(response.getString("email"));
//            phoneEditText.setText(response.getString("phone"));
//            fieldEditText.setText(response.getString("field"));
//
//            JSONObject address = response.getJSONObject("address");
//            streetEditText.setText(address.getString("street"));
//            complementEditText.setText(address.getString("complement"));
//            cityEditText.setText(address.getString("city"));
//            stateEditText.setText(address.getString("state"));
//            zipCodeEditText.setText(address.getString("zipCode"));
//
//            // Save IDs for updating
//            userId = response.getString("userId");
//            addressId = address.getString("addressId");
//            companyId = response.getString("companyId");
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Error parsing employer info", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    // Update employer information
//    private void updateEmployerInfo() {
//        String url = BASE_URL + "/" + userId; // Construct URL for updating
//
//        JSONObject jobData = new JSONObject();
//        try {
//            jobData.put("userId", userId);
//            jobData.put("name", nameEditText.getText().toString());
//            jobData.put("email", emailEditText.getText().toString());
//            jobData.put("phone", phoneEditText.getText().toString());
//            jobData.put("field", fieldEditText.getText().toString());
//            jobData.put("companyId", companyId);
//
//            JSONObject address = new JSONObject();
//            address.put("addressId", addressId);
//            address.put("street", streetEditText.getText().toString());
//            address.put("complement", complementEditText.getText().toString());
//            address.put("city", cityEditText.getText().toString());
//            address.put("state", stateEditText.getText().toString());
//            address.put("zipCode", zipCodeEditText.getText().toString());
//
//            jobData.put("address", address);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.PUT,
//                url,
//                jobData,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Toast.makeText(employerinfoActivity.this, "Employer info updated successfully", Toast.LENGTH_SHORT).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        Toast.makeText(employerinfoActivity.this, "Error updating employer info", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                String credentials = "employer@example.com:Test@1234"; // Use actual credentials
//                String auth = "Basic " + android.util.Base64.encodeToString(credentials.getBytes(), android.util.Base64.NO_WRAP);
//                headers.put("Authorization", auth);
//                return headers;
//            }
//        };
//
//        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
//    }
//
//    // Delete employer information
//    private void deleteEmployerInfo() {
//        String url = BASE_URL + "/delete/" + userId; // Construct URL for deleting
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.DELETE,
//                url,
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Toast.makeText(employerinfoActivity.this, "Employer info deleted successfully", Toast.LENGTH_SHORT).show();
//                        finish(); // Close the activity
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        Toast.makeText(employerinfoActivity.this, "Error deleting employer info", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
//
//        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
//    }
//}
