package com.example.hiveeapp.student_user.setting;


import android.util.Patterns;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.VolleyError;
import com.example.hiveeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * StudentUpdateActivity allows the user to update their information by filling out a form.
 * The form includes fields like name, email, phone number, address, etc., which are validated before submission.
 */
public class StudentUpdatesActivity extends AppCompatActivity {

    private EditText nameField, emailField, phoneField, streetField, complementField, cityField, stateField, zipField;
    private Button updateStudentButton;
    private ImageButton backArrowIcon;

    // Constants for validation
    private static final int MAX_PHONE_LENGTH = 15;  // Adjust based on your DB schema
    private static final int MIN_PHONE_LENGTH = 7;   // Minimum acceptable phone length
    private static final int ZIP_CODE_LENGTH = 5;    // Standard US ZIP code length
    private static final String USER_PREFS = "MyAppPrefs"; // Shared preferences key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile); // Update layout name accordingly

        // Initialize the input fields and buttons
        initViews();

        // Set up back arrow functionality to close the current activity and return to the previous one
        backArrowIcon.setOnClickListener(v -> finish());

        // Handle the "Update Student" button click and validate inputs
        updateStudentButton.setOnClickListener(v -> {
            if (validateInput()) {
                updateStudent();  // If inputs are valid, proceed to update the student information
            }
        });
    }

    /**
     * Initializes the input fields and buttons in the activity layout.
     */
    private void initViews() {
        nameField = findViewById(R.id.nameField);
        emailField = findViewById(R.id.emailField);
        phoneField = findViewById(R.id.phoneField);
        streetField = findViewById(R.id.streetField);
        complementField = findViewById(R.id.complementField);
        cityField = findViewById(R.id.cityField);
        stateField = findViewById(R.id.stateField);
        zipField = findViewById(R.id.zipField);
        updateStudentButton = findViewById(R.id.updateProfileButton);
        backArrowIcon = findViewById(R.id.backArrowIcon);
    }

    /**
     * Validates the input fields to ensure that all required fields are filled and valid.
     *
     * @return true if all fields are valid, false otherwise
     */
    private boolean validateInput() {
        boolean isValid = true;

        // Reset any previous error messages
        nameField.setError(null);
        emailField.setError(null);
        phoneField.setError(null);
        streetField.setError(null);
        cityField.setError(null);
        stateField.setError(null);
        zipField.setError(null);

        // Get the input values from the fields
        String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();
        String street = streetField.getText().toString().trim();
        String city = cityField.getText().toString().trim();
        String state = stateField.getText().toString().trim();
        String zip = zipField.getText().toString().trim();

        // Validate the name field
        if (name.isEmpty()) {
            nameField.setError("Name is required");
            isValid = false;
        }

        // Validate the email field using Patterns.EMAIL_ADDRESS
        if (email.isEmpty()) {
            emailField.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Invalid email format");
            isValid = false;
        }

        // Validate the phone field
        if (phone.isEmpty()) {
            phoneField.setError("Phone number is required");
            isValid = false;
        } else if (phone.length() > MAX_PHONE_LENGTH || phone.length() < MIN_PHONE_LENGTH) {
            phoneField.setError("Phone number must be between " + MIN_PHONE_LENGTH + " and " + MAX_PHONE_LENGTH + " digits");
            isValid = false;
        } else if (!phone.matches("\\d+")) {
            phoneField.setError("Phone number must contain only digits");
            isValid = false;
        }

        // Validate the address fields
        if (street.isEmpty()) {
            streetField.setError("Street address is required");
            isValid = false;
        }

        if (city.isEmpty()) {
            cityField.setError("City is required");
            isValid = false;
        }

        if (state.isEmpty()) {
            stateField.setError("State is required");
            isValid = false;
        }

        // Validate the ZIP code format (assuming US ZIP code)
        if (zip.isEmpty()) {
            zipField.setError("Zip code is required");
            isValid = false;
        } else if (zip.length() != ZIP_CODE_LENGTH || !zip.matches("\\d{" + ZIP_CODE_LENGTH + "}")) {
            zipField.setError("Zip code must be " + ZIP_CODE_LENGTH + " digits");
            isValid = false;
        }

        return isValid;
    }

    /**
     * Constructs a JSON object with student details and sends a request to update student information.
     */
    private void updateStudent() {
        // Get the values from the input fields
        String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();
        String street = streetField.getText().toString().trim();
        String complement = complementField.getText().toString().trim();
        String city = cityField.getText().toString().trim();
        String state = stateField.getText().toString().trim();
        String zipCode = zipField.getText().toString().trim();

        // Retrieve the studentId from SharedPreferences (or use a hardcoded value for testing)
        int studentId = 1141;  // This is a test student ID

        // Construct the student JSON object
        JSONObject studentData = new JSONObject();
        try {
            studentData.put("name", name);
            studentData.put("email", email);
            studentData.put("phone", phone);
            studentData.put("role", "STUDENT");
            studentData.put("studentId", studentId);
            studentData.put("field", JSONObject.NULL);
            studentData.put("jobApplications", new JSONArray());

            // Construct the address JSON object
            JSONObject address = new JSONObject();
            address.put("addressId", JSONObject.NULL);
            address.put("street", street);
            address.put("complement", complement.isEmpty() ? JSONObject.NULL : complement);
            address.put("city", city);
            address.put("state", state);
            address.put("zipCode", zipCode);

            studentData.put("address", address);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating student data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send the student data to the server using the StudentApi
        studentinfoApi.updateStudent(this, studentData,
                response -> {
                    // Handle successful response
                    Toast.makeText(StudentUpdatesActivity.this, "Student updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();  // Close the activity and return to the previous screen
                },
                error -> {
                    // Handle error response and display user-friendly message
                    String errorMessage = getErrorMessage(error);
                    Toast.makeText(StudentUpdatesActivity.this, "Error updating student: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Extracts and returns a meaningful error message from a VolleyError.
     *
     * @param error The VolleyError object containing the error details
     * @return A user-friendly error message
     */
    private String getErrorMessage(VolleyError error) {
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


















//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.example.hiveeapp.R;
//import com.example.hiveeapp.volley.VolleySingleton;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class studentProfileActivity extends AppCompatActivity {
//
//    private EditText etName, etEmail, etPhoneNumber, etAddress, etUniversity, etGPA, etGradDate, etResumePath;
//    private Button btnUpdateProfile, btnDeleteAccount, backButton;
//
//    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/student"; // Replace with your actual API URL
//    private String companyId; // Unique identifier for the student
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student_profile);
//
//        // Initialize EditTexts for student profile input
//        etName = findViewById(R.id.etName);
//        etEmail = findViewById(R.id.etEmail);
//        etPhoneNumber = findViewById(R.id.etPhoneNumber);
//        etAddress = findViewById(R.id.etAddress);
//        etUniversity = findViewById(R.id.etUniversity);
//        etGPA = findViewById(R.id.etGPA);
//        etGradDate = findViewById(R.id.etGradDate);
//        etResumePath = findViewById(R.id.etResumePath);
//
//        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
//        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
//        backButton = findViewById(R.id.back_button);
//
//        // Fetch student info when activity starts
//        companyId = "200"; // Replace with the actual company ID
//        fetchStudentInfo(companyId);
//
//        // Set listeners for the buttons
//        btnUpdateProfile.setOnClickListener(v -> updateStudentInfo());
//        btnDeleteAccount.setOnClickListener(v -> deleteStudentInfo());
//        backButton.setOnClickListener(v -> onBackPressed());
//    }
//
//    // Fetch student information from the server
//    private void fetchStudentInfo(String companyId) {
//        String url = BASE_URL + "/get/" + companyId; // Construct URL for fetching
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.GET,
//                url,
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        populateStudentInfo(response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        Toast.makeText(studentProfileActivity.this, "Error fetching student info", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
//
//        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
//    }
//
//    // Populate EditTexts with fetched student info
//    private void populateStudentInfo(JSONObject response) {
//        try {
//            etName.setText(response.getString("name"));
//            etEmail.setText(response.getString("email"));
//            etPhoneNumber.setText(response.getString("phoneNumber"));
//            etAddress.setText(response.getString("address"));
//            etUniversity.setText(response.getString("university"));
//            etGPA.setText(response.getString("gpa"));
//            etGradDate.setText(response.getString("gradDate"));
//            etResumePath.setText(response.getString("resumePath"));
//
//            // Save company ID for updating
//            companyId = response.getString("companyId");
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Error parsing student info", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    // Update student information
//    private void updateStudentInfo() {
//        String url = BASE_URL; // Construct URL for updating
//
//        JSONObject studentData = new JSONObject();
//        try {
//            studentData.put("companyId", companyId);
//            studentData.put("name", etName.getText().toString());
//            studentData.put("email", etEmail.getText().toString());
//            studentData.put("phoneNumber", etPhoneNumber.getText().toString());
//            studentData.put("address", etAddress.getText().toString());
//            studentData.put("university", etUniversity.getText().toString());
//            studentData.put("gpa", etGPA.getText().toString());
//            studentData.put("gradDate", etGradDate.getText().toString());
//            studentData.put("resumePath", etResumePath.getText().toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.PUT,
//                url,
//                studentData,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Toast.makeText(studentProfileActivity.this, "Student info updated successfully", Toast.LENGTH_SHORT).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        Toast.makeText(studentProfileActivity.this, "Error updating student info", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                String credentials = "employer@example.com:Test@1234"; // Replace with actual credentials
//                String auth = "Basic " + android.util.Base64.encodeToString(credentials.getBytes(), android.util.Base64.NO_WRAP);
//                headers.put("Authorization", auth);
//                return headers;
//            }
//        };
//
//        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
//    }
//
//    // Delete student information
//    private void deleteStudentInfo() {
//        String url = BASE_URL + "/delete/" + companyId; // Construct URL for deleting
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.DELETE,
//                url,
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Toast.makeText(studentProfileActivity.this, "Student info deleted successfully", Toast.LENGTH_SHORT).show();
//                        finish(); // Close the activity
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        Toast.makeText(studentProfileActivity.this, "Error deleting student info", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                String credentials = "employer@example.com:Test@1234"; // Replace with actual credentials
//                String auth = "Basic " + android.util.Base64.encodeToString(credentials.getBytes(), android.util.Base64.NO_WRAP);
//                headers.put("Authorization", auth);
//                return headers;
//            }
//        };
//
//        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
//    }
//}
