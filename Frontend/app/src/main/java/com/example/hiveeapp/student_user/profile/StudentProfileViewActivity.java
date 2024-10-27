package com.example.hiveeapp.student_user.profile;

import android.content.Intent;
import android.os.Bundle;
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

    private TextView nameTextView, emailTextView, phoneTextView, universityTextView, graduationDateTextView, gpaTextView, addressTextView;
    private Button updateInfoButton;
    private ImageButton backArrowIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile_view);

        // Initialize views
        int userId = getIntent().getIntExtra("USER_ID", -1);

        nameTextView = findViewById(R.id.profileNameView);
        emailTextView = findViewById(R.id.profileEmailView);
        phoneTextView = findViewById(R.id.profilePhoneView);
        universityTextView = findViewById(R.id.profileUniversityView);
        graduationDateTextView = findViewById(R.id.profileGraduationDateView);
        gpaTextView = findViewById(R.id.profileGpaView);
        addressTextView = findViewById(R.id.profileAddressView);
        updateInfoButton = findViewById(R.id.updateInfoButton);
        backArrowIcon = findViewById(R.id.backArrowIcon);

        // Load student information from the backend
        if (userId != -1) {
            loadStudentProfile(userId);
        }

        // Set up the button to navigate to the update page
        updateInfoButton.setOnClickListener(v -> {
            Intent intent = new Intent(StudentProfileViewActivity.this, StudentProfileActivity.class);
            startActivity(intent);
        });

        // Handle back arrow click
        backArrowIcon.setOnClickListener(v -> {
            // Navigate back to StudentMainActivity
            Intent intent = new Intent(StudentProfileViewActivity.this, StudentMainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadStudentProfile(int userId) {
        // Pass userId as an integer, not as a String
        StudentApi.getStudents(this, userId, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject student = response.getJSONObject(0);
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
        nameTextView.setText(student.optString("name"));
        emailTextView.setText(student.optString("email"));
        phoneTextView.setText(student.optString("phone"));
        universityTextView.setText(student.optString("university"));
        graduationDateTextView.setText(student.optString("graduationDate"));
        gpaTextView.setText(String.valueOf(student.optDouble("gpa")));

        // Format address details
        JSONObject address = student.optJSONObject("address");
        if (address != null) {
            String formattedAddress = address.optString("street") + ", " +
                    address.optString("complement") + ", " +
                    address.optString("city") + ", " +
                    address.optString("state") + " - " +
                    address.optString("zipCode");
            addressTextView.setText(formattedAddress);
        }
    }
}