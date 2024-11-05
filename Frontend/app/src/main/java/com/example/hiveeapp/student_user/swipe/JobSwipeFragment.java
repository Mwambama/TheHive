package com.example.hiveeapp.student_user.swipe;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobSwipeFragment extends Fragment {

    private static final String GET_JOB_POSTINGS_URL = "http://coms-3090-063.class.las.iastate.edu:8080/job-posting";
    private RecyclerView swipeRecyclerView;
    private JobSwipeAdapter swipeAdapter;
    private List<JobPosting> jobPostings = new ArrayList<>();
    private int studentId;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        studentId = ((StudentMainActivity) context).getUserId();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_swipe, container, false);
        swipeRecyclerView = view.findViewById(R.id.jobSwipeRecyclerView);
        swipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeAdapter = new JobSwipeAdapter(getContext(), studentId);
        swipeRecyclerView.setAdapter(swipeAdapter);

        // Set up swipe gestures
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeAdapter.getSwipeCallback());
        itemTouchHelper.attachToRecyclerView(swipeRecyclerView);

        loadJobPostings();

        return view;
    }

    private void loadJobPostings() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, GET_JOB_POSTINGS_URL, null,
                response -> {
                    jobPostings.clear();
                    parseJobPostings(response);
                    swipeAdapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(getContext(), "Failed to load job postings", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createAuthorizationHeaders(getContext());
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    private void parseJobPostings(JSONArray response) {
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject obj = response.getJSONObject(i);
                jobPostings.add(new JobPosting(
                        obj.getInt("jobPostingId"),
                        obj.getString("title"),
                        obj.getString("description"),
                        obj.getString("summary"),
                        obj.getDouble("salary"),
                        obj.getString("jobType"),
                        obj.getDouble("minimumGpa"),
                        obj.getString("jobStart"),
                        obj.getString("applicationStart"),
                        obj.getString("applicationEnd")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates headers for API requests with authorization.
     */
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