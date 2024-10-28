package com.example.hiveeapp.student_user.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.StudentMainActivity;
import com.example.hiveeapp.student_user.setting.StudentApi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StudentProfileViewActivity extends AppCompatActivity {

    private static final String TAG = "StudentProfileView";
    private int userId;
    private TextView nameTextView, emailTextView, phoneTextView, universityTextView, graduationDateTextView, gpaTextView, addressTextView;
    private Button updateInfoButton;
    private ImageButton backArrowIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile_view);

        // Retrieve userId from Intent
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Debug: Log userId received from Intent
        Log.d(TAG, "Received userId from Intent: " + userId);

        if (userId == -1) {
            Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Invalid userId. Cannot load profile.");
            return;
        }

        // Initialize TextViews and Button with the corresponding views in XML
        nameTextView = findViewById(R.id.profileNameView);
        emailTextView = findViewById(R.id.profileEmailView);
        phoneTextView = findViewById(R.id.profilePhoneView);
        universityTextView = findViewById(R.id.profileUniversityView);
        graduationDateTextView = findViewById(R.id.profileGraduationDateView);
        gpaTextView = findViewById(R.id.profileGpaView);
        addressTextView = findViewById(R.id.profileAddressView);
        updateInfoButton = findViewById(R.id.updateInfoButton);  // Initialize update button

        // Set up the update button to navigate to the update activity
        updateInfoButton.setOnClickListener(v -> {
            Log.d(TAG, "Update button clicked, navigating to StudentProfileActivity");

            // Navigate to the StudentProfileActivity with userId
            Intent intent = new Intent(StudentProfileViewActivity.this, StudentProfileActivity.class);
            intent.putExtra("USER_ID", userId);  // Pass userId if needed in next activity
            startActivity(intent);
        });

        // Load student information from the backend
        loadStudentProfile(userId);
    }

    private void loadStudentProfile(int userId) {
        Log.d(TAG, "Loading student profile with userId: " + userId);

        StudentApi.getStudent(this, userId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject student) {
                try {
                    displayProfile(student);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(StudentProfileViewActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StudentProfileViewActivity.this, "Error fetching profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProfile(JSONObject student) throws JSONException {
        Log.d(TAG, "Displaying profile data for student: " + student.optString("name"));

        nameTextView.setText(student.optString("name"));
        emailTextView.setText(student.optString("email"));
        phoneTextView.setText(student.optString("phone"));
        universityTextView.setText(student.optString("university"));
        graduationDateTextView.setText(student.optString("graduationDate"));
        gpaTextView.setText(String.valueOf(student.optDouble("gpa")));
    }
}