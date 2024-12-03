package com.example.hiveeapp.student_user.search;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.swipe.JobPosting; // Use the swipe.JobPosting class
import com.example.hiveeapp.student_user.swipe.JobSwipeFragment;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobSearchFragment extends Fragment {

    private static final String TAG = "JobSearchFragment";
    private static final String SEARCH_URL = "http://coms-3090-063.class.las.iastate.edu:8080/job-posting/search";

    private EditText keywordInput, minSalaryInput, maxSalaryInput, minJobStartInput, maxJobStartInput;
    private CheckBox isApplicationOpenCheckbox, isQualifiedCheckbox;
    private Button searchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_search, container, false);

        // Initialize views
        keywordInput = view.findViewById(R.id.keywordInput);
        minSalaryInput = view.findViewById(R.id.minSalaryInput);
        maxSalaryInput = view.findViewById(R.id.maxSalaryInput);
        minJobStartInput = view.findViewById(R.id.minJobStartInput);
        maxJobStartInput = view.findViewById(R.id.maxJobStartInput);
        isApplicationOpenCheckbox = view.findViewById(R.id.isApplicationOpenCheckbox);
        isQualifiedCheckbox = view.findViewById(R.id.isQualifiedCheckbox);
        searchButton = view.findViewById(R.id.searchButton);

        // Set search button click listener
        searchButton.setOnClickListener(v -> performSearch());

        return view;
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
        if (!minJobStartInput.getText().toString().isEmpty()) {
            filters.put("minJobStart", minJobStartInput.getText().toString());
        }
        if (!maxJobStartInput.getText().toString().isEmpty()) {
            filters.put("maxJobStart", maxJobStartInput.getText().toString());
        }
        filters.put("isApplicationOpen", String.valueOf(isApplicationOpenCheckbox.isChecked()));
        filters.put("isQualified", String.valueOf(isQualifiedCheckbox.isChecked()));

        Log.d(TAG, "Filters: " + filters);

        fetchJobPostings(filters, jobPostings -> {
            Log.d(TAG, "Job postings fetched successfully: " + jobPostings.size());
            if (jobPostings.isEmpty()) {
                Toast.makeText(getContext(), "No jobs found.", Toast.LENGTH_SHORT).show();
            } else {
                sendRecommendedJobsToMainPage(jobPostings);
            }
        });
    }

    private void fetchJobPostings(Map<String, String> filters, JobPostingsCallback callback) {
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
                        callback.onJobPostingsFetched(jobPostings);
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing job postings", e);
                        Toast.makeText(getContext(), "Failed to parse job postings", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Volley Request Error: " + error.getMessage(), error); // Log error details
                    handleError(error);
                    Toast.makeText(getContext(), "Error fetching job postings", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createAuthorizationHeaders(getContext());
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    private void sendRecommendedJobsToMainPage(List<JobPosting> recommendedJobs) {
        Log.d(TAG, "Sending recommended jobs: " + recommendedJobs.size());

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        Fragment jobSwipeFragment = fragmentManager.findFragmentByTag("JobSwipeFragment");

        if (jobSwipeFragment instanceof JobSwipeFragment) {
            ((JobSwipeFragment) jobSwipeFragment).prependRecommendedJobs(recommendedJobs);
            Log.d(TAG, "Recommended jobs passed to JobSwipeFragment");
        } else {
            Log.e(TAG, "JobSwipeFragment not found. Creating a new instance.");

            JobSwipeFragment newJobSwipeFragment = new JobSwipeFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("recommendedJobs", new ArrayList<>(recommendedJobs));
            newJobSwipeFragment.setArguments(bundle);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frameLayout, new JobSwipeFragment(), "JobSwipeFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        }

        // Navigate back if the fragment is already found
        fragmentManager.popBackStack();
    }

    private Map<String, String> createAuthorizationHeaders(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        String username = "teststudent1@example.com";
        String password = "TestStudent1234@";

        String credentials = username + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        return headers;
    }

    private List<JobPosting> parseJobPostings(JSONArray response) throws JSONException {
        List<JobPosting> jobPostings = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            JSONObject jsonObject = response.getJSONObject(i);
            JobPosting jobPosting = new JobPosting(
                    jsonObject.optInt("jobPostingId", -1),
                    jsonObject.getString("title"),
                    jsonObject.getString("description"),
                    jsonObject.optString("summary", ""),
                    jsonObject.getDouble("salary"),
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

        Log.d(TAG, "Parsed job postings: " + jobPostings); // Log the parsed data
        return jobPostings;
    }


    private void handleError(Throwable error) {
        String errorMessage = "Error fetching job postings.";
        if (error != null && error.getMessage() != null) {
            errorMessage += " Details: " + error.getMessage();
        }
        Log.e(TAG, "Volley Error: ", error);
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    interface JobPostingsCallback {
        void onJobPostingsFetched(List<JobPosting> jobPostings);
    }
}
