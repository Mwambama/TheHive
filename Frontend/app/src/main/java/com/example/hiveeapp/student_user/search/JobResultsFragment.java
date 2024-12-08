package com.example.hiveeapp.student_user.search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.swipe.JobPosting;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobResultsFragment extends Fragment implements JobListAdapter.OnJobInteractionListener {

    private static final String TAG = "JobResultsFragment";
    private static final String APPLY_URL = "http://coms-3090-063.class.las.iastate.edu:8080/applications/apply";
    private static final String PREFERENCES_NAME = "JobSwipePreferences";
    private static final String STUDENT_ID_KEY = "studentId";

    private RecyclerView jobResultsRecyclerView;
    private JobListAdapter jobListAdapter;
    private SharedViewModel sharedViewModel;
    private List<JobPosting> jobPostings;
    private int studentId = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_job_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve student ID from Intent or SharedPreferences
        retrieveStudentId();

        if (studentId == -1) {
            Toast.makeText(getContext(), "Student ID not found. Please log in.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Student ID not found. Cannot proceed.");
            return;
        }

        jobResultsRecyclerView = view.findViewById(R.id.job_results_recycler_view);
        jobListAdapter = new JobListAdapter(this);
        jobResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        jobResultsRecyclerView.setAdapter(jobListAdapter);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        jobPostings = sharedViewModel.getJobPostings();
        if (jobPostings != null && !jobPostings.isEmpty()) {
            jobListAdapter.setJobPostings(jobPostings);
        }
    }

    private void retrieveStudentId() {
        // Retrieve from SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        studentId = preferences.getInt(STUDENT_ID_KEY, -1);

        if (studentId != -1) {
            Log.d(TAG, "Student ID retrieved from SharedPreferences: " + studentId);
        } else {
            Log.e(TAG, "Student ID not found in SharedPreferences.");
        }
    }

    @Override
    public void onJobClick(JobPosting jobPosting) {
        // Handle job click
        Toast.makeText(getContext(), "Clicked on: " + jobPosting.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onJobApply(JobPosting jobPosting, int position) {
        // Handle job application logic
        applyForJob(jobPosting, position);
    }

    private void applyForJob(JobPosting jobPosting, int position) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("studentId", studentId);
            requestBody.put("jobPostingId", jobPosting.getJobPostingId());
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON request body", e);
            Toast.makeText(getContext(), "Failed to create application request.", Toast.LENGTH_SHORT).show();
            return;
        }

        String applyUrl = APPLY_URL;

        StringRequest request = new StringRequest(
                Request.Method.POST,
                applyUrl,
                response -> {
                    // Handle successful application response
                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();

                    // Remove job from the list
                    jobListAdapter.removeJob(position);
                },
                error -> {
                    // Handle errors
                    String errorMessage = "Failed to apply for job.";
                    if (error.networkResponse != null) {
                        if (error.networkResponse.statusCode == 500) {
                            errorMessage = "Server error: Unable to process the application.";
                        } else if (error.networkResponse.statusCode == 401) {
                            errorMessage = "Unauthorized access. Please log in.";
                        }
                    }
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error applying for job", error);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.putAll(createAuthorizationHeaders());
                return headers;
            }

            @Override
            public byte[] getBody() {
                return requestBody.toString().getBytes();
            }
        };

        VolleySingleton.getInstance(requireContext()).addToRequestQueue(request);
    }


    private Map<String, String> createAuthorizationHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        String username = "teststudent1@example.com";
        String password = "TestStudent1234@";

        String credentials = username + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        return headers;
    }
}
