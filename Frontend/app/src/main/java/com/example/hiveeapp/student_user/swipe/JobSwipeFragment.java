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
import java.util.List;
import java.util.Map;

public class JobSwipeFragment extends Fragment {

    public static final String GET_JOB_POSTINGS_URL = "http://coms-3090-063.class.las.iastate.edu:8080/job-posting";
    private static final String TAG = "JobSwipeFragment";
    private static final String PREFERENCES_NAME = "JobSwipePreferences";
    private static final String SWIPE_POSITION_KEY = "SwipePosition";

    private SwipeFlingAdapterView swipeFlingAdapterView;
    private JobSwipeAdapter swipeAdapter;
    private List<JobPosting> jobPostings = new ArrayList<>();
    private int studentId;
    private int savedSwipePosition = 0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            studentId = ((StudentMainActivity) context).getUserId();
        } catch (ClassCastException e) {
            throw new IllegalStateException("Activity must implement StudentMainActivity to provide studentId");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check for recommended jobs passed via arguments
        if (getArguments() != null) {
            List<JobPosting> recommendedJobs = (List<JobPosting>) getArguments().getSerializable("recommendedJobs");
            if (recommendedJobs != null && !recommendedJobs.isEmpty()) {
                Log.d(TAG, "Received recommended jobs: " + recommendedJobs.size());
                prependRecommendedJobs(recommendedJobs);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_swipe, container, false);
        swipeFlingAdapterView = view.findViewById(R.id.swipe_view);

        // Retrieve saved position from SharedPreferences
        savedSwipePosition = getSavedSwipePosition(requireContext());

        // Initialize the adapter
        swipeAdapter = new JobSwipeAdapter(requireContext(), studentId);
        swipeFlingAdapterView.setAdapter(swipeAdapter);

        // Set up fling listener
        setupFlingListener();

        swipeFlingAdapterView.setOnItemClickListener((itemPosition, dataObject) -> {
            if (!isAdded()) return;
            JobPosting job = (JobPosting) dataObject;
            Toast.makeText(requireContext(), "Clicked on: " + job.getTitle(), Toast.LENGTH_SHORT).show();
        });

        // Load default job postings
        loadJobPostings();

        return view;
    }

    private void setupFlingListener() {
        swipeFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                if (!jobPostings.isEmpty()) {
                    Log.d(TAG, "Removing job: " + jobPostings.get(0).getTitle());
                    swipeAdapter.removeJob(0);
                    savedSwipePosition++;
                    saveSwipePosition(requireContext(), savedSwipePosition);
                } else {
                    Log.e(TAG, "No jobs available to remove.");
                }
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Log.d(TAG, "Job dismissed: " + ((JobPosting) dataObject).getTitle());
                Toast.makeText(requireContext(), "Job dismissed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                JobPosting job = (JobPosting) dataObject;
                Log.d(TAG, "Job applied: " + job.getTitle());
                swipeAdapter.applyForJob(job.getJobPostingId());
                Toast.makeText(requireContext(), "Applied for: " + job.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                Log.d(TAG, "Adapter about to empty. Reloading jobs.");
                loadJobPostings();
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                Log.d(TAG, "Scroll progress: " + scrollProgressPercent);
            }
        });
    }

    private void loadJobPostings() {
        if (!isAdded()) {
            Log.e(TAG, "Fragment not added. Skipping loadJobPostings.");
            return;
        }

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, GET_JOB_POSTINGS_URL, null,
                response -> {
                    if (!isAdded()) return;
                    jobPostings.clear();
                    jobPostings.addAll(parseJobPostings(response));
                    if (jobPostings.isEmpty()) {
                        Toast.makeText(requireContext(), "No jobs available.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "Jobs loaded: " + jobPostings.size());
                    }
                    if (swipeAdapter != null) {
                        swipeAdapter.setJobPostings(jobPostings);
                        swipeAdapter.notifyDataSetChanged();
                    }
                    restoreSwipePosition();
                },
                error -> {
                    if (!isAdded()) return;
                    Log.e(TAG, "Failed to load job postings", error);
                    Toast.makeText(requireContext(), "Failed to load job postings", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createAuthorizationHeaders();
            }
        };

        VolleySingleton.getInstance(requireContext().getApplicationContext()).addToRequestQueue(request);
    }

    public void prependRecommendedJobs(List<JobPosting> recommendedJobs) {
        if (!isAdded() || recommendedJobs == null || recommendedJobs.isEmpty()) {
            Log.e(TAG, "No jobs to prepend or fragment not added.");
            return;
        }

        Log.d(TAG, "Prepending jobs: " + recommendedJobs.size());
        // Limit to 10 recommended jobs
        List<JobPosting> topRecommendedJobs = recommendedJobs.size() > 10
                ? recommendedJobs.subList(0, 10)
                : recommendedJobs;

        // Prepend recommended jobs
        jobPostings.addAll(0, topRecommendedJobs);

        if (swipeAdapter != null) {
            swipeAdapter.setJobPostings(jobPostings);
            swipeAdapter.notifyDataSetChanged(); // Notify adapter to refresh the UI
        }
    }

    private List<JobPosting> parseJobPostings(JSONArray response) {
        List<JobPosting> parsedJobs = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject obj = response.getJSONObject(i);
                parsedJobs.add(new JobPosting(
                        obj.getInt("jobPostingId"),
                        obj.getString("title"),
                        obj.getString("description"),
                        obj.optString("summary", ""),
                        obj.optDouble("salary", 0.0),
                        obj.optString("jobType", "N/A"),
                        obj.optDouble("minimumGpa", 0.0),
                        obj.optString("jobStart", ""),
                        obj.optString("applicationStart", ""),
                        obj.optString("applicationEnd", ""),
                        obj.optInt("employerId", -1),
                        obj.optString("companyName", "Unknown Company")
                ));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing job postings", e);
        }
        return parsedJobs;
    }

    private void restoreSwipePosition() {
        if (savedSwipePosition > 0 && savedSwipePosition < jobPostings.size()) {
            Log.d(TAG, "Restoring swipe position to: " + savedSwipePosition);
            for (int i = 0; i < savedSwipePosition; i++) {
                swipeAdapter.removeJob(0);
            }
        } else {
            Log.d(TAG, "No saved position to restore or out of bounds.");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveSwipePosition(requireContext(), savedSwipePosition);
    }

    private void saveSwipePosition(Context context, int position) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        preferences.edit().putInt(SWIPE_POSITION_KEY, position).apply();
    }

    private int getSavedSwipePosition(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(SWIPE_POSITION_KEY, 0);
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
