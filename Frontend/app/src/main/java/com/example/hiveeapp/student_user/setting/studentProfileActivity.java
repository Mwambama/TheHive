package com.example.hiveeapp.student_user.setting;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class studentProfileActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhoneNumber, etAddress, etUniversity, etGPA, etGradDate, etResumePath, etPassword;
    private Button btnUpdateProfile, btnDeleteAccount, backButton;

    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/student"; // Replace with your actual API URL
    private String userId; // Unique identifier for the student

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        // Initialize EditTexts for student profile input
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        etUniversity = findViewById(R.id.etUniversity);
        etGPA = findViewById(R.id.etGPA);
        etGradDate = findViewById(R.id.etGradDate);
        etResumePath = findViewById(R.id.etResumePath);
        etPassword = findViewById(R.id.etPassword);

        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        backButton = findViewById(R.id.back_button);

        // Fetch student info when activity starts
        userId = "312"; // Replace with the actual user ID
        fetchStudentInfo(userId);

        // Set listeners for the buttons
        btnUpdateProfile.setOnClickListener(v -> updateStudentInfo());
        btnDeleteAccount.setOnClickListener(v -> deleteStudentInfo());
        backButton.setOnClickListener(v -> onBackPressed());
    }

    // Fetch student information from the server
    private void fetchStudentInfo(String userId) {
        String url = BASE_URL + "/get/" + userId; // Construct URL for fetching

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        populateStudentInfo(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(studentProfileActivity.this, "Error fetching student info", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    // Populate EditTexts with fetched student info
    private void populateStudentInfo(JSONObject response) {
        try {
            etName.setText(response.getString("name"));
            etEmail.setText(response.getString("email"));
            etPhoneNumber.setText(response.getString("phoneNumber"));
            etAddress.setText(response.getString("address"));
            etUniversity.setText(response.getString("university"));
            etGPA.setText(response.getString("gpa"));
            etGradDate.setText(response.getString("gradDate"));
            etResumePath.setText(response.getString("resumePath"));
            etPassword.setText(response.getString("password"));

            // Save user ID for updating
            userId = response.getString("userId");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing student info", Toast.LENGTH_SHORT).show();
        }
    }

    // Update student information
    private void updateStudentInfo() {
        String url = BASE_URL + "/" + userId; // Construct URL for updating

        JSONObject studentData = new JSONObject();
        try {
            studentData.put("userId", userId);
            studentData.put("name", etName.getText().toString());
            studentData.put("email", etEmail.getText().toString());
            studentData.put("phoneNumber", etPhoneNumber.getText().toString());
            studentData.put("address", etAddress.getText().toString());
            studentData.put("university", etUniversity.getText().toString());
            studentData.put("gpa", etGPA.getText().toString());
            studentData.put("gradDate", etGradDate.getText().toString());
            studentData.put("resumePath", etResumePath.getText().toString());
            studentData.put("password", etPassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                studentData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(studentProfileActivity.this, "Student info updated successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(studentProfileActivity.this, "Error updating student info", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = "student@example.com:Test@1234"; // Replace with actual credentials
                String auth = "Basic " + android.util.Base64.encodeToString(credentials.getBytes(), android.util.Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    // Delete student information
    private void deleteStudentInfo() {
        String url = BASE_URL + "/delete/" + userId; // Construct URL for deleting

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(studentProfileActivity.this, "Student info deleted successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(studentProfileActivity.this, "Error deleting student info", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = "student@example.com:Test@1234"; // Replace with actual credentials
                String auth = "Basic " + android.util.Base64.encodeToString(credentials.getBytes(), android.util.Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
