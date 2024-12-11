package com.example.hiveeapp.employer_user.applications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.EmployerMainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * ApplicationListActivity handles the display and management of job postings for an employer.
 * It allows users to view the jobs they've posted and includes navigation for other employer activities.
 */
public class ApplicationListActivity extends AppCompatActivity {

    private RecyclerView applicationRecyclerView; // RecyclerView for displaying job postings
    private applicationAdapter applicAdapter;    // Adapter for managing job postings
    private MaterialButton addEmployerButton;    // Button for adding a new employer

    public static final String USER_PREFS = "UserPreferences"; // SharedPreferences name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_application_list);

        // Initialize views
        initViews();

        // Set up the RecyclerView with a linear layout and the job postings adapter
        applicationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        applicAdapter = new applicationAdapter(this, true); // true indicates editable mode
        applicationRecyclerView.setAdapter(applicAdapter);

        // Load the job postings from the server
        loadApplications();

        // Set up Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_invitations) {
                startActivity(new Intent(this, ApplicationListActivity.class));
                return true;
            } else if (itemId == R.id.navigation_employers) {
                return true;
            } else if (itemId == R.id.navigation_main_user_page) {
                startActivity(new Intent(this, EmployerMainActivity.class));
                return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_employers);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the job postings list when the activity is resumed
        loadApplications();
    }

    /**
     * Initialize views in the activity.
     */
    private void initViews() {
        applicationRecyclerView = findViewById(R.id.applicationRecyclerView);
        addEmployerButton = findViewById(R.id.addEmployerButton);
    }

    /**
     * Load the list of job postings from the server using the jobPostingId.
     */
    private void loadApplications() {
        // Retrieve jobPostingId from SharedPreferences
        SharedPreferences preferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        int jobPostingId = preferences.getInt("jobPostingId", -1); // Ensure key matches your SharedPreferences setup

        Log.d("LoadApplications", "Retrieved jobPostingId: " + jobPostingId);

        // Check if the jobPostingId is valid
        if (jobPostingId == -1) {
            Toast.makeText(this, "Error: Job Posting ID not found. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Construct the API URL for fetching applications by jobPostingId
        String apiUrl = "http://coms-3090-063.class.las.iastate.edu:8080/applications?jobPostingId=" + jobPostingId + "&status=PENDING";
        Log.d("applicationsApi", "GET Applications Request URL: " + apiUrl);

        // Call the API
        applicationsApi.getApplications(
                this,
                apiUrl,
                response -> {
                    JSONArray reversedApplications = reverseJSONArray(response);
                    applicAdapter.setApplications(reversedApplications);
                },
                error -> {
                    String errorMessage = "An error occurred while fetching applications.";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            String errorData = new String(error.networkResponse.data, "UTF-8");
                            JSONObject jsonError = new JSONObject(errorData);
                            errorMessage = jsonError.optString("message", errorMessage);
                        } catch (Exception e) {
                            Log.e("LoadApplications", "Error parsing error response", e);
                        }
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                }
        );
    }

    /**
     * Reverses the order of the given JSONArray.
     *
     * @param array The JSONArray to be reversed.
     * @return A new JSONArray in reversed order.
     */
    private JSONArray reverseJSONArray(JSONArray array) {
        JSONArray reversedArray = new JSONArray();
        for (int i = array.length() - 1; i >= 0; i--) {
            try {
                JSONObject jsonObject = array.getJSONObject(i);
                reversedArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return reversedArray;
    }
}
