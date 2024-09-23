package com.example.hiveeapp.company_user.handleEmployers;

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

public class CompanyActivityApi extends AppCompatActivity {

    private EditText nameField, emailField, phoneField, streetField, cityField, stateField, zipField;
    private Button addEmployerButton, deleteEmployerButton;
    private TextView textView;
    private JSONArray employerArray;

    // URL for POST and DELETE requests
    private final String postUrl = "https://83b448a1-640e-45cc-bf06-bbb5383d4f84.mock.pstmn.io/add";
    private final String deleteUrl = "https://83b448a1-640e-45cc-bf06-bbb5383d4f84.mock.pstmn.io/delete/1";

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

    // Method to send the JSON data to Postman and handle the response for adding
    private void sendEmployerDataToServer(JSONObject employerJson) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, postUrl, employerJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        textView.setText("Add Response: " + response.toString());
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

    // Delete the last employer from the JSON array and send a request to the server
    private void deleteLastEmployer() {
        if (employerArray.length() > 0) {
            employerArray.remove(employerArray.length() - 1);

            // Create a simple JSON object to send to the server
            JSONObject deletePayload = new JSONObject();
            try {
                deletePayload.put("message", "Employer deleted");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Send the delete request
            sendDeleteRequestToServer(deletePayload);

            displayJSON();
        }
    }

    // Method to send the delete JSON data to the server and handle the response
    private void sendDeleteRequestToServer(JSONObject deleteJson) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE, deleteUrl, deleteJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        textView.setText("Delete Response: " + response.toString());
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

    // Display the current employer data in the TextView
    private void displayJSON() {
        textView.setText(employerArray.toString());
    }
}
