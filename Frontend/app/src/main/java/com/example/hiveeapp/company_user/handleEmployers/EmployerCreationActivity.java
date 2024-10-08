package com.example.hiveeapp.company_user.handleEmployers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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

public class EmployerCreationActivity extends AppCompatActivity {

    private EditText nameField, emailField, phoneField, streetField, complementField, cityField, stateField, zipField;
    private Button addEmployerButton;
    private ImageButton backArrowIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employer);

        // Initialize views
        initViews();

        // Set the back arrow functionality to navigate back
        backArrowIcon.setOnClickListener(v -> {
            finish(); // This will close the current activity and go back to the previous one.
        });

        // Check if editing an existing employer
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("employerId")) {
            // Pre-fill the fields with employer data
            int employerId = intent.getIntExtra("employerId", -1);
            nameField.setText(intent.getStringExtra("name"));
            emailField.setText(intent.getStringExtra("email"));
            phoneField.setText(intent.getStringExtra("phone"));
            streetField.setText(intent.getStringExtra("street"));
            complementField.setText(intent.getStringExtra("complement"));
            cityField.setText(intent.getStringExtra("city"));
            stateField.setText(intent.getStringExtra("state"));
            zipField.setText(intent.getStringExtra("zip_code"));

            addEmployerButton.setText("Update Employer"); // Change the button text
            addEmployerButton.setOnClickListener(v -> {
                if (validateInput()) {
                    try {
                        // Construct a JSONObject for the employer data
                        JSONObject employerData = new JSONObject();
                        employerData.put("id", employerId);
                        employerData.put("name", nameField.getText().toString().trim());
                        employerData.put("email", emailField.getText().toString().trim());
                        employerData.put("phone", phoneField.getText().toString().trim());

                        JSONObject address = new JSONObject();
                        address.put("street", streetField.getText().toString().trim());
                        address.put("complement", complementField.getText().toString().trim());
                        address.put("city", cityField.getText().toString().trim());
                        address.put("state", stateField.getText().toString().trim());
                        address.put("zipCode", zipField.getText().toString().trim());

                        employerData.put("address", address);

                        // Call the updateEmployer method with the constructed JSONObject
                        EmployerApi.updateEmployer(
                                this,
                                employerData,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(EmployerCreationActivity.this, "Employer updated successfully!", Toast.LENGTH_SHORT).show();
                                        finish(); // Close the activity after successful update
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        String errorMessage = getErrorMessage(error);
                                        Toast.makeText(EmployerCreationActivity.this, "Error updating employer: " + errorMessage, Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error constructing employer data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // Handle adding a new employer
            addEmployerButton.setOnClickListener(v -> {
                if (validateInput()) {
                    addEmployer();
                }
            });
        }
    }

    /**
     * Initialize all views in the activity
     */
    private void initViews() {
        nameField = findViewById(R.id.nameField);
        emailField = findViewById(R.id.emailField);
        phoneField = findViewById(R.id.phoneField);
        streetField = findViewById(R.id.streetField);
        complementField = findViewById(R.id.complementField);
        cityField = findViewById(R.id.cityField);
        stateField = findViewById(R.id.stateField);
        zipField = findViewById(R.id.zipField);
        addEmployerButton = findViewById(R.id.addEmployerButton);
        backArrowIcon = findViewById(R.id.backArrowIcon);
    }

    /**
     * Validates the input fields and provides feedback if any input is invalid
     */
    private boolean validateInput() {
        boolean isValid = true;

        // Reset previous error messages
        nameField.setError(null);
        emailField.setError(null);

        String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();
        String street = streetField.getText().toString().trim();
        String complement = complementField.getText().toString().trim();
        String city = cityField.getText().toString().trim();
        String state = stateField.getText().toString().trim();
        String zip = zipField.getText().toString().trim();

        // Validate name field
        if (name.isEmpty()) {
            nameField.setError("Name is required");
            isValid = false;
        }

        // Validate email field
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Valid email is required");
            isValid = false;
        }

        // Validate other fields if needed
        if (phone.isEmpty()) {
            phoneField.setError("Phone number is required");
            isValid = false;
        }

        if (street.isEmpty()) {
            streetField.setError("Street address is required");
            isValid = false;
        }

        if (city.isEmpty()) {
            cityField.setError("City is required");
            isValid = false;
        }

        if (state.isEmpty()) {
            stateField.setError("State is required");
            isValid = false;
        }

        if (zip.isEmpty()) {
            zipField.setError("Zip code is required");
            isValid = false;
        }

        return isValid;
    }

    /**
     * Adds a new employer by calling the Employer API
     */
    private void addEmployer() {
        String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();
        String street = streetField.getText().toString().trim();
        String complement = complementField.getText().toString().trim();
        String city = cityField.getText().toString().trim();
        String state = stateField.getText().toString().trim();
        String zip = zipField.getText().toString().trim();

        // Call the EmployerApi to add the employer
        EmployerApi.addEmployer(this, name, email, phone, street, complement, city, state, zip,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the success response
                        Toast.makeText(EmployerCreationActivity.this, "Employer added successfully!", Toast.LENGTH_SHORT).show();
                        // Finish the activity to return to EmployerListActivity
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        String errorMessage = getErrorMessage(error);
                        Toast.makeText(EmployerCreationActivity.this, "Error adding employer: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Extracts and returns a meaningful error message from the VolleyError object
     */
    private String getErrorMessage(VolleyError error) {
        String errorMsg = "An unexpected error occurred";
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String errorData = new String(error.networkResponse.data, "UTF-8");
                JSONObject jsonError = new JSONObject(errorData);
                errorMsg = jsonError.optString("message", errorMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (error.getMessage() != null) {
            errorMsg = error.getMessage();
        }
        return errorMsg;
    }
}