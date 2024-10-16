package com.example.hiveeapp.employer_user.setting;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class employerinfoActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText streetEditText;
    private EditText complementEditText;
    private EditText cityEditText;
    private EditText stateEditText;
    private EditText zipCodeEditText;

    private static final String BASE_URL = "http://coms-3090-063.class.las.iastate.edu:8080/employer"; // Replace with your actual API URL
    private String employerId; // Unique identifier for the employer

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

        Button updateButton = findViewById(R.id.update_button);
        Button deleteButton = findViewById(R.id.delete_button);

        // Fetch initial employer data (this could be a specific employer's ID)
        employerId = "311"; // Set the employer ID you want to fetch
        fetchEmployerInfo(employerId);

        updateButton.setOnClickListener(v -> updateEmployerInfo());
        deleteButton.setOnClickListener(v -> deleteEmployerInfo());
    }

    // Fetch employer information from the server
    private void fetchEmployerInfo(String employerId) {
        String url = BASE_URL + "/" + employerId; // Construct URL for fetching

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
            streetEditText.setText(response.getJSONObject("address").getString("street"));
            complementEditText.setText(response.getJSONObject("address").getString("complement"));
            cityEditText.setText(response.getJSONObject("address").getString("city"));
            stateEditText.setText(response.getJSONObject("address").getString("state"));
            zipCodeEditText.setText(response.getJSONObject("address").getString("zipCode"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing employer info", Toast.LENGTH_SHORT).show();
        }
    }

    // Update employer information
    private void updateEmployerInfo() {
        String url = BASE_URL; // Construct URL for updating

        JSONObject jobData = new JSONObject();
        try {
            jobData.put("name", nameEditText.getText().toString());
            jobData.put("email", emailEditText.getText().toString());
            jobData.put("phone", phoneEditText.getText().toString());
            jobData.put("street", streetEditText.getText().toString());
            jobData.put("complement", complementEditText.getText().toString());
            jobData.put("city", cityEditText.getText().toString());
            jobData.put("state", stateEditText.getText().toString());
            jobData.put("zipCode", zipCodeEditText.getText().toString());
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
        );

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    // Delete employer information
    private void deleteEmployerInfo() {
        String url = BASE_URL + "/delete/" + employerId; // Construct URL for deleting

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
