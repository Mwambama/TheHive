package com.example.hiveeapp.company_user.handleEmployers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hiveeapp.R;

import org.json.JSONObject;

public class AddEmployerActivity extends AppCompatActivity {

    private EditText nameField, emailField, phoneField, streetField, cityField, stateField, zipField;
    private Button addEmployerButton;
    private ImageButton backArrowIcon;

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
                // Use the EmployerApi method to send the data to the server
                EmployerApi.addEmployer(this, name, email, phone, street, city, state, zip,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Handle success response
                                Toast.makeText(AddEmployerActivity.this, "Employer added successfully!", Toast.LENGTH_SHORT).show();
                                // Navigate back to EmployerListActivity and refresh the list
                                Intent intent = new Intent(AddEmployerActivity.this, EmployerListActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the backstack and go to EmployerListActivity
                                startActivity(intent);
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error response
                                Toast.makeText(AddEmployerActivity.this, "Error adding employer: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}