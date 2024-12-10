package com.example.hiveeapp.student_user.application;

import static com.example.hiveeapp.student_user.swipe.JobSwipeFragment.AUTH_PREFERENCES;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.StudentMainActivity;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AppliedJobsFragment extends Fragment {

    private static final String TAG = "AppliedJobsFragment";
    private static final String GET_APPLICATIONS_URL = "http://coms-3090-063.class.las.iastate.edu:8080/applications/student";
    private static final int MAX_PENDING_DAYS = 30;

    private RecyclerView recyclerView;
    private AppliedJobsAdapter adapter;
    private List<Object> groupedApplications = new ArrayList<>();
    private int studentId;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            studentId = ((StudentMainActivity) context).getUserId();
        } catch (ClassCastException e) {
            throw new IllegalStateException("Activity must implement StudentMainActivity to provide userId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applied_jobs, container, false);

        recyclerView = view.findViewById(R.id.applicationsRecyclerView);
        adapter = new AppliedJobsAdapter(getContext(), groupedApplications);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadApplications();

        return view;
    }

    private void loadApplications() {
        // Use query parameter for studentId instead of path parameter
        String url = GET_APPLICATIONS_URL + "?studentId=" + studentId;
        Log.d(TAG, "Fetching applications from URL: " + url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<JobApplication> applications = parseApplications(response);
                    groupedApplications = groupApplications(filterApplications(applications));
                    adapter.updateApplications(groupedApplications);
                },
                error -> {
                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        Log.e(TAG, "Error loading applications: " + statusCode, error);
                        Toast.makeText(requireContext(), "Failed to load applications. Error code: " + statusCode, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Network error occurred", error);
                        Toast.makeText(requireContext(), "Network error. Check your connection.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createAuthorizationHeaders();
            }
        };

        VolleySingleton.getInstance(requireContext()).addToRequestQueue(request);
    }

    private List<JobApplication> parseApplications(JSONArray response) {
        List<JobApplication> applications = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject obj = response.getJSONObject(i);

                int applicationId = obj.optInt("applicationId", -1);
                int jobPostingId = obj.optInt("jobPostingId", -1);
                int studentId = obj.optInt("studentId", -1);
                String jobTitle = obj.optString("jobTitle", "Untitled Job");
                String status = obj.optString("status", "Unknown");
                String appliedOn = obj.optString("appliedOn", "");

                if (applicationId != -1 && !jobTitle.isEmpty()) {
                    applications.add(new JobApplication(applicationId, jobPostingId, studentId, jobTitle, status, appliedOn));
                } else {
                    Log.w(TAG, "Skipping application due to missing required fields: " + obj.toString());
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing applications", e);
        }
        return applications;
    }

    private List<JobApplication> filterApplications(List<JobApplication> applications) {
        List<JobApplication> filtered = new ArrayList<>();
        long now = System.currentTimeMillis();

        for (JobApplication app : applications) {
            if ("NOT_ACCEPTED".equalsIgnoreCase(app.getStatus())) {
                continue;
            }

            if ("PENDING".equalsIgnoreCase(app.getStatus())) {
                long appliedTime = parseDateToMillis(app.getAppliedOn());
                if ((now - appliedTime) > MAX_PENDING_DAYS * 24 * 60 * 60 * 1000L) {
                    continue;
                }
            }

            filtered.add(app);
        }

        return filtered;
    }

    private List<Object> groupApplications(List<JobApplication> applications) {
        List<Object> groupedItems = new ArrayList<>();

        groupedItems.add("Accepted Applications");
        for (JobApplication app : applications) {
            if ("Accepted".equalsIgnoreCase(app.getStatus())) {
                groupedItems.add(app);
            }
        }

        groupedItems.add("Pending Applications");
        for (JobApplication app : applications) {
            if ("Pending".equalsIgnoreCase(app.getStatus())) {
                groupedItems.add(app);
            }
        }

        return groupedItems;
    }

    private long parseDateToMillis(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date parsedDate = sdf.parse(date);
            return parsedDate != null ? parsedDate.getTime() : 0;
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: " + date, e);
            return 0;
        }
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
}
