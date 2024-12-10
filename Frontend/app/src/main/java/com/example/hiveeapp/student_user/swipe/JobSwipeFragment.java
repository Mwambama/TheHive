package com.example.hiveeapp.student_user.swipe;

import android.content.Context;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.StudentMainActivity;
import com.example.hiveeapp.volley.VolleySingleton;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JobSwipeFragment extends Fragment {

    private static final String TAG = "JobSwipeFragment";
    private static final String PREFERENCES_NAME = "JobSwipePreferences";
    private static final String APPLIED_JOBS_KEY = "AppliedJobs";
    private static final String DISMISSED_JOBS_KEY = "DismissedJobs";
    public static final String AUTH_PREFERENCES = "UserPreferences";
    public static final String GET_RECOMMENDED_JOB_POSTINGS_URL = "http://coms-3090-063.class.las.iastate.edu:8080/job-posting/suggestions/";

    private SwipeFlingAdapterView swipeFlingAdapterView;
    private JobSwipeAdapter swipeAdapter;
    private List<JobPosting> jobPostings = new ArrayList<>();
    private int studentId;

    private Set<String> appliedJobs;
    private Set<String> dismissedJobs;

    private JobSwipeViewModel viewModel;
    private boolean isFetchingJobs = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            studentId = ((StudentMainActivity) context).getUserId();
        } catch (ClassCastException e) {
            throw new IllegalStateException("Activity must implement StudentMainActivity to provide studentId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_swipe, container, false);

        swipeFlingAdapterView = view.findViewById(R.id.swipe_view);
        swipeAdapter = new JobSwipeAdapter(requireContext(), this);
        swipeFlingAdapterView.setAdapter(swipeAdapter);

        setupFlingListener();

        swipeFlingAdapterView.setOnItemClickListener((itemPosition, dataObject) -> {
            JobPosting job = (JobPosting) dataObject;
            Toast.makeText(requireContext(), "Clicked on: " + job.getTitle(), Toast.LENGTH_SHORT).show();
        });

        appliedJobs = getAppliedJobs();
        dismissedJobs = getDismissedJobs();

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(JobSwipeViewModel.class);

        // Observe the jobPostings LiveData
        viewModel.getJobPostings().observe(getViewLifecycleOwner(), postings -> {
            if (postings != null) {
                jobPostings = postings;
                swipeAdapter.setJobPostings(jobPostings);
            }
        });

        // Load data if not already loaded
        if (viewModel.getJobPostings().getValue() == null) {
            loadJobPostingsFromAdapter(new JobPostingsCallback() {
                @Override
                public void onJobPostingsLoaded(List<JobPosting> postings) {
                    List<JobPosting> filteredJobs = filterJobPostings(postings);
                    jobPostings = filteredJobs;
                    viewModel.setJobPostings(jobPostings);
                    swipeAdapter.setJobPostings(jobPostings);
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, "Error loading job postings: " + error);
                    Toast.makeText(requireContext(), "Failed to load job postings: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }

    private void setupFlingListener() {
        swipeFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                if (!jobPostings.isEmpty()) {
                    swipeAdapter.removeJob(0);
                    jobPostings.remove(0);
                    viewModel.setJobPostings(jobPostings);
                }
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                JobPosting job = (JobPosting) dataObject;
                saveDismissedJob(job.getJobPostingId());
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                JobPosting job = (JobPosting) dataObject;
                if (appliedJobs.contains(String.valueOf(job.getJobPostingId()))) {
                    Toast.makeText(requireContext(), "You already applied to this job!", Toast.LENGTH_SHORT).show();
                } else {
                    saveAppliedJob(job.getJobPostingId());
                    Toast.makeText(requireContext(), "Successfully applied to: " + job.getTitle(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                Log.d(TAG, "Adapter about to empty.");
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                // Optional feedback
            }
        });
    }

    public void loadJobPostingsFromAdapter(JobPostingsCallback callback) {
        if (isFetchingJobs) return; // Prevent duplicate calls
        isFetchingJobs = true;

        String url = GET_RECOMMENDED_JOB_POSTINGS_URL + studentId;
        Log.d(TAG, "Fetching job postings from URL: " + url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    isFetchingJobs = false;
                    List<JobPosting> recommendedJobs = parseJobPostings(response);
                    callback.onJobPostingsLoaded(recommendedJobs);
                },
                error -> {
                    isFetchingJobs = false;
                    String errorMsg = "Error loading job postings";
                    if (error.networkResponse != null) {
                        errorMsg += " (Code: " + error.networkResponse.statusCode + ")";
                    }
                    callback.onError(errorMsg);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createAuthorizationHeaders();
            }
        };

        VolleySingleton.getInstance(requireContext()).addToRequestQueue(request);
    }

    private Map<String, String> createAuthorizationHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        SharedPreferences preferences = requireContext().getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        String username = preferences.getString("email", "");
        String password = preferences.getString("password", "");

        if (!username.isEmpty() && !password.isEmpty()) {
            String credentials = username + ":" + password;
            String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            headers.put("Authorization", auth);
        }

        return headers;
    }

    private List<JobPosting> parseJobPostings(JSONArray response) {
        List<JobPosting> jobs = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject obj = response.getJSONObject(i);
                jobs.add(new JobPosting(
                        obj.getInt("jobPostingId"),
                        obj.getString("title"),
                        obj.getString("description"),
                        obj.optString("summary", ""),
                        obj.optDouble("salary", 0.0),
                        obj.optString("jobType", ""),
                        obj.optDouble("minimumGpa", 0.0),
                        obj.getString("jobStart"),
                        obj.getString("applicationStart"),
                        obj.getString("applicationEnd"),
                        obj.optInt("employerId", -1),
                        obj.optString("companyName", "Unknown Company")
                ));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing job postings", e);
        }
        return jobs;
    }

    private List<JobPosting> filterJobPostings(List<JobPosting> postings) {
        List<JobPosting> filteredJobs = new ArrayList<>();
        for (JobPosting job : postings) {
            String jobIdStr = String.valueOf(job.getJobPostingId());
            if (!appliedJobs.contains(jobIdStr) && !dismissedJobs.contains(jobIdStr)) {
                filteredJobs.add(job);
            }
        }
        return filteredJobs;
    }

    private Set<String> getAppliedJobs() {
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return new HashSet<>(preferences.getStringSet(APPLIED_JOBS_KEY, new HashSet<>()));
    }

    private Set<String> getDismissedJobs() {
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return new HashSet<>(preferences.getStringSet(DISMISSED_JOBS_KEY, new HashSet<>()));
    }

    private void saveAppliedJob(int jobPostingId) {
        appliedJobs.add(String.valueOf(jobPostingId));
        saveJobs(appliedJobs, APPLIED_JOBS_KEY);
    }

    private void saveDismissedJob(int jobPostingId) {
        dismissedJobs.add(String.valueOf(jobPostingId));
        saveJobs(dismissedJobs, DISMISSED_JOBS_KEY);
    }

    private void saveJobs(Set<String> jobs, String key) {
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(key, jobs);
        editor.apply();
    }

    public interface JobPostingsCallback {
        void onJobPostingsLoaded(List<JobPosting> postings);

        void onError(String error);
    }
}
