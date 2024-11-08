package com.example.hiveeapp.employer_user.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hiveeapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewEmployerInfoActivity extends AppCompatActivity {

    private static final String TAG = "EmployerProfileView";
    private int userId;
    private int companyId; // Add this line
    private EditText nameEditText, emailEditText, phoneEditText, streetEditText, complementEditText, cityEditText, stateEditText, zipCodeEditText, fieldEditText;
    private Button updateInfoButton;
    private ImageButton backArrowIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ViewEmployerInfoActivity started");
        setContentView(R.layout.activity_employer_info);

        // Retrieve userId and companyId from Intent
        userId = getIntent().getIntExtra("USER_ID", -1);
        companyId = getIntent().getIntExtra("COMPANY_ID", -1); // Add this line
        Log.d(TAG, "Received userId from Intent: " + userId);
        Log.d(TAG, "Received companyId from Intent: " + companyId); // Add this line

        if (userId == -1) {
            Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Invalid userId. Cannot load profile.");
            return;
        }

        if (companyId == -1) { // Add this block
            Toast.makeText(this, "Invalid Company ID", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Invalid companyId. Cannot load profile.");
            return;
        }

        // Initialize EditTexts and Button with the corresponding views in XML
        nameEditText = findViewById(R.id.profileName);
        emailEditText = findViewById(R.id.profileEmail);
        phoneEditText = findViewById(R.id.profilePhone);
        streetEditText = findViewById(R.id.profileStreet);
        complementEditText = findViewById(R.id.profileComplement);
        cityEditText = findViewById(R.id.profileCity);
        stateEditText = findViewById(R.id.profileState);
        zipCodeEditText = findViewById(R.id.profileZipCode);
        fieldEditText = findViewById(R.id.profileField);
        updateInfoButton = findViewById(R.id.updateInfoButton);

        // Load employer information from the backend
        loadEmployerProfile(userId);

        // Handle Update button click
        updateInfoButton.setOnClickListener(v -> {
            Log.d(TAG, "Update button clicked, navigating to EmployerProfileActivity");
            Intent intent = new Intent(ViewEmployerInfoActivity.this, EmployerProfileActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("COMPANY_ID", companyId); // Add this line
            startActivity(intent);
        });
    }

    private void loadEmployerProfile(int userId) {
        Log.d(TAG, "Loading employer profile with userId: " + userId);

        employerinfoApi.getEmployer(this, userId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject employer) {
                try {
                    displayProfile(employer);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ViewEmployerInfoActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewEmployerInfoActivity.this, "Error fetching profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProfile(JSONObject employer) throws JSONException {
        Log.d(TAG, "Displaying profile data for employer: " + employer.optString("name"));

        // Set basic profile fields
        nameEditText.setText(employer.optString("name"));
        emailEditText.setText(employer.optString("email"));
        phoneEditText.setText(employer.optString("phone"));
        fieldEditText.setText(employer.optString("field"));

        // Set address fields
        JSONObject address = employer.optJSONObject("address");
        if (address != null) {
            streetEditText.setText(address.optString("street"));
            complementEditText.setText(address.optString("complement"));
            cityEditText.setText(address.optString("city"));
            stateEditText.setText(address.optString("state"));
            zipCodeEditText.setText(address.optString("zipCode"));
        } else {
            streetEditText.setText("");
            complementEditText.setText("");
            cityEditText.setText("");
            stateEditText.setText("");
            zipCodeEditText.setText("");
        }
    }
}