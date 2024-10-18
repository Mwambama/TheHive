package com.example.hiveeapp.employer_user.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewEmployerInfoActivity extends AppCompatActivity {

    private EditText nameField, emailField, phoneField, streetField, complementField, cityField, stateField, zipField, field;
    private Button updateButton, deleteButton;
    private ImageButton backArrowIcon;

    private static final int MAX_PHONE_LENGTH = 15;
    private static final int MIN_PHONE_LENGTH = 7;
    private static final int ZIP_CODE_LENGTH = 5;

    private static final String USER_PREFS = "MyAppPrefs"; // Shared preferences key
    private static final String COMPANY_ID_KEY = "companyId"; // Key to store/retrieve companyId from SharedPreferences

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_info);

        // Initialize the input fields and buttons
        initViews();

        // Set up back arrow functionality to close the current activity and return to the previous one
        backArrowIcon.setOnClickListener(v -> finish());

        // Fetch employer information
        fetchEmployerInfo();

        // Handle the "Update" button click and validate inputs
        updateButton.setOnClickListener(v -> {
            if (validateInput()) {
                try {
                    updateEmployerInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ViewEmployerInfoActivity.this, "Error updating employer information.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle the "Delete" button click
        deleteButton.setOnClickListener(v -> deleteEmployerInfo());
    }

    private void initViews() {
        nameField = findViewById(R.id.name);
        emailField = findViewById(R.id.email);
        phoneField = findViewById(R.id.phone);
        streetField = findViewById(R.id.street);
        complementField = findViewById(R.id.complement);
        cityField = findViewById(R.id.city);
        stateField = findViewById(R.id.state);
        zipField = findViewById(R.id.zip_code);
        field = findViewById(R.id.field);
        updateButton = findViewById(R.id.update_button);
        deleteButton = findViewById(R.id.delete_button);
        backArrowIcon = findViewById(R.id.backArrowIcon);
    }

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

    private void fetchEmployerInfo() {
        // Retrieve the companyId from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        int companyId = prefs.getInt(COMPANY_ID_KEY, 177);  // Default to 177 for testing

        String url = "http://coms-3090-063.class.las.iastate.edu:8080/employer?companyId=" + companyId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Log the complete JSON response
                Log.d("ViewEmployerInfoActivity", "Response: " + response.toString());
                try {
                    nameField.setText(response.getString("name"));
                    emailField.setText(response.getString("email"));
                    phoneField.setText(response.getString("phone"));
                    JSONObject address = response.getJSONObject("address");
                    streetField.setText(address.getString("street"));
                    complementField.setText(address.getString("complement"));
                    cityField.setText(address.getString("city"));
                    stateField.setText(address.getString("state"));
                    zipField.setText(address.getString("zipCode"));
                    field.setText(response.getString("field"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ViewEmployerInfoActivity.this, "Error parsing employer information.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewEmployerInfoActivity.this, getErrorMessage(error), Toast.LENGTH_SHORT).show();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void updateEmployerInfo() throws JSONException {
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/employer"; // Replace with actual employer ID
        JSONObject employerData = new JSONObject();
        employerData.put("name", nameField.getText().toString().trim());
        employerData.put("email", emailField.getText().toString().trim());
        employerData.put("phone", phoneField.getText().toString().trim());
        JSONObject addressData = new JSONObject();
        addressData.put("street", streetField.getText().toString().trim());
        addressData.put("complement", complementField.getText().toString().trim());
        addressData.put("city", cityField.getText().toString().trim());
        addressData.put("state", stateField.getText().toString().trim());
        addressData.put("zipCode", zipField.getText().toString().trim());
        employerData.put("address", addressData);
        employerData.put("field", field.getText().toString().trim());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, url, employerData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(ViewEmployerInfoActivity.this, "Employer information updated successfully.", Toast.LENGTH_SHORT).show();
                finish();  // Close the activity and return to the previous screen
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewEmployerInfoActivity.this, getErrorMessage(error), Toast.LENGTH_SHORT).show();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void deleteEmployerInfo() {
        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        int companyId = prefs.getInt(COMPANY_ID_KEY, 203); // Default to 203 for testing

        String url = "http://coms-3090-063.class.las.iastate.edu:8080/employer?companyId=" + companyId;
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ViewEmployerInfoActivity.this, "Employer information deleted successfully.", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity after deletion
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewEmployerInfoActivity.this, getErrorMessage(error), Toast.LENGTH_SHORT).show();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private String getErrorMessage(VolleyError error) {
        if (error.networkResponse != null && error.networkResponse.data != null) {
            String responseData = new String(error.networkResponse.data);
            Log.e("Volley Error Response", responseData);
            return "Error: " + responseData; // Customize error message based on response
        }
        return "Network error occurred. Please try again.";
    }
}
