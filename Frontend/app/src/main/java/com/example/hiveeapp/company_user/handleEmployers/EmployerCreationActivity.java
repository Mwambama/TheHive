package com.example.hiveeapp.company_user.handleEmployers;

import android.util.Patterns;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.VolleyError;
import com.example.hiveeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * EmployerCreationActivity allows the user to add a new employer by filling out a form.
 * The form includes fields like name, email, phone number, address, etc., which are validated before submission.
 */
public class EmployerCreationActivity extends AppCompatActivity {

    // UI Components for employer creation form
    private EditText nameField, emailField, phoneField, streetField, complementField, cityField, stateField, zipField;
    private Button addEmployerButton;
    private ImageButton backArrowIcon;

    // Constants for validation
    private static final int MAX_PHONE_LENGTH = 10;  // Maximum phone number length allowed
    private static final int MIN_PHONE_LENGTH = 7;   // Minimum phone number length required
    private static final int ZIP_CODE_LENGTH = 5;    // Standard US ZIP code length
    private static final String USER_PREFS = "MyAppPrefs";  // SharedPreferences key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employer);

        // Initialize the input fields and buttons
        initViews();

        // Set up back arrow functionality to close the current activity and return to the previous one
        backArrowIcon.setOnClickListener(v -> finish());

        // Handle the "Add Employer" button click and validate inputs
        addEmployerButton.setOnClickListener(v -> {
            if (validateInput()) {
                addEmployer();  // Proceed to add the employer if inputs are valid
            }
        });
    }

    /**
     * Initializes the input fields and buttons in the activity layout.
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
     * Validates the input fields to ensure that all required fields are filled and valid.
     *
     * @return true if all fields are valid, false otherwise
     */
    private boolean validateInput() {
        boolean isValid = true;

        // Reset any previous error messages
        nameField.setError(null);
        emailField.setError(null);
        phoneField.setError(null);
        streetField.setError(null);
        cityField.setError(null);
        stateField.setError(null);
        zipField.setError(null);

        // Get the input values from the fields
        String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();
        String street = streetField.getText().toString().trim();
        String city = cityField.getText().toString().trim();
        String state = stateField.getText().toString().trim();
        String zip = zipField.getText().toString().trim();

        // Validate the name field
        if (name.isEmpty()) {
            nameField.setError("Name is required");
            isValid = false;
        }

        // Validate the email field using Patterns.EMAIL_ADDRESS
        if (email.isEmpty()) {
            emailField.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Invalid email format");
            isValid = false;
        }

        // Validate the phone field
        if (phone.isEmpty()) {
            phoneField.setError("Phone number is required");
            isValid = false;
        } else if (phone.length() > MAX_PHONE_LENGTH || phone.length() < MIN_PHONE_LENGTH) {
            phoneField.setError("Phone number must be between " + MIN_PHONE_LENGTH + " and " + MAX_PHONE_LENGTH + " digits");
            isValid = false;
        } else if (!phone.matches("\\d+")) {
            phoneField.setError("Phone number must contain only digits");
            isValid = false;
        }

        // Validate the address fields
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

        // Validate the ZIP code format (assuming US ZIP code)
        if (zip.isEmpty()) {
            zipField.setError("Zip code is required");
            isValid = false;
        } else if (zip.length() != ZIP_CODE_LENGTH || !zip.matches("\\d{" + ZIP_CODE_LENGTH + "}")) {
            zipField.setError("Zip code must be " + ZIP_CODE_LENGTH + " digits");
            isValid = false;
        }

        return isValid;
    }

    /**
     * Constructs a JSON object with employer details and sends a request to add a new employer.
     */
    private void addEmployer() {
        // Get the values from the input fields
        String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();
        String street = streetField.getText().toString().trim();
        String complement = complementField.getText().toString().trim();
        String city = cityField.getText().toString().trim();
        String state = stateField.getText().toString().trim();
        String zipCode = zipField.getText().toString().trim();

        // Retrieve the companyId from SharedPreferences (or use a hardcoded value for testing)
        int companyId = 1029;  // This is a test company ID

        // Construct the employer JSON object
        JSONObject employerData = new JSONObject();
        try {
            employerData.put("name", name);
            employerData.put("email", email);
            employerData.put("phone", phone);
            employerData.put("role", "EMPLOYER");
            employerData.put("companyId", companyId);
            employerData.put("field", JSONObject.NULL);
            employerData.put("jobPostings", new JSONArray());

            // Construct the address JSON object
            JSONObject address = new JSONObject();
            address.put("addressId", JSONObject.NULL);
            address.put("street", street);
            address.put("complement", complement.isEmpty() ? JSONObject.NULL : complement);
            address.put("city", city);
            address.put("state", state);
            address.put("zipCode", zipCode);

            employerData.put("address", address);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating employer data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send the employer data to the server using the EmployerApi
        EmployerApi.addEmployerWithAddress(this, employerData,
                response -> {
                    // Handle successful response
                    Toast.makeText(EmployerCreationActivity.this, "Employer added successfully!", Toast.LENGTH_SHORT).show();
                    finish();  // Close the activity and return to the previous screen
                },
                error -> {
                    // Handle error response and display user-friendly message
                    String errorMessage = getErrorMessage(error);
                    Toast.makeText(EmployerCreationActivity.this, "Error adding employer: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Extracts and returns a meaningful error message from a VolleyError.
     *
     * @param error The VolleyError object containing the error details
     * @return A user-friendly error message
     */
    private String getErrorMessage(VolleyError error) {
        String errorMsg = "An unexpected error occurred";
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String errorData = new String(error.networkResponse.data, "UTF-8");

                // Attempt to parse errorData as JSON
                try {
                    JSONObject jsonError = new JSONObject(errorData);
                    errorMsg = jsonError.optString("message", errorMsg);
                } catch (JSONException jsonException) {
                    // If parsing fails, use the raw errorData
                    errorMsg = errorData;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                errorMsg = "Error parsing error message";
            }
        } else if (error.getMessage() != null) {
            errorMsg = error.getMessage();
        }
        return errorMsg;
    }
}