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
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.example.hiveeapp.volley.VolleySingleton;

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
    public static final String GET_JOB_POSTINGS_URL = "http://coms-3090-063.class.las.iastate.edu:8080/job-posting";

    private SwipeFlingAdapterView swipeFlingAdapterView;
    private JobSwipeAdapter swipeAdapter;
    private List<JobPosting> jobPostings = new ArrayList<>();
    private int studentId;

    private Set<String> appliedJobs;
    private Set<String> dismissedJobs;

    private JobSwipeViewModel viewModel;

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
        swipeAdapter = new JobSwipeAdapter(requireContext(), studentId);
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
                swipeAdapter.notifyDataSetChanged();
            }
        });

        if (viewModel.getJobPostings().getValue() == null) {
            loadJobPostings();
        } else {
            jobPostings = viewModel.getJobPostings().getValue();
            swipeAdapter.setJobPostings(jobPostings);
        }

        return view;
    }

    private void setupFlingListener() {
        swipeFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                if (!jobPostings.isEmpty()) {
                    Log.d(TAG, "Removing job: " + jobPostings.get(0).getTitle());
                    swipeAdapter.removeJob(0);
                    jobPostings.remove(0);
                    viewModel.setJobPostings(jobPostings);
                }
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                JobPosting job = (JobPosting) dataObject;
                Toast.makeText(requireContext(), "Dismissed: " + job.getTitle(), Toast.LENGTH_SHORT).show();
                saveDismissedJob(job.getJobPostingId()); // Save dismissed job
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                JobPosting job = (JobPosting) dataObject;
                Toast.makeText(requireContext(), "Applied to: " + job.getTitle(), Toast.LENGTH_SHORT).show();
                saveAppliedJob(job.getJobPostingId());
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                Log.d(TAG, "Adapter about to empty. No further action needed.");
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                // Optional scroll feedback
            }
        });
    }

    private void loadJobPostings() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, GET_JOB_POSTINGS_URL, null,
                response -> {
                    List<JobPosting> allJobs = parseJobPostings(response);

                    List<JobPosting> filteredJobs = new ArrayList<>();
                    for (JobPosting job : allJobs) {
                        String jobIdStr = String.valueOf(job.getJobPostingId());
                        // Exclude both applied and dismissed jobs
                        if (!appliedJobs.contains(jobIdStr) && !dismissedJobs.contains(jobIdStr)) {
                            filteredJobs.add(job);
                        }
                    }

                    jobPostings = filteredJobs;
                    viewModel.setJobPostings(jobPostings);
                },
                error -> {
                    Log.e(TAG, "Error loading job postings", error);
                    Toast.makeText(requireContext(), "Failed to load job postings", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");

                String username = "teststudent1@example.com";
                String password = "TestStudent1234@";
                String credentials = username + ":" + password;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);

                return headers;
            }
        };

        VolleySingleton.getInstance(requireContext()).addToRequestQueue(request);
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

    private void saveAppliedJob(int jobPostingId) {
        appliedJobs.add(String.valueOf(jobPostingId));
        saveAppliedJobs(appliedJobs);
    }

    private void saveDismissedJob(int jobPostingId) {
        dismissedJobs.add(String.valueOf(jobPostingId));
        saveDismissedJobs(dismissedJobs);
    }

    private Set<String> getAppliedJobs() {
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return new HashSet<>(preferences.getStringSet(APPLIED_JOBS_KEY, new HashSet<>()));
    }

    private void saveAppliedJobs(Set<String> appliedJobs) {
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(APPLIED_JOBS_KEY, appliedJobs);
        editor.apply();
    }

    private Set<String> getDismissedJobs() {
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return new HashSet<>(preferences.getStringSet(DISMISSED_JOBS_KEY, new HashSet<>()));
    }

    private void saveDismissedJobs(Set<String> dismissedJobs) {
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(DISMISSED_JOBS_KEY, dismissedJobs);
        editor.apply();
    }

    public void prependRecommendedJobs(List<JobPosting> recommendedJobs) {
        if (recommendedJobs == null || recommendedJobs.isEmpty()) {
            Log.e(TAG, "No recommended jobs to prepend.");
            return;
        }

        // Add recommended jobs to the beginning of the list
        jobPostings.addAll(0, recommendedJobs);
        viewModel.setJobPostings(jobPostings);

        // Notify the adapter of changes
        if (swipeAdapter != null) {
            swipeAdapter.setJobPostings(jobPostings);
            swipeAdapter.notifyDataSetChanged();
        }

        Log.d(TAG, "Prepended recommended jobs: " + recommendedJobs.size());
    }
}
