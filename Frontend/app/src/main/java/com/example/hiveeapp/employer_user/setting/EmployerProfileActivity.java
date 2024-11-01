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

public class EmployerProfileActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, phoneEditText, universityEditText, gpaEditText;
    private MaterialButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_info_test);

        // Initialize views
        nameEditText = findViewById(R.id.profileName);
        emailEditText = findViewById(R.id.profileEmail);
        phoneEditText = findViewById(R.id.profilePhone);
        saveButton = findViewById(R.id.saveProfileButton);

        // Load student information
        loadStudentProfile();

        // Handle Save button click
        saveButton.setOnClickListener(v -> updateStudentProfile());
    }

    /**
     * Loads the student profile data from the backend.
     */
    private void loadStudentProfile() {
        // Call the StudentApi to get student info
        employerinfoApi.getStudents(this, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    // Assuming the first element is the student
                    JSONObject student = response.getJSONObject(0);
                    populateProfileFields(student);
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

    /**
     * Populates the UI fields with the student data.
     */
    private void populateProfileFields(JSONObject student) throws JSONException {
        nameEditText.setText(student.optString("name"));
        emailEditText.setText(student.optString("email"));
        phoneEditText.setText(student.optString("phone"));

    }

    /**
     * Sends the updated profile information to the backend.
     */
    private void updateStudentProfile() {
        try {
            // Create a JSONObject with the updated values
            JSONObject updatedStudent = new JSONObject();
            updatedStudent.put("name", nameEditText.getText().toString());
            updatedStudent.put("email", emailEditText.getText().toString());
            updatedStudent.put("phone", phoneEditText.getText().toString());

            // Call the API to update the student
            employerinfoApi.updateStudent(this, updatedStudent, new Response.Listener<JSONObject>() {
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
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(EmployerProfileActivity.this, "Error creating update request", Toast.LENGTH_SHORT).show();
        }
    }
}














//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.example.hiveeapp.R;
//import com.example.hiveeapp.student_user.profile.StudentProfileViewActivity;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.UnsupportedEncodingException;
//
//public class EmployerProfileActivity extends AppCompatActivity {
//
//    private static final String TAG = "EmployerProfileActivity";
//   // private static final int PICK_PDF_REQUEST = 1;
//    private EditText nameEditText, streetEditText, complementEditText, cityEditText, stateEditText, zipCodeEditText;
//    private EditText emailEditText, phoneEditText, fieldEditText;
//   //private MaterialButton saveButton, updateInfoButton;
//    private Button updateInfoButton;
//    private ImageButton backArrowIcon;
//    private ImageView pdfPreview;
//    private int userId;
//    private int companyId;
//   // private Uri pdfUri;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_employer_info);
//
//        // Retrieve userId from Intent
//        userId = getIntent().getIntExtra("USER_ID", -1);
//
//        if (userId == -1) {
//            Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show();
//            Log.e(TAG, "Invalid userId. Cannot load profile.");
//            return;
//        }
//
//        companyId = getIntent().getIntExtra("COMPANY_ID", -1);
//
//        if (companyId == -1) {
//            Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show();
//            Log.e(TAG, "Invalid userId. Cannot load profile.");
//            return;
//        }
//
//        // Initialize views
//        initViews();
//
//        // Load student information
//        loadEmployerProfile();
//
//        // Set up Save button listener
//        updateInfoButton.setOnClickListener(v -> updateEmployerProfile());
//    }
//
//    private void initViews() {
//        nameEditText = findViewById(R.id.profileName);
//        emailEditText = findViewById(R.id.profileEmail);
//        phoneEditText = findViewById(R.id.profilePhone);
//        fieldEditText =  findViewById(R.id.profileField);
//        updateInfoButton = findViewById(R.id.updateProfileButton);
//
//        // Initialize individual address fields
//        streetEditText = findViewById(R.id.profileStreet);
//        complementEditText = findViewById(R.id.profileComplement);
//        cityEditText = findViewById(R.id.profileCity);
//        stateEditText = findViewById(R.id.profileState);
//        zipCodeEditText = findViewById(R.id.profileZipCode);
//
//       // saveButton = findViewById(R.id.saveProfileButton);
//        backArrowIcon = findViewById(R.id.backArrowIcon);
//
//        backArrowIcon.setOnClickListener(v -> finish());
//    }
//    private void loadEmployerProfile() {
//        employerinfoApi.getStudent(this, userId, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject student) {
//                try {
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
//    private void populateProfileFields(JSONObject student) throws JSONException {
//        nameEditText.setText(student.optString("name"));
//        emailEditText.setText(student.optString("email"));
//        phoneEditText.setText(student.optString("phone"));
//        fieldEditText.setText(student.optString("field"));
//
//        // Populate address fields if available
//        JSONObject address = student.optJSONObject("address");
//        if (address != null) {
//            streetEditText.setText(address.optString("street"));
//            complementEditText.setText(address.optString("complement"));
//            cityEditText.setText(address.optString("city"));
//            stateEditText.setText(address.optString("state"));
//            zipCodeEditText.setText(address.optString("zipCode"));
//        }
//    }
//
//    private void updateEmployerProfile() {
//        try {
//            // Gather updated data
//            JSONObject updatedEmployer = new JSONObject();
//            updatedEmployer.put("userId", userId);
//            updatedEmployer.put("name", nameEditText.getText().toString());
//            updatedEmployer.put("email", emailEditText.getText().toString());
//            updatedEmployer.put("phone", phoneEditText.getText().toString());
//            updatedEmployer.put("companyId", companyId);
//            updatedEmployer.put("field", fieldEditText.getText().toString());
//            updatedEmployer.put("role", "EMPLOYER");
//
//
//            // Create the address as a nested JSON object
//            JSONObject addressObject = new JSONObject();
//            addressObject.put("street", streetEditText.getText().toString());
//            addressObject.put("complement", complementEditText.getText().toString());
//            addressObject.put("city", cityEditText.getText().toString());
//            addressObject.put("state", stateEditText.getText().toString());
//            addressObject.put("zipCode", zipCodeEditText.getText().toString());
//
//            updatedEmployer.put("address", addressObject);
//
//            // Call StudentApi update method
//            employerinfoApi.updateStudent(this, updatedEmployer, response -> {
//                Toast.makeText(EmployerProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(EmployerProfileActivity.this, StudentProfileViewActivity.class);
//                intent.putExtra("USER_ID", userId);
//                startActivity(intent);
//                finish();
//            }, error -> {
//                String errorMessage = getErrorMessage(error);
//                Toast.makeText(EmployerProfileActivity.this, "Failed to update profile: " + errorMessage, Toast.LENGTH_SHORT).show();
//            });
//        } catch (JSONException e) {
//            Log.e(TAG, "JSON Exception while creating update request", e);
//            Toast.makeText(EmployerProfileActivity.this, "Error creating update request", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//
//    private String getErrorMessage(VolleyError error) {
//        String errorMsg = "An unexpected error occurred";
//        if (error.networkResponse != null && error.networkResponse.data != null) {
//            try {
//                String errorData = new String(error.networkResponse.data, "UTF-8");
//                try {
//                    JSONObject jsonError = new JSONObject(errorData);
//                    errorMsg = jsonError.optString("message", errorMsg);
//                } catch (JSONException jsonException) {
//                    errorMsg = errorData;
//                }
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//                errorMsg = "Error parsing error message";
//            }
//        } else if (error.getMessage() != null) {
//            errorMsg = error.getMessage();
//        }
//        return errorMsg;
//    }
//}