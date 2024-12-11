package com.example.hiveeapp.employer_user.setting;


import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.setting.StudentApi;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//public class EmployerProfileActivity extends AppCompatActivity {
//
//    private EditText nameEditText, emailEditText, phoneEditText, universityEditText, gpaEditText;
//    private MaterialButton saveButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_employer_info_test);
//
//        // Initialize views
//        nameEditText = findViewById(R.id.profileName);
//        emailEditText = findViewById(R.id.profileEmail);
//        phoneEditText = findViewById(R.id.profilePhone);
//        saveButton = findViewById(R.id.saveProfileButton);
//
//        // Load student information
//        loadStudentProfile();
//
//        // Handle Save button click
//        saveButton.setOnClickListener(v -> updateStudentProfile());
//    }
//
//    /**
//     * Loads the student profile data from the backend.
//     */
//    private void loadStudentProfile() {
//        // Call the StudentApi to get student info
//        employerinfoApi.getEmployer(this, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                try {
//                    // Assuming the first element is the student
//                    JSONObject student = response.getJSONObject(0);
//                    populateProfileFields(student);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(EmployerProfileActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(EmployerProfileActivity.this, "Error fetching profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    /**
//     * Populates the UI fields with the student data.
//     */
//    private void populateProfileFields(JSONObject student) throws JSONException {
//        nameEditText.setText(student.optString("name"));
//        emailEditText.setText(student.optString("email"));
//        phoneEditText.setText(student.optString("phone"));
//
//    }
//
//    /**
//     * Sends the updated profile information to the backend.
//     */
//    private void updateStudentProfile() {
//        try {
//            // Create a JSONObject with the updated values
//            JSONObject updatedStudent = new JSONObject();
//            updatedStudent.put("name", nameEditText.getText().toString());
//            updatedStudent.put("email", emailEditText.getText().toString());
//            updatedStudent.put("phone", phoneEditText.getText().toString());
//
//            // Call the API to update the student
//            employerinfoApi.updateStudent(this, updatedStudent, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    Toast.makeText(EmployerProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(EmployerProfileActivity.this, "Error updating profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(EmployerProfileActivity.this, "Error creating update request", Toast.LENGTH_SHORT).show();
//        }
//    }
//}



// this is the editprofile for the employer with address that  has a few issues, I need this to complete this



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.profile.StudentProfileViewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;






public class EmployerProfileActivity extends AppCompatActivity {

    private static final String TAG = "EmployerProfileActivity";
    private EditText nameEditText, streetEditText, complementEditText, cityEditText, stateEditText, zipCodeEditText;
    private EditText emailEditText, phoneEditText, fieldEditText;
    private Button saveButton;
    private ImageButton backArrowIcon;
    private int userId;
    private int companyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_info);

        // Retrieve userId and companyId from Intent
        userId = getIntent().getIntExtra("USER_ID", -1);
        companyId = getIntent().getIntExtra("COMPANY_ID", -1);
        Log.d(TAG, "Received userId from Intent: " + userId);
        Log.d(TAG, "Received companyId from Intent: " + companyId);

        if (userId == -1) {
            Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Invalid userId. Cannot load profile.");
            return;
        }

        if (companyId == -1) {
            Toast.makeText(this, "Invalid Company ID", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Invalid companyId. Cannot load profile.");
            return;
        }

        // Initialize views
        initViews();

        // Load employer information
        loadEmployerProfile();

        // Set up Save button listener
        saveButton.setOnClickListener(v -> updateEmployerProfile());
    }

    private void initViews() {
        nameEditText = findViewById(R.id.profileName);
        emailEditText = findViewById(R.id.profileEmail);
        phoneEditText = findViewById(R.id.profilePhone);
        fieldEditText = findViewById(R.id.profileField);
        saveButton = findViewById(R.id.updateInfoButton);

        // Initialize individual address fields
        streetEditText = findViewById(R.id.profileStreet);
        complementEditText = findViewById(R.id.profileComplement);
        cityEditText = findViewById(R.id.profileCity);
        stateEditText = findViewById(R.id.profileState);
        zipCodeEditText = findViewById(R.id.profileZipCode);

        backArrowIcon = findViewById(R.id.backArrowIcon);
        backArrowIcon.setOnClickListener(v -> finish());
    }

    private void loadEmployerProfile() {
        employerinfoApi.getEmployer(this, userId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject employer) {
                try {
                    populateProfileFields(employer);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(EmployerProfileActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EmployerProfileActivity.this, "Error fetching profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateProfileFields(JSONObject employer) throws JSONException {
        nameEditText.setText(employer.optString("name"));
        emailEditText.setText(employer.optString("email"));
        phoneEditText.setText(employer.optString("phone"));
        fieldEditText.setText(employer.optString("field"));

        // Populate address fields if available
        JSONObject address = employer.optJSONObject("address");
        if (address != null) {
            streetEditText.setText(address.optString("street"));
            complementEditText.setText(address.optString("complement"));
            cityEditText.setText(address.optString("city"));
            stateEditText.setText(address.optString("state"));
            zipCodeEditText.setText(address.optString("zipCode"));
        }
    }

    private void updateEmployerProfile() {
        // Collect updated information from EditTexts and send to backend
        JSONObject updatedProfile = new JSONObject();
        try {
            updatedProfile.put("userId", userId); // Add userId to the JSON object
            updatedProfile.put("name", nameEditText.getText().toString());
            updatedProfile.put("email", emailEditText.getText().toString());
            updatedProfile.put("phone", phoneEditText.getText().toString());
            updatedProfile.put("field", fieldEditText.getText().toString());

            JSONObject updatedAddress = new JSONObject();
            updatedAddress.put("street", streetEditText.getText().toString());
            updatedAddress.put("complement", complementEditText.getText().toString());
            updatedAddress.put("city", cityEditText.getText().toString());
            updatedAddress.put("state", stateEditText.getText().toString());
            updatedAddress.put("zipCode", zipCodeEditText.getText().toString());

            updatedProfile.put("address", updatedAddress);
            updatedProfile.put("companyId", companyId); // Ensure companyId is included
            updatedProfile.put("role", "EMPLOYER"); // Add role if required by the API
        } catch (JSONException e) {
            e.printStackTrace();
        }

        employerinfoApi.updateEmployer(this, updatedProfile, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(EmployerProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EmployerProfileActivity.this, "Error updating profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
