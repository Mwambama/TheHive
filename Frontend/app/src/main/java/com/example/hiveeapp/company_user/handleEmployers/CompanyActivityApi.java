package com.example.hiveeapp.company_user.handleEmployers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.company_user.CompanyActivity;
import com.example.hiveeapp.company_user.invitations.AddInvitationActivity;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CompanyActivityApi extends AppCompatActivity {

    private EditText nameField, emailField, phoneField, streetField, cityField, stateField, zipField;
    private Button addEmployerButton, deleteEmployerButton, updateEmployerButton, getEmployersButton;
    private TextView textView;
    private ListView employersListView;
    private JSONArray employerArray;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> employerList = new ArrayList<>();
    private int selectedEmployerId = -1;

    // URL for POST, DELETE, GET, and PUT requests
    private final String baseUrl = "https://2e7fd141-9a29-4eae-ac7f-4996f5e64e96.mock.pstmn.io/";

    private static final String EMPLOYERS_FILE = "employers.json";

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
        updateEmployerButton = findViewById(R.id.updateEmployerButton);
        getEmployersButton = findViewById(R.id.getEmployersButton);
        textView = findViewById(R.id.textView);
        employersListView = findViewById(R.id.employersListView);

        // Back Arrow Icon Logic
        ImageButton backArrowIcon = findViewById(R.id.backArrowIcon);
        backArrowIcon.setOnClickListener(v -> {
            // Navigate back to the parent activity
            Intent intent = new Intent(CompanyActivityApi.this, CompanyActivity.class);
            startActivity(intent);
            finish();
        });

        // Icon Button Logic
        ImageButton manageEmployersIcon = findViewById(R.id.manageEmployersIcon);
        manageEmployersIcon.setOnClickListener(v -> {
            Toast.makeText(CompanyActivityApi.this, "You are already on this page", Toast.LENGTH_SHORT).show();
        });

        ImageButton manageInvitationsIcon = findViewById(R.id.manageInvitationsIcon);
        manageInvitationsIcon.setOnClickListener(v -> {
            // Navigate to manage invitations activity
            Intent intent = new Intent(CompanyActivityApi.this, AddInvitationActivity.class);
            startActivity(intent);
        });

        ImageButton mainUserPageIcon = findViewById(R.id.mainUserPageIcon);
        mainUserPageIcon.setOnClickListener(v -> {
            // Navigate to company main activity
            Intent intent = new Intent(CompanyActivityApi.this, CompanyActivity.class);
            startActivity(intent);
        });


        // Initialize JSON array to store employer data
        employerArray = new JSONArray();

        // Initialize ListView and Adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, employerList);
        employersListView.setAdapter(adapter);
        employersListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Load employers from local file
        loadEmployers();

        // Set item click listener for ListView
        employersListView.setOnItemClickListener((parent, view, position, id) -> {
            try {
                JSONObject selectedEmployer = employerArray.getJSONObject(position);
                selectedEmployerId = selectedEmployer.getInt("id");

                // Populate fields with selected employer data
                nameField.setText(selectedEmployer.getString("name"));
                emailField.setText(selectedEmployer.getString("email"));
                phoneField.setText(selectedEmployer.getString("phone"));

                JSONObject address = selectedEmployer.getJSONObject("address");
                streetField.setText(address.getString("street"));
                cityField.setText(address.getString("city"));
                stateField.setText(address.getString("state"));
                zipField.setText(address.getString("zip_code"));

                textView.setText("Selected Employer ID: " + selectedEmployerId);

            } catch (JSONException e) {
                e.printStackTrace();
                textView.setText("Error retrieving employer details.");
            }
        });

        // Set click listener for adding employers
        addEmployerButton.setOnClickListener(v -> addEmployer());

        // Set click listener for deleting selected employer
        deleteEmployerButton.setOnClickListener(v -> {
            if (selectedEmployerId != -1) {
                deleteEmployer(selectedEmployerId);
            } else {
                Toast.makeText(this, "Please select an employer to delete.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for updating selected employer
        updateEmployerButton.setOnClickListener(v -> {
            if (selectedEmployerId != -1) {
                updateEmployer(selectedEmployerId);
            } else {
                Toast.makeText(this, "Please select an employer to update.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for getting employers
        getEmployersButton.setOnClickListener(v -> getEmployers());
    }

    // Load employers from local file
    private void loadEmployers() {
        employerArray = readEmployersFromFile(this);
        refreshEmployerList();
    }

    // Add a new employer to the JSON array and send the data to the server
    private void addEmployer() {
        try {
            JSONObject employer = new JSONObject();
            employer.put("name", nameField.getText().toString().trim());
            employer.put("email", emailField.getText().toString().trim());
            employer.put("phone", phoneField.getText().toString().trim());

            JSONObject address = new JSONObject();
            address.put("street", streetField.getText().toString().trim());
            address.put("city", cityField.getText().toString().trim());
            address.put("state", stateField.getText().toString().trim());
            address.put("zip_code", zipField.getText().toString().trim());

            employer.put("address", address);

            // Generate new ID
            int newId = generateNewId(employerArray);
            employer.put("id", newId);

            // Add to employer array
            employerArray.put(employer);

            // Write back to file
            writeEmployersToFile(this, employerArray);

            // Send the data to the server via POST request
            sendEmployerDataToServer(employer, Request.Method.POST, baseUrl + "add", "Add Response");

            refreshEmployerList();

            clearFields();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Delete the selected employer from the JSON array and send a request to the server
    private void deleteEmployer(int employerId) {
        try {
            // Find and remove employer with the given id
            boolean found = false;
            for (int i = 0; i < employerArray.length(); i++) {
                JSONObject employer = employerArray.getJSONObject(i);
                if (employer.getInt("id") == employerId) {
                    employerArray.remove(i);
                    found = true;
                    break;
                }
            }

            if (found) {
                // Write back to file
                writeEmployersToFile(this, employerArray);

                // Send delete request to server
                sendDeleteRequestToServer(employerId);

                refreshEmployerList();

                clearFields();

                selectedEmployerId = -1;
            } else {
                Toast.makeText(this, "Employer not found.", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Update the selected employer in the JSON array and send the data to the server
    private void updateEmployer(int employerId) {
        try {
            // Find the employer with the given id
            boolean found = false;
            for (int i = 0; i < employerArray.length(); i++) {
                JSONObject employer = employerArray.getJSONObject(i);
                if (employer.getInt("id") == employerId) {
                    // Update employer data
                    employer.put("name", nameField.getText().toString().trim());
                    employer.put("email", emailField.getText().toString().trim());
                    employer.put("phone", phoneField.getText().toString().trim());

                    JSONObject address = employer.getJSONObject("address");
                    address.put("street", streetField.getText().toString().trim());
                    address.put("city", cityField.getText().toString().trim());
                    address.put("state", stateField.getText().toString().trim());
                    address.put("zip_code", zipField.getText().toString().trim());

                    employer.put("address", address);

                    found = true;
                    break;
                }
            }

            if (found) {
                // Write back to file
                writeEmployersToFile(this, employerArray);

                // Send the updated data to the server via PUT request
                JSONObject updatedEmployer = employerArray.getJSONObject(selectedEmployerId);
                sendEmployerDataToServer(updatedEmployer, Request.Method.PUT, baseUrl + "update/" + employerId, "Update Response");

                refreshEmployerList();

                clearFields();

                selectedEmployerId = -1;
            } else {
                Toast.makeText(this, "Employer not found.", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Get employers from the local file and display them
    private void getEmployers() {
        employerArray = readEmployersFromFile(this);
        refreshEmployerList();
    }

    // Helper method to refresh the ListView
    private void refreshEmployerList() {
        employerList.clear();
        for (int i = 0; i < employerArray.length(); i++) {
            try {
                JSONObject employer = employerArray.getJSONObject(i);
                int id = employer.getInt("id");
                String name = employer.getString("name");
                employerList.add("ID: " + id + ", Name: " + name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }

    // Helper method to clear input fields
    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        streetField.setText("");
        cityField.setText("");
        stateField.setText("");
        zipField.setText("");
    }

    // Method to send the JSON data to the server and handle the response
    private void sendEmployerDataToServer(JSONObject employerJson, int method, String url, String responsePrefix) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                method, url, employerJson,
                response -> {
                    textView.setText(responsePrefix + ": " + response.toString());
                },
                error -> {
                    if (error.networkResponse != null) {
                        textView.setText("Error: " + new String(error.networkResponse.data));
                    } else {
                        textView.setText("Error: " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the queue
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    // Method to send the delete request to the server and handle the response
    private void sendDeleteRequestToServer(int employerId) {
        String url = baseUrl + "delete/" + employerId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE, url, null,
                response -> {
                    textView.setText("Delete Response: " + response.toString());
                },
                error -> {
                    if (error.networkResponse != null) {
                        textView.setText("Error: " + new String(error.networkResponse.data));
                    } else {
                        textView.setText("Error: " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the queue
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    // Helper method to read employers from file
    private JSONArray readEmployersFromFile(Context context) {
        try {
            FileInputStream fis = context.openFileInput(EMPLOYERS_FILE);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            String jsonString = new String(data, "UTF-8");
            return new JSONArray(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            // If file does not exist or is empty, return an empty array
            return new JSONArray();
        }
    }

    // Helper method to write employers to file
    private void writeEmployersToFile(Context context, JSONArray employers) {
        try {
            String jsonString = employers.toString();
            FileOutputStream fos = context.openFileOutput(EMPLOYERS_FILE, Context.MODE_PRIVATE);
            fos.write(jsonString.getBytes("UTF-8"));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to generate a new ID
    private int generateNewId(JSONArray employers) {
        int newId = 1;
        try {
            for (int i = 0; i < employers.length(); i++) {
                JSONObject employer = employers.getJSONObject(i);
                int id = employer.getInt("id");
                if (id >= newId) {
                    newId = id + 1;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newId;
    }
}