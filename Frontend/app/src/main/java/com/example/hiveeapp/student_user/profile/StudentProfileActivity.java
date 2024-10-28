package com.example.hiveeapp.student_user.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.setting.StudentApi;
import com.google.android.material.button.MaterialButton;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class StudentProfileActivity extends AppCompatActivity {

    private static final String TAG = "StudentProfileActivity";
    private EditText nameEditText, emailEditText, phoneEditText, universityEditText, gpaEditText;
    private MaterialButton saveButton;
    private ImageButton backArrowIcon;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        // Retrieve userId from Intent
        userId = getIntent().getIntExtra("USER_ID", -1);

        if (userId == -1) {
            Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Invalid userId. Cannot load profile.");
            return;
        }

        // Initialize views
        initViews();

        // Load student information
        loadStudentProfile();

        // Set up Save button listener
        saveButton.setOnClickListener(v -> updateStudentProfile());
    }

    private void initViews() {
        nameEditText = findViewById(R.id.profileName);
        emailEditText = findViewById(R.id.profileEmail);
        phoneEditText = findViewById(R.id.profilePhone);
        universityEditText = findViewById(R.id.profileUniversity);
        gpaEditText = findViewById(R.id.profileGPA);
        saveButton = findViewById(R.id.saveProfileButton);
        backArrowIcon = findViewById(R.id.backArrowIcon);

        backArrowIcon.setOnClickListener(v -> finish());
    }

    private void loadStudentProfile() {
        StudentApi.getStudent(this, userId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject student) {
                try {
                    populateProfileFields(student);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(StudentProfileActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StudentProfileActivity.this, "Error fetching profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void populateProfileFields(JSONObject student) throws JSONException {
        nameEditText.setText(student.optString("name"));
        emailEditText.setText(student.optString("email"));
        phoneEditText.setText(student.optString("phone"));
        universityEditText.setText(student.optString("university"));
        gpaEditText.setText(String.valueOf(student.optDouble("gpa")));
    }

    private void updateStudentProfile() {
        try {
            // Gather updated data
            JSONObject updatedStudent = new JSONObject();
            updatedStudent.put("userId", userId); // Ensure this matches the server's expected key
            updatedStudent.put("name", nameEditText.getText().toString());
            updatedStudent.put("email", emailEditText.getText().toString());
            updatedStudent.put("phone", phoneEditText.getText().toString());
            updatedStudent.put("university", universityEditText.getText().toString());

            // Validate and add GPA field
            String gpaText = gpaEditText.getText().toString();
            if (!gpaText.isEmpty()) {
                try {
                    double gpaValue = Double.parseDouble(gpaText);
                    if (!Double.isNaN(gpaValue) && !Double.isInfinite(gpaValue)) {
                        updatedStudent.put("gpa", gpaValue);
                    } else {
                        Log.e(TAG, "GPA value is NaN or Infinite");
                        Toast.makeText(this, "Invalid GPA value", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Invalid GPA input: " + gpaText);
                    Toast.makeText(this, "Invalid GPA format", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Use an empty JSON object for address if it's null
            updatedStudent.put("address", new JSONObject()); // If server-side error persists, pass null or remove it entirely if not required

            // Call StudentApi update method
            StudentApi.updateStudent(this, updatedStudent, response -> {
                Toast.makeText(StudentProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StudentProfileActivity.this, StudentProfileViewActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish();
            }, error -> {
                String errorMessage = getErrorMessage(error);
                Toast.makeText(StudentProfileActivity.this, "Failed to update profile: " + errorMessage, Toast.LENGTH_SHORT).show();
            });
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception while creating update request", e);
            Toast.makeText(StudentProfileActivity.this, "Error creating update request", Toast.LENGTH_SHORT).show();
        }
    }

    // Error message extractor similar to InvitationCreationActivity
    private String getErrorMessage(VolleyError error) {
        String errorMsg = "An unexpected error occurred";
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String errorData = new String(error.networkResponse.data, "UTF-8");
                try {
                    JSONObject jsonError = new JSONObject(errorData);
                    errorMsg = jsonError.optString("message", errorMsg);
                } catch (JSONException jsonException) {
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
