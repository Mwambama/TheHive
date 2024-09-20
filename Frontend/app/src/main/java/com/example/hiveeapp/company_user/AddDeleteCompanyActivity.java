package com.example.hiveeapp.company_user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddDeleteCompanyActivity extends AppCompatActivity {

    private EditText nameField, emailField, phoneField, streetField, cityField, stateField, zipField;
    private Button addEmployerButton, deleteEmployerButton;
    private TextView textView;
    private JSONArray employerArray;

    private final String postUrl = "https://39105a8a-a3d7-41a3-b90b-9e4031e56567.mock.pstmn.io";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adddelete_company);

        // Initialize fields and buttons
        nameField = findViewById(R.id.nameField);
        emailField = findViewById(R.id.emailField);
        phoneField = findViewById(R.id.phoneField);
        streetField = findViewById(R.id.streetField);
        cityField = findViewById(R.id.cityField);
        stateField = findViewById(R.id.stateField);
        zipField = findViewById(R.id.zipField);
        addEmployerButton = findViewById(R.id.addEmployerButton);
        deleteEmployerButton = findViewById(R.id.deleteEmployerButton);
        textView = findViewById(R.id.textView);

        // Initialize JSON array to store employer data
        employerArray = new JSONArray();

        // Set click listener for adding employers
        addEmployerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmployer();
            }
        });

        // Set click listener for deleting the last employer
        deleteEmployerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLastEmployer();
            }
        });
    }

    // Add a new employer to the JSON array and send the data to the server
    private void addEmployer() {
        try {
            JSONObject employer = new JSONObject();
            employer.put("name", nameField.getText().toString());
            employer.put("email", emailField.getText().toString());
            employer.put("phone", phoneField.getText().toString());

            JSONObject address = new JSONObject();
            address.put("street", streetField.getText().toString());
            address.put("city", cityField.getText().toString());
            address.put("state", stateField.getText().toString());
            address.put("zip_code", zipField.getText().toString());

            employer.put("address", address);

            employerArray.put(employer);

            // Send the data to the server via POST request
            sendEmployerDataToServer(employer);

            displayJSON();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Delete the last employer from the JSON array and update the display
    private void deleteLastEmployer() {
        if (employerArray.length() > 0) {
            employerArray.remove(employerArray.length() - 1);
            displayJSON();
        }
    }

    // Display the current employer data in the TextView
    private void displayJSON() {
        textView.setText(employerArray.toString());
    }

    // Method to send the JSON data to Postman and handle the response
    private void sendEmployerDataToServer(JSONObject employerJson) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, postUrl, employerJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle valid response here
                        if (response != null && response.length() > 0) {
                            textView.setText("Response: " + response.toString());
                        } else {
                            textView.setText("No response from server");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            textView.setText("Error: " + new String(error.networkResponse.data));
                        } else {
                            textView.setText("Error: " + error.getMessage());
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the queue
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
