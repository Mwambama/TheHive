package com.example.hiveeapp.company_user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class AddDeleteCompanyActivity extends AppCompatActivity {

    private EditText nameField, emailField, phoneField, streetField, cityField, stateField, zipField;
    private Button addEmployerButton, deleteEmployerButton;
    private TextView textView;
    private JSONArray employerArray;  // Store employers in JSON array

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

    // Add a new employer to the JSON array and update the display
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
            updateJSONFile("create_company1.json");
            displayJSON();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Delete the last employer from the JSON array and update the display
    private void deleteLastEmployer() {
        if (employerArray.length() > 0) {
            employerArray.remove(employerArray.length() - 1);
            updateJSONFile("delete_company1.json");
            displayJSON();
        }
    }

    // Update the local JSON file with the current employer data
    private void updateJSONFile(String fileName) {
        try {
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);

            String jsonString = employerArray.toString();
            writer.close();
            fos.close();

            // Log the file contents and location (test)
            String filePath = getFilesDir().getAbsolutePath() + "/" + fileName;
            System.out.println("JSON saved to: " + filePath);
            System.out.println("JSON Content: " + jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Display the current employer data in the TextView
    private void displayJSON() {
        textView.setText(employerArray.toString());
    }
}