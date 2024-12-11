package com.example.hiveeapp.student_user.swipe;

import static android.content.Context.MODE_PRIVATE;
import static com.example.hiveeapp.student_user.StudentMainActivity.DAILY_APPLIED_COUNT_KEY;
import static com.example.hiveeapp.student_user.swipe.JobApplicationUtils.applyForJob;

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
import com.android.volley.VolleyError;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_swipe, container, false);

        if (getContext() == null) {
            Log.e(TAG, "Fragment not attached to context yet.");
            return view;
        }

        // Retrieve studentId from SharedPreferences
        SharedPreferences preferences = getContext().getSharedPreferences("UserPreferences", MODE_PRIVATE);
        studentId = preferences.getInt("userId", -1);
        if (studentId == -1) {
            Toast.makeText(getContext(), "User ID not found. Cannot load jobs.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "User ID not found in SharedPreferences.");
            return view;
        }

        swipeFlingAdapterView = view.findViewById(R.id.swipe_view);
        swipeAdapter = new JobSwipeAdapter(getContext(), jobPostings);
        swipeFlingAdapterView.setAdapter(swipeAdapter);

        setupFlingListener();

        swipeFlingAdapterView.setOnItemClickListener((itemPosition, dataObject) -> {
            JobPosting job = (JobPosting) dataObject;
            Toast.makeText(getContext(), "Clicked on: " + job.getTitle(), Toast.LENGTH_SHORT).show();
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
            fetchJobPostings();
        }

        return view;
    }

    private void setupFlingListener() {
        swipeFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                if (!jobPostings.isEmpty()) {
                    jobPostings.remove(0);
                    swipeAdapter.setJobPostings(new ArrayList<>(jobPostings));
                } else {
                    Log.d(TAG, "No more jobs to swipe.");
                }
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                JobPosting job = (JobPosting) dataObject;
                saveDismissedJob(job.getJobPostingId());
                Toast.makeText(getContext(), "Job dismissed: " + job.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                JobPosting job = (JobPosting) dataObject;
                Context context = requireContext();

                if (appliedJobs.contains(String.valueOf(job.getJobPostingId()))) {
                    Toast.makeText(context, "You already applied to this job!", Toast.LENGTH_SHORT).show();
                } else {
                    saveAppliedJob(job.getJobPostingId());

                    applyForJob(context, studentId, job.getJobPostingId(),
                            () -> {
                                // Success callback
                                Toast.makeText(context, "Application for " + job.getTitle() + " was successful!", Toast.LENGTH_SHORT).show();

                                // Call the method from the activity to increment and refresh the count
                                if (getActivity() instanceof StudentMainActivity) {
                                    ((StudentMainActivity) getActivity()).incrementAndDisplayDailyAppliedCount();
                                }
                            },
                            () -> {
                                // Error callback
                                Toast.makeText(context, "Failed to apply for " + job.getTitle(), Toast.LENGTH_SHORT).show();
                            }
                    );
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                if (itemsInAdapter == 0) {
                    fetchJobPostings();
                }
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                // Optional visual feedback
            }
        });
    }

    private void fetchJobPostings() {
        if (isFetchingJobs) return; // Prevent duplicate calls
        isFetchingJobs = true;

        String url = GET_RECOMMENDED_JOB_POSTINGS_URL + studentId;
        Log.d(TAG, "Fetching job postings from URL: " + url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    isFetchingJobs = false;
                    List<JobPosting> newJobs = parseJobPostings(response);
                    if (newJobs.isEmpty()) {
                        Log.d(TAG, "No jobs returned from the API.");
                        Toast.makeText(getContext(), "No jobs available to display.", Toast.LENGTH_SHORT).show();
                    } else {
                        List<JobPosting> filteredJobs = filterJobPostings(newJobs);
                        jobPostings.addAll(filteredJobs);
                        swipeAdapter.setJobPostings(new ArrayList<>(jobPostings));
                    }
                },
                error -> {
                    isFetchingJobs = false;
                    String errorMsg = "Error fetching job postings";
                    if (error instanceof VolleyError && error.networkResponse != null) {
                        errorMsg += " (Code: " + error.networkResponse.statusCode + ")";
                    }
                    Log.e(TAG, errorMsg);
                    Toast.makeText(getContext(), "Failed to fetch jobs. Try again later.", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createAuthorizationHeaders();
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    private Map<String, String> createAuthorizationHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        SharedPreferences preferences = getContext().getSharedPreferences(AUTH_PREFERENCES, MODE_PRIVATE);
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
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        return new HashSet<>(preferences.getStringSet(APPLIED_JOBS_KEY, new HashSet<>()));
    }

    private Set<String> getDismissedJobs() {
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
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
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(key, jobs);
        editor.apply();
    }
}
