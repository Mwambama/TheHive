package com.example.hiveeapp.student_user;

import com.example.hiveeapp.R;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.employer_user.display.AddJobActivity;
import com.example.hiveeapp.employer_user.model.ChatActivity;
import com.example.hiveeapp.employer_user.model.TrackingApplicationActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

public class studentProfileActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private EditText etName, etEmail, etPhoneNumber, etAddress, etUniversity, etGPA, etGradDate, etResumePath, etPassword;
    private Button btnUpdateProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

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

        // Load existing profile data
        loadProfileData();

        // Set up onClick listener for the update button
        btnUpdateProfile.setOnClickListener(v -> updateProfile());

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void loadProfileData() {
        String jsonString = loadJSONFromAsset("profile_data.json");

        if (jsonString != null) {
            try {
                JSONObject profile = new JSONObject(jsonString);

                etName.setText(profile.getString("name"));
                etEmail.setText(profile.getString("email"));
                etPhoneNumber.setText(profile.getString("phoneNumber"));
                etAddress.setText(profile.getString("address"));
                etUniversity.setText(profile.getString("university"));
                etGPA.setText(profile.getString("gpa"));
                etGradDate.setText(profile.getString("gradDate"));
                etResumePath.setText(profile.getString("resumePath"));
                etPassword.setText(profile.getString("password"));

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error parsing JSON data", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to load JSON file", Toast.LENGTH_SHORT).show();
        }
    }

    private String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void updateProfile() {
        // Logic to update profile information
        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_add_job) {
            startActivity(new Intent(this, AddJobActivity.class));
            return true;
        } else if (itemId == R.id.nav_chat) {
            startActivity(new Intent(this, ChatActivity.class));
            return true;
        } else if (itemId == R.id.nav_home) {
            return true;
        } else if (itemId == R.id.nav_tracking) {
            startActivity(new Intent(this, TrackingApplicationActivity.class));
            return true;
        } else {
            return false;
        }
    }
}
