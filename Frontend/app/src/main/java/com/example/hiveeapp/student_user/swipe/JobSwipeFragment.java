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
import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.StudentMainActivity;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
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
        swipeFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                swipeAdapter.removeJob(0);
                savedSwipePosition++;
                saveSwipePosition(requireContext(), savedSwipePosition);
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Toast.makeText(requireContext(), "Job dismissed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                JobPosting job = (JobPosting) dataObject;
                swipeAdapter.applyForJob(job.getJobPostingId());
                Toast.makeText(requireContext(), "Applied for: " + job.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                loadJobPostings();
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                // Handle visual feedback during scrolling
            }
        });

        swipeFlingAdapterView.setOnItemClickListener((itemPosition, dataObject) -> {
            JobPosting job = (JobPosting) dataObject;
            Toast.makeText(requireContext(), "Clicked on: " + job.getTitle(), Toast.LENGTH_SHORT).show();
        });

        loadJobPostings();

        return view;
    }

    private void loadJobPostings() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, GET_JOB_POSTINGS_URL, null,
                response -> {
                    jobPostings.clear();
                    parseJobPostings(response);

                    if (swipeAdapter != null) {
                        swipeAdapter.setJobPostings(jobPostings);
                    }

                    // Restore swipe position
                    restoreSwipePosition();
                },
                error -> {
                    Log.e(TAG, "Failed to load job postings", error);
                    Toast.makeText(requireContext(), "Failed to load job postings", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createAuthorizationHeaders(requireContext());
            }
        };

        VolleySingleton.getInstance(requireContext()).addToRequestQueue(request);
    }

    private void parseJobPostings(JSONArray response) {
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject obj = response.getJSONObject(i);
                jobPostings.add(new JobPosting(
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
    }

    private void restoreSwipePosition() {
        if (savedSwipePosition > 0 && savedSwipePosition < jobPostings.size()) {
            for (int i = 0; i < savedSwipePosition; i++) {
                swipeAdapter.removeJob(0);
            }
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

    public static Map<String, String> createAuthorizationHeaders(Context context) {
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
