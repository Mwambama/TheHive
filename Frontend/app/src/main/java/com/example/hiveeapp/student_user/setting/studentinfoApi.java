package com.example.hiveeapp.student_user.setting;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class studentinfoApi extends AppCompatActivity {

    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/student";
    private static final String ADDRESS_URL = "http://coms-3090-063.class.las.iastate.edu:8080/address";
    private static final String TAG = "studentinfoApi";
    private static final int MAX_PHONE_LENGTH = 10;
    private static final int MIN_PHONE_LENGTH = 7;
    private static final int ZIP_CODE_LENGTH = 5;

    public static Map<String, String> getHeaders(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // Mocked username and password for testing purposes
        String username = "employer@example.com";
        String password = "Test@1234";
        String credentials = username + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        return headers;
    }

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
                return studentinfoApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void updateStudent(Context context, JSONObject studentData, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String validationError = validateStudentData(studentData);
        if (validationError != null) {
            Toast.makeText(context, validationError, Toast.LENGTH_LONG).show();
            errorListener.onErrorResponse(new VolleyError(validationError));
            return;
        }

        JSONObject addressData = studentData.optJSONObject("address");

        if (addressData != null && addressData.has("addressId")) {
            updateAddress(context, addressData, addressResponse -> performStudentUpdate(context, studentData, listener, errorListener),
                    error -> handleErrorResponse("Error updating address: " + getErrorMessage(error), error, errorListener));
        } else {
            addAddress(context, addressData, addressResponse -> {
                try {
                    long addressId = addressResponse.getLong("addressId");
                    addressData.put("addressId", addressId);
                    studentData.put("address", addressData);
                } catch (JSONException e) {
                    handleJsonException("Error setting addressId in studentData.", e, errorListener);
                    return;
                }
                performStudentUpdate(context, studentData, listener, errorListener);
            }, error -> handleErrorResponse("Error adding address: " + getErrorMessage(error), error, errorListener));
        }
    }

    public static void deleteStudent(Context context, long studentId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/" + studentId;
        Log.d(TAG, "DELETE Student Request URL: " + url);

        StringRequest request = new StringRequest(
                Request.Method.DELETE,
                url,
                response -> {
                    Log.d(TAG, "Student deleted successfully: " + response);
                    listener.onResponse(response);
                },
                error -> handleErrorResponse("Error deleting student: " + getErrorMessage(error), error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return studentinfoApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    private static void performStudentUpdate(Context context, JSONObject studentData, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL;
        Log.d(TAG, "PUT Student Request URL: " + url);
        Log.d(TAG, "Student Data Payload: " + studentData.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                studentData,
                listener,
                error -> handleErrorResponse("Error updating student: " + getErrorMessage(error), error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return studentinfoApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void addAddress(Context context, JSONObject addressData, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = ADDRESS_URL;
        Log.d(TAG, "POST Address Request URL: " + url);
        Log.d(TAG, "Request Payload: " + addressData.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                addressData,
                listener,
                error -> handleErrorResponse("Error adding address", error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return studentinfoApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void updateAddress(Context context, JSONObject addressData, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = ADDRESS_URL;
        Log.d(TAG, "PUT Address Request URL: " + url);
        Log.d(TAG, "Request Payload: " + addressData.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                addressData,
                listener,
                error -> handleErrorResponse("Error updating address: " + getErrorMessage(error), error, errorListener)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return studentinfoApi.getHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static String validateStudentData(JSONObject studentData) {
        try {
            String name = studentData.optString("name", "");
            if (name.isEmpty()) {
                return "Name is required.";
            }

            String email = studentData.optString("email", "");
            if (email.isEmpty()) {
                return "Email is required.";
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return "Invalid email format.";
            }

            String phone = studentData.optString("phone", "");
            if (phone.isEmpty()) {
                return "Phone number is required.";
            } else if (phone.length() > MAX_PHONE_LENGTH || phone.length() < MIN_PHONE_LENGTH || !phone.matches("\\d+")) {
                return "Phone number must be between " + MIN_PHONE_LENGTH + " and " + MAX_PHONE_LENGTH + " digits and contain only numbers.";
            }

            JSONObject addressData = studentData.optJSONObject("address");
            if (addressData != null) {
                String street = addressData.optString("street", "");
                if (street.isEmpty()) {
                    return "Street address is required.";
                }

                String city = addressData.optString("city", "");
                if (city.isEmpty()) {
                    return "City is required.";
                }

                String state = addressData.optString("state", "");
                if (state.isEmpty()) {
                    return "State is required.";
                } else if (!state.matches("^[A-Z]{2}$")) {
                    return "State must be exactly 2 uppercase letters (e.g., IA, IL).";
                }

                String zipCode = addressData.optString("zipCode", "");
                if (zipCode.isEmpty()) {
                    return "Zip code is required.";
                } else if (zipCode.length() != ZIP_CODE_LENGTH || !zipCode.matches("\\d{" + ZIP_CODE_LENGTH + "}")) {
                    return "Zip code must be " + ZIP_CODE_LENGTH + " digits.";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error validating student data.";
        }

        return null;
    }

    private static void handleErrorResponse(String errorMessagePrefix, VolleyError error, Response.ErrorListener errorListener) {
        String errorMsg = getErrorMessage(error);
        String fullErrorMessage = errorMessagePrefix + ": " + errorMsg;
        Log.e(TAG, fullErrorMessage);
        errorListener.onErrorResponse(new VolleyError(fullErrorMessage));
    }

    private static void handleJsonException(String errorMessagePrefix, JSONException e, Response.ErrorListener errorListener) {
        String fullErrorMessage = errorMessagePrefix + ": " + e.getMessage();
        Log.e(TAG, fullErrorMessage);
        errorListener.onErrorResponse(new VolleyError(fullErrorMessage));
    }

    private static String getErrorMessage(VolleyError error) {
        String errorMsg = "Unknown error";
        if (error.networkResponse != null) {
            try {
                errorMsg = new String(error.networkResponse.data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return errorMsg;
    }
}
