package com.example.hiveeapp.employer_user.setting;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class employerinfoActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText streetEditText;
    private EditText complementEditText;
    private EditText cityEditText;
    private EditText stateEditText;
    private EditText zipCodeEditText;
    private EditText fieldEditText;

    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/student"; // Replace with your actual API URL
    private String userId; // Unique identifier for the user
    private String addressId = "312"; // Hardcoded for testing
    private String companyId; // Unique identifier for the company

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_info);

        // Initialize EditTexts for employer information input
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone);
        streetEditText = findViewById(R.id.street);
        complementEditText = findViewById(R.id.complement);
        cityEditText = findViewById(R.id.city);
        stateEditText = findViewById(R.id.state);
        zipCodeEditText = findViewById(R.id.zip_code);
        fieldEditText = findViewById(R.id.field);

        Button updateButton = findViewById(R.id.update_button);
        Button deleteButton = findViewById(R.id.delete_button);

        // Set the user ID you want to fetch (same as employerId)
        userId = "312";

        // Fetch employer info when activity starts
        fetchEmployerInfo(userId);

        // Set listeners for the buttons
        updateButton.setOnClickListener(v -> updateEmployerInfo());
        deleteButton.setOnClickListener(v -> deleteEmployerInfo());
    }

    // Fetch employer information from the server
    private void fetchEmployerInfo(String userId) {
        String url = BASE_URL + "/get/" + userId; // Construct URL for fetching

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        populateEmployerInfo(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(employerinfoActivity.this, "Error fetching employer info", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    // Populate EditTexts with fetched employer info
    private void populateEmployerInfo(JSONObject response) {
        try {
            nameEditText.setText(response.getString("name"));
            emailEditText.setText(response.getString("email"));
            phoneEditText.setText(response.getString("phone"));
            fieldEditText.setText(response.getString("field"));

            JSONObject address = response.getJSONObject("address");
            streetEditText.setText(address.getString("street"));
            complementEditText.setText(address.getString("complement"));
            cityEditText.setText(address.getString("city"));
            stateEditText.setText(address.getString("state"));
            zipCodeEditText.setText(address.getString("zipCode"));

            // Save IDs for updating
            userId = response.getString("userId");
            addressId = address.getString("addressId");
            companyId = response.getString("companyId");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing employer info", Toast.LENGTH_SHORT).show();
        }
    }

    // Update employer information
    private void updateEmployerInfo() {
        String url = BASE_URL + "/" + userId; // Construct URL for updating

        JSONObject jobData = new JSONObject();
        try {
            jobData.put("userId", userId);
            jobData.put("name", nameEditText.getText().toString());
            jobData.put("email", emailEditText.getText().toString());
            jobData.put("phone", phoneEditText.getText().toString());
            jobData.put("field", fieldEditText.getText().toString());
            jobData.put("companyId", companyId);

            JSONObject address = new JSONObject();
            address.put("addressId", addressId);
            address.put("street", streetEditText.getText().toString());
            address.put("complement", complementEditText.getText().toString());
            address.put("city", cityEditText.getText().toString());
            address.put("state", stateEditText.getText().toString());
            address.put("zipCode", zipCodeEditText.getText().toString());

            jobData.put("address", address);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                jobData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(employerinfoActivity.this, "Employer info updated successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(employerinfoActivity.this, "Error updating employer info", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = "employer@example.com:Test@1234";
                String auth = "Basic " + android.util.Base64.encodeToString(credentials.getBytes(), android.util.Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    // Delete employer information
    private void deleteEmployerInfo() {
        String url = BASE_URL + "/delete/" + userId; // Construct URL for deleting

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(employerinfoActivity.this, "Employer info deleted successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(employerinfoActivity.this, "Error deleting employer info", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
