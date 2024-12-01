package com.example.hiveeapp.student_user.swipe;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobSwipeAdapter extends BaseAdapter {
    private static final String TAG = "JobSwipeAdapter";
    private Context context;
    private List<JobPosting> jobPostings;
    private int studentId;

    public JobSwipeAdapter(Context context, int studentId) {
        this.context = context;
        this.studentId = studentId;
        this.jobPostings = new ArrayList<>();
        loadJobPostings();
    }

    @Override
    public int getCount() {
        return jobPostings.size();
    }

    @Override
    public Object getItem(int position) {
        return jobPostings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_job_card, parent, false);
        }

        JobPosting job = jobPostings.get(position);

        TextView jobTitle = convertView.findViewById(R.id.jobTitle);
        TextView jobDescription = convertView.findViewById(R.id.description);
        TextView jobSalary = convertView.findViewById(R.id.salary);

        jobTitle.setText(job.getTitle());
        jobDescription.setText(job.getDescription());
        jobSalary.setText("Salary: $" + job.getSalary());

        return convertView;
    }

    private void loadJobPostings() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, JobSwipeFragment.GET_JOB_POSTINGS_URL, null,
                response -> {
                    jobPostings.clear();
                    parseJobPostings(response);
                    notifyDataSetChanged();
                },
                error -> {
                    Log.e(TAG, "Error loading job postings", error);
                    Toast.makeText(context, "Failed to load job postings", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createAuthorizationHeaders(context);
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
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
            e.printStackTrace();
        }
    }

    public void applyForJob(int jobPostingId) {
        JobApplicationUtils.applyForJob(context, studentId, jobPostingId,
                () -> {
                    // Success callback
                    Toast.makeText(context, "Application submitted successfully!", Toast.LENGTH_SHORT).show();
                },
                () -> {
                    // Failure callback
                    Toast.makeText(context, "Failed to submit application. Please try again.", Toast.LENGTH_SHORT).show();
                }
        );
    }

    public void removeJob(int position) {
        if (position < jobPostings.size()) {
            jobPostings.remove(position);
            notifyDataSetChanged();
        }
    }

    /**
     * Updates the list of job postings dynamically.
     */
    public void setJobPostings(List<JobPosting> newJobPostings) {
        this.jobPostings = new ArrayList<>(newJobPostings);
        notifyDataSetChanged();
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
