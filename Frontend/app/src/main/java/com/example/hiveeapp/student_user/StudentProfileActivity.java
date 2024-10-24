package com.example.hiveeapp.student_user;

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

public class StudentProfileActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, phoneEditText, universityEditText, gpaEditText;
    private MaterialButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        // Initialize views
        nameEditText = findViewById(R.id.profileName);
        emailEditText = findViewById(R.id.profileEmail);
        phoneEditText = findViewById(R.id.profilePhone);
        universityEditText = findViewById(R.id.profileUniversity);
        gpaEditText = findViewById(R.id.profileGPA);
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
        StudentApi.getStudents(this, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    // Assuming the first element is the student
                    JSONObject student = response.getJSONObject(0);
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

    /**
     * Populates the UI fields with the student data.
     */
    private void populateProfileFields(JSONObject student) throws JSONException {
        nameEditText.setText(student.optString("name"));
        emailEditText.setText(student.optString("email"));
        phoneEditText.setText(student.optString("phone"));
        universityEditText.setText(student.optString("university"));
        gpaEditText.setText(String.valueOf(student.optDouble("gpa")));
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
            updatedStudent.put("university", universityEditText.getText().toString());
            updatedStudent.put("gpa", Double.parseDouble(gpaEditText.getText().toString()));

            // Call the API to update the student
            StudentApi.updateStudent(this, updatedStudent, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(StudentProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(StudentProfileActivity.this, "Error updating profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(StudentProfileActivity.this, "Error creating update request", Toast.LENGTH_SHORT).show();
        }
    }
}

