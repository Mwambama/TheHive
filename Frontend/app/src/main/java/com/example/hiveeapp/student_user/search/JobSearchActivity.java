package com.example.hiveeapp.student_user.search;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.example.hiveeapp.student_user.StudentMainActivity;
import com.example.hiveeapp.student_user.chat.ChatListActivity;
import com.example.hiveeapp.student_user.profile.StudentProfileViewActivity;
import com.example.hiveeapp.volley.VolleySingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    private static final String PREFERENCES_NAME = "UserPreferences";
    private static final String STUDENT_ID_KEY = "userId";

    private EditText keywordInput, minSalaryInput, maxSalaryInput;
    private com.google.android.material.textfield.TextInputEditText minJobStartButton, maxJobStartButton;
    private CheckBox isApplicationOpenCheckbox, isQualifiedCheckbox;
    private Button searchButton;
    private AutoCompleteTextView jobTypeDropdown, jobModeDropdown;

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

        setupJobTypeDropdown();
        setupJobModeDropdown();

        // Set search button click listener
        searchButton.setOnClickListener(v -> performSearch());

        setupBottomNavigationView();
    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Log.d(TAG, "Bottom navigation item selected: " + itemId);

            if (itemId == R.id.navigation_profile) {
                navigateToProfile();
                return true;
            } else if (itemId == R.id.navigation_chat) {
                navigateToChat();
                return true;
            } else if (itemId == R.id.navigation_apply) {
                navigateToApplications();
                return true;
            } else if (itemId == R.id.navigation_search) {
                return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.navigation_search); // Set the current page
    }

    private void navigateToProfile() {
        Log.d(TAG, "Navigating to Profile");
        if (studentId == -1) {
            Log.e(TAG, "Invalid Student ID, cannot navigate to profile.");
            Toast.makeText(this, "Invalid Student ID. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(JobSearchActivity.this, StudentProfileViewActivity.class);
        intent.putExtra("USER_ID", studentId);
        startActivity(intent);
        finish();
    }

    private void navigateToChat() {
        Log.d(TAG, "Navigating to Chat");
        Intent intent = new Intent(JobSearchActivity.this, ChatListActivity.class);
        intent.putExtra("USER_ID", studentId);
        startActivity(intent);
        finish();
    }

    private void navigateToApplications() {
        Log.d(TAG, "Navigating to Applications");
        Intent intent = new Intent(JobSearchActivity.this, StudentMainActivity.class); // Assuming this is the applications activity
        startActivity(intent);
        finish();
    }

    private void initializeViews() {
        keywordInput = findViewById(R.id.keywordInput);
        minSalaryInput = findViewById(R.id.minSalaryInput);
        maxSalaryInput = findViewById(R.id.maxSalaryInput);
        minJobStartButton = findViewById(R.id.minJobStartButton);
        maxJobStartButton = findViewById(R.id.maxJobStartButton);
        isApplicationOpenCheckbox = findViewById(R.id.isApplicationOpenCheckbox);
        isQualifiedCheckbox = findViewById(R.id.isQualifiedCheckbox);
        searchButton = findViewById(R.id.searchButton);
        jobTypeDropdown = findViewById(R.id.jobTypeDropdown);
        jobModeDropdown = findViewById(R.id.jobModeDropdown);
    }

    private void setupJobTypeDropdown() {
        String[] jobTypes = {"Full-Time", "Part-Time", "Internship", "Co-op"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, jobTypes);
        jobTypeDropdown.setAdapter(adapter);
    }

    private void setupJobModeDropdown() {
        String[] jobModes = {"Online", "Hybrid", "Presential"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, jobModes);
        jobModeDropdown.setAdapter(adapter);
    }

    private void retrieveStudentId() {
        // Attempt to retrieve from Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USER_ID")) {
            studentId = intent.getIntExtra("USER_ID", -1);
        }

        // If not found in Intent, retrieve from SharedPreferences
        if (studentId == -1 || studentId == 0) { // Handle case where studentId is incorrectly 0
            SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
            studentId = preferences.getInt(STUDENT_ID_KEY, -1);
        }

        // Validate the retrieved studentId
        if (studentId == -1 || studentId == 0) {
            Toast.makeText(this, "Student ID not found. Please log in.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Student ID not found in Intent or SharedPreferences.");
            finish();
        } else {
            Log.d(TAG, "Student ID retrieved: " + studentId);
        }
    }

    private void setupDatePickers() {
        minJobStartButton.setOnClickListener(v -> showDatePicker(date -> {
            minJobStartDate = date;
            minJobStartButton.setText(date);
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

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        List<JobPosting> jobPostings = parseJobPostings(response);
                        if (!jobPostings.isEmpty()) {
                            Intent intent = new Intent(this, JobResultsActivity.class);
                            intent.putExtra("jobPostings", (ArrayList<JobPosting>) jobPostings);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "No jobs found for your search criteria.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Failed to parse job postings.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error fetching job postings.", Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
                String username = preferences.getString("email", "teststudent1@example.com");
                String password = preferences.getString("password", "TestStudent1234@");

                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                String credentials = username + ":" + password;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
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
        for (int i = 0; i < response.length() && i < 10; i++) {
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

    interface OnDateSelectedListener {
        void onDateSelected(String date);
    }
}
