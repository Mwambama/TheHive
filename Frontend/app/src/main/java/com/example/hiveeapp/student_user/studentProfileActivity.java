package com.example.hiveeapp.student_user;

import com.example.hiveeapp.R;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.volley.VolleySingleton;
import org.json.JSONException;
import org.json.JSONObject;



    public class studentProfileActivity extends AppCompatActivity {

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
        }

        private void loadProfileData() {
            // Fetch profile data from the server (example data used here)
            etName.setText("Yanchui");
            etEmail.setText("yanchui@gmail.com");
            etPhoneNumber.setText("+14087889999");
            etAddress.setText("678 Lincoln Way, Ames, Iowa");
            etUniversity.setText("Iowa State University");
            etGPA.setText("2.2");
            etGradDate.setText("Fall 2014");
            etResumePath.setText("https://resume.path/yanchui-resume");
            etPassword.setText("********");
        }
        private void updateProfile() {
            // Logic to update profile information
            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
        }
    }

