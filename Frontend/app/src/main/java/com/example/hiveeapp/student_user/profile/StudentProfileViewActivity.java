package com.example.hiveeapp.student_user.profile;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.hiveeapp.student_user.chat.ChatListActivity;
import com.example.hiveeapp.student_user.search.JobSearchActivity;
import com.example.hiveeapp.student_user.setting.StudentApi;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class StudentProfileViewActivity extends AppCompatActivity {

    private static final String TAG = "StudentProfileView";
    private int userId;
    private TextView nameTextView, emailTextView, phoneTextView, universityTextView, graduationDateTextView, gpaTextView;
    private TextView streetTextView, complementTextView, cityTextView, stateTextView, zipCodeTextView;
    private Button updateInfoButton;
    private ImageButton backArrowIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "StudentProfileViewActivity started");
        setContentView(R.layout.activity_student_profile_view);

        // Retrieve userId from Intent or SharedPreferences
        userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            SharedPreferences preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
            userId = preferences.getInt("userId", -1);
        }

        Log.d(TAG, "Retrieved userId: " + userId);

        if (userId == -1) {
            Toast.makeText(this, "Invalid User ID. Cannot load profile.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Invalid userId. Cannot load profile.");
            finish(); // Close the activity if userId is invalid
            return;
        }

        // Initialize views
        initViews();

        // Set up the update button
        updateInfoButton.setOnClickListener(v -> {
            Log.d(TAG, "Update button clicked");
            navigateToEditProfile();
        });

        // Load student information from the backend
        loadStudentProfile(userId);

        // Set up BottomNavigationView
        setupBottomNavigationView();
    }

    private void initViews() {
        try {
            nameTextView = findViewById(R.id.profileNameView);
            emailTextView = findViewById(R.id.profileEmailView);
            phoneTextView = findViewById(R.id.profilePhoneView);
            universityTextView = findViewById(R.id.profileUniversityView);
            graduationDateTextView = findViewById(R.id.profileGraduationDateView);
            gpaTextView = findViewById(R.id.profileGpaView);
            streetTextView = findViewById(R.id.profileStreet);
            complementTextView = findViewById(R.id.profileComplement);
            cityTextView = findViewById(R.id.profileCity);
            stateTextView = findViewById(R.id.profileState);
            zipCodeTextView = findViewById(R.id.profileZipCode);
            updateInfoButton = findViewById(R.id.updateProfileButton);
            backArrowIcon = findViewById(R.id.backArrowIcon);

            backArrowIcon.setOnClickListener(v -> finish());
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views: " + e.getMessage());
        }
    }

    private void navigateToEditProfile() {
        if (userId <= 0) {
            Toast.makeText(this, "Invalid User ID. Cannot proceed to edit profile.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Invalid userId: " + userId);
            return;
        }

        Intent intent = new Intent(StudentProfileViewActivity.this, StudentProfileActivity.class);
        intent.putExtra("USER_ID", userId);
        Log.d(TAG, "Navigating to StudentProfileActivity with userId: " + userId);
        startActivity(intent);
    }

    private void setupBottomNavigationView() {
        try {
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                Log.d(TAG, "BottomNavigationView item selected: " + itemId);
                if (itemId == R.id.navigation_apply) {
                    startActivity(new Intent(StudentProfileViewActivity.this, StudentMainActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    return true;
                } else if (itemId == R.id.navigation_chat) {
                    Intent intent = new Intent(StudentProfileViewActivity.this, ChatListActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_search) {
                    Intent intent = new Intent(StudentProfileViewActivity.this, JobSearchActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            });

            bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up BottomNavigationView: " + e.getMessage());
        }
    }

    private void loadStudentProfile(int userId) {
        Log.d(TAG, "Loading student profile with userId: " + userId);

        StudentApi.getStudent(this, userId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject student) {
                try {
                    Log.d(TAG, "Student profile loaded successfully");
                    displayProfile(student);
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Exception while displaying profile: " + e.getMessage());
                    Toast.makeText(StudentProfileViewActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error fetching profile: " + error.getMessage());
                Toast.makeText(StudentProfileViewActivity.this, "Error fetching profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProfile(JSONObject student) throws JSONException {
        Log.d(TAG, "Displaying profile data for student: " + student.optString("name"));

        try {
            // Set basic profile fields
            nameTextView.setText(student.optString("name", "N/A"));
            emailTextView.setText(student.optString("email", "N/A"));
            phoneTextView.setText(student.optString("phone", "N/A"));
            universityTextView.setText(student.optString("university", "N/A"));
            graduationDateTextView.setText(student.optString("graduationDate", "N/A"));
            gpaTextView.setText(String.valueOf(student.optDouble("gpa", 0.0)));

            // Set address fields
            JSONObject address = student.optJSONObject("address");
            if (address != null) {
                streetTextView.setText(address.optString("street", "N/A"));
                complementTextView.setText(address.optString("complement", "N/A"));
                cityTextView.setText(address.optString("city", "N/A"));
                stateTextView.setText(address.optString("state", "N/A"));
                zipCodeTextView.setText(address.optString("zipCode", "N/A"));
            } else {
                Log.d(TAG, "No address found for student");
                streetTextView.setText("N/A");
                complementTextView.setText("N/A");
                cityTextView.setText("N/A");
                stateTextView.setText("N/A");
                zipCodeTextView.setText("N/A");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error displaying profile data: " + e.getMessage());
        }
    }
}
