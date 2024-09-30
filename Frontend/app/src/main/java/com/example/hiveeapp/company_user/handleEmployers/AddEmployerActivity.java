package com.example.hiveeapp.company_user.handleEmployers;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class AddEmployerActivity extends AppCompatActivity {

    private EditText nameField, emailField, phoneField, streetField, cityField, stateField, zipField;
    private Button addEmployerButton;
    private ImageButton backArrowIcon;
    private static final String SERVER_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employer);

        // Initialize fields
        nameField = findViewById(R.id.nameField);
        emailField = findViewById(R.id.emailField);
        phoneField = findViewById(R.id.phoneField);
        streetField = findViewById(R.id.streetField);
        cityField = findViewById(R.id.cityField);
        stateField = findViewById(R.id.stateField);
        zipField = findViewById(R.id.zipField);
        addEmployerButton = findViewById(R.id.addEmployerButton);
        backArrowIcon = findViewById(R.id.backArrowIcon);

        // Back navigation
        backArrowIcon.setOnClickListener(v -> finish());

        // Add employer logic
        addEmployerButton.setOnClickListener(v -> {
            String name = nameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String phone = phoneField.getText().toString().trim();
            String street = streetField.getText().toString().trim();
            String city = cityField.getText().toString().trim();
            String state = stateField.getText().toString().trim();
            String zip = zipField.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter valid employer details.", Toast.LENGTH_SHORT).show();
            } else {
                // Send data to the server
                sendEmployerDataToServer(name, email, phone, street, city, state, zip);
            }
        });
    }

    // Method to send employer data to the server using Volley
    private void sendEmployerDataToServer(String name, String email, String phone, String street, String city, String state, String zip) {
        JSONObject employerData = new JSONObject();
        try {
            employerData.put("name", name);
            employerData.put("email", email);
            employerData.put("phone", phone);
            employerData.put("street", street);
            employerData.put("city", city);
            employerData.put("state", state);
            employerData.put("zip", zip);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create employer JSON data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a POST request to send employer data to the server
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SERVER_URL, employerData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle success response
                        Toast.makeText(AddEmployerActivity.this, "Employer added successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Return to the previous activity
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Toast.makeText(AddEmployerActivity.this, "Error adding employer: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}