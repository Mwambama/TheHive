package com.example.hiveeapp.volley;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hiveeapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class JsonArrReqActivity extends AppCompatActivity {
    TextView textView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_job);

            textView = findViewById(R.id.textView);

            // Load the JSON data from assets
            String jsonString = loadJSONFromAsset("job_posts.json");

            if (jsonString != null) {
                try {
                    // Parse the JSON array
                    JSONArray jsonArray = new JSONArray(jsonString);
                    StringBuilder jsonData = new StringBuilder();

                    // Loop through the array and append each job's details
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject job = jsonArray.getJSONObject(i);

                        // Get job info
                        String jobTitle = job.getString("jobTitle");
                        String jobDescription = job.getString("jobDescription");
                        String jobType = job.getString("jobType");
                        String salaryRequirements = job.getString("salaryRequirements");
                        String ageRequirement = job.getString("ageRequirement");
                        String minimumGPA = job.getString("minimumGPA");

                        // Build the display string
                        jsonData.append("Job Title: ").append(jobTitle)
                                .append("\nJob Description: ").append(jobDescription)
                                .append("\nJob Type: ").append(jobType)
                                .append("\nSalary Requirements: ").append(salaryRequirements)
                                .append("\nAge Requirement: ").append(ageRequirement)
                                .append("\nMinimum GPA: ").append(minimumGPA)
                                .append("\n\n");
                    }

                    // Display the data in the TextView
                    textView.setText(jsonData.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                    textView.setText("Error parsing JSON data");
                }
            } else {
                textView.setText("Failed to load JSON file");
            }
        }

        private String loadJSONFromAsset(String fileName) {
            String json = null;
            try {
                InputStream is = getAssets().open(fileName);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return json;
        }
    }

