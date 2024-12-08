package com.example.hiveeapp.student_user.search;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.search.JobPosting;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobSearchActivity extends AppCompatActivity {

    private static final String TAG = "JobSearchActivity";
    private static final String SEARCH_URL = "http://coms-3090-063.class.las.iastate.edu:8080/job-posting/search";
    private static final String PREFERENCES_NAME = "JobSwipePreferences";
    private static final String STUDENT_ID_KEY = "studentId";

    private EditText keywordInput, minSalaryInput, maxSalaryInput;
    private com.google.android.material.textfield.TextInputEditText minJobStartButton, maxJobStartButton;
    private CheckBox isApplicationOpenCheckbox, isQualifiedCheckbox;
    private Button searchButton;

    private int studentId;
    private String minJobStartDate = "";
    private String maxJobStartDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_job_search);

        // Initialize views
        initializeViews();

        // Retrieve Student ID
        retrieveStudentId();

        // Set date pickers for job start date fields
        setupDatePickers();

        // Set search button click listener
        searchButton.setOnClickListener(v -> performSearch());
    }

    private void initializeViews() {
        keywordInput = findViewById(R.id.keywordInput);
        minSalaryInput = findViewById(R.id.minSalaryInput);
        maxSalaryInput = findViewById(R.id.maxSalaryInput);
        minJobStartButton = findViewById(R.id.minJobStartButton); // Corrected to TextInputEditText
        maxJobStartButton = findViewById(R.id.maxJobStartButton); // Corrected to TextInputEditText
        isApplicationOpenCheckbox = findViewById(R.id.isApplicationOpenCheckbox);
        isQualifiedCheckbox = findViewById(R.id.isQualifiedCheckbox);
        searchButton = findViewById(R.id.searchButton);
    }

    private void retrieveStudentId() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        studentId = preferences.getInt(STUDENT_ID_KEY, -1);

        if (studentId == -1) {
            Toast.makeText(this, "Student ID not found. Please log in.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Student ID not found in SharedPreferences.");
            finish(); // Exit if no studentId
        } else {
            Log.d(TAG, "Student ID retrieved: " + studentId);
        }
    }

    private void setupDatePickers() {
        minJobStartButton.setOnClickListener(v -> showDatePicker(date -> {
            minJobStartDate = date;
            minJobStartButton.setText(date); // Display the selected date in the TextInputEditText
        }));

        maxJobStartButton.setOnClickListener(v -> showDatePicker(date -> {
            maxJobStartDate = date;
            maxJobStartButton.setText(date);
        }));
    }

    private void showDatePicker(OnDateSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    // Format date to yyyy-MM-dd
                    String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    listener.onDateSelected(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void performSearch() {
        Map<String, String> filters = new HashMap<>();
        if (!keywordInput.getText().toString().isEmpty()) {
            filters.put("q", keywordInput.getText().toString());
        }
        if (!minSalaryInput.getText().toString().isEmpty()) {
            filters.put("minSalary", minSalaryInput.getText().toString());
        }
        if (!maxSalaryInput.getText().toString().isEmpty()) {
            filters.put("maxSalary", maxSalaryInput.getText().toString());
        }
        if (!minJobStartDate.isEmpty()) {
            filters.put("minJobStart", minJobStartDate);
        }
        if (!maxJobStartDate.isEmpty()) {
            filters.put("maxJobStart", maxJobStartDate);
        }
        filters.put("isApplicationOpen", String.valueOf(isApplicationOpenCheckbox.isChecked()));
        filters.put("isQualified", String.valueOf(isQualifiedCheckbox.isChecked()));

        Log.d(TAG, "Filters: " + filters);

        fetchJobPostings(filters);
    }

    private void fetchJobPostings(Map<String, String> filters) {
        StringBuilder urlBuilder = new StringBuilder(SEARCH_URL);
        if (!filters.isEmpty()) {
            urlBuilder.append("?");
            for (Map.Entry<String, String> entry : filters.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        String url = urlBuilder.toString();
        Log.d(TAG, "Request URL: " + url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        List<JobPosting> jobPostings = parseJobPostings(response);

                        Log.d(TAG, "Parsed job postings count: " + jobPostings.size());

                        if (!jobPostings.isEmpty()) {
                            Intent intent = new Intent(this, JobResultsActivity.class);
                            intent.putExtra("jobPostings", (ArrayList<JobPosting>) jobPostings);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "No jobs found for your search criteria.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing job postings", e);
                        Toast.makeText(this, "Failed to parse job postings", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error fetching job postings", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Volley Request Error: ", error);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", createAuthorizationHeader());
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private List<JobPosting> parseJobPostings(JSONArray response) throws JSONException {
        List<JobPosting> jobPostings = new ArrayList<>();
        int limit = Math.min(response.length(), 10);

        for (int i = 0; i < limit; i++) {
            JSONObject jsonObject = response.getJSONObject(i);
            JobPosting jobPosting = new JobPosting(
                    jsonObject.optInt("jobPostingId", -1),
                    jsonObject.getString("title"),
                    jsonObject.getString("description"),
                    jsonObject.optString("summary", ""),
                    jsonObject.optDouble("salary", 0.0),
                    jsonObject.optString("jobType", "N/A"),
                    jsonObject.optDouble("minimumGpa", 0.0),
                    jsonObject.getString("jobStart"),
                    jsonObject.getString("applicationStart"),
                    jsonObject.getString("applicationEnd"),
                    jsonObject.optInt("employerId", -1),
                    jsonObject.optString("companyName", "Unknown Company")
            );
            jobPostings.add(jobPosting);
        }

        return jobPostings;
    }

    private String createAuthorizationHeader() {
        String username = "teststudent1@example.com";
        String password = "TestStudent1234@";
        String credentials = username + ":" + password;
        return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }

    interface OnDateSelectedListener {
        void onDateSelected(String date);
    }
}
