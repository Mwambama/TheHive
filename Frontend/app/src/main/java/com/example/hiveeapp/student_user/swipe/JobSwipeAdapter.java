package com.example.hiveeapp.student_user.swipe;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobSwipeAdapter extends RecyclerView.Adapter<JobSwipeAdapter.JobViewHolder> {
    private List<JobPosting> jobPostings = new ArrayList<>();
    private Context context;
    private int studentId;

    public JobSwipeAdapter(Context context, int studentId) {
        this.context = context;
        this.studentId = studentId;
        loadJobPostings();
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_card, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobPosting job = jobPostings.get(position);
        holder.bind(job);
    }

    @Override
    public int getItemCount() {
        return jobPostings.size();
    }

    // Load job postings and exclude already applied jobs
    private void loadJobPostings() {
        String jobUrl = "http://coms-3090-063.class.las.iastate.edu:8080/job-posting";
        String appliedUrl = "http://coms-3090-063.class.las.iastate.edu:8080/applications/student?studentId=" + studentId;

        JsonArrayRequest jobRequest = new JsonArrayRequest(Request.Method.GET, jobUrl, null,
                jobsResponse -> fetchAppliedJobs(appliedUrl, jobsResponse),
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthorizationHeaders();
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(jobRequest);
    }

    // Fetch applied jobs to filter out from job postings
    private void fetchAppliedJobs(String url, JSONArray jobsResponse) {
        JsonArrayRequest appliedRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                appliedResponse -> filterJobs(jobsResponse, appliedResponse),
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthorizationHeaders();
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(appliedRequest);
    }

    // Filter out applied jobs from the main job list
    private void filterJobs(JSONArray jobsResponse, JSONArray appliedResponse) {
        List<Integer> appliedJobIds = new ArrayList<>();
        for (int i = 0; i < appliedResponse.length(); i++) {
            try {
                JSONObject appliedJob = appliedResponse.getJSONObject(i);
                appliedJobIds.add(appliedJob.getInt("jobPostingId"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        jobPostings.clear();
        for (int i = 0; i < jobsResponse.length(); i++) {
            try {
                JSONObject jobObject = jobsResponse.getJSONObject(i);
                int jobId = jobObject.getInt("jobPostingId");
                if (!appliedJobIds.contains(jobId)) {
                    jobPostings.add(parseJob(jobObject));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        notifyDataSetChanged();
    }

    // Parse job posting details into JobPosting object
    private JobPosting parseJob(JSONObject jobObject) throws JSONException {
        String companyName = jobObject.optString("companyName", "N/A");
        return new JobPosting(
                jobObject.getInt("jobPostingId"),
                jobObject.getString("title"),
                jobObject.getString("description"),
                jobObject.getString("summary"),
                jobObject.getDouble("salary"),
                jobObject.getString("jobType"),
                jobObject.getDouble("minimumGpa"),
                jobObject.getString("jobStart"),
                jobObject.getString("applicationStart"),
                jobObject.getString("applicationEnd"),
                jobObject.getInt("employerId"),
                companyName
        );
    }

    // ViewHolder to bind job posting data to views
    public class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, company, description, summary, salary, jobType, minimumGpa, jobStart, applicationStart, applicationEnd;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            company = itemView.findViewById(R.id.company);
            description = itemView.findViewById(R.id.description);
            summary = itemView.findViewById(R.id.summary);
            salary = itemView.findViewById(R.id.salary);
            jobType = itemView.findViewById(R.id.jobType);
            minimumGpa = itemView.findViewById(R.id.minimumGpa);
            jobStart = itemView.findViewById(R.id.jobStart);
            applicationStart = itemView.findViewById(R.id.applicationStart);
            applicationEnd = itemView.findViewById(R.id.applicationEnd);
        }

        public void bind(JobPosting job) {
            jobTitle.setText(job.getTitle());
            company.setText("Company: " + job.getCompanyName());
            description.setText(job.getDescription());
            summary.setText(job.getSummary());
            salary.setText("Salary: $" + job.getSalary());
            jobType.setText("Job Type: " + job.getJobType());
            minimumGpa.setText("Minimum GPA: " + job.getMinimumGpa());
            jobStart.setText("Job Start Date: " + job.getJobStart());
            applicationStart.setText("Application Start: " + job.getApplicationStart());
            applicationEnd.setText("Application End: " + job.getApplicationEnd());
        }
    }

    // Apply for job when swiped
    private void applyForJob(int jobPostingId) {
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/applications/apply";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("studentId", studentId);
            jsonObject.put("jobPostingId", jobPostingId);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(context, response, Toast.LENGTH_SHORT).show(),
                this::handleError) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonObject.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthorizationHeaders();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Set up swipe actions
    public ItemTouchHelper.SimpleCallback getSwipeCallback() {
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                JobPosting job = jobPostings.get(position);

                if (direction == ItemTouchHelper.RIGHT) {
                    applyForJob(job.getJobPostingId());
                } else if (direction == ItemTouchHelper.LEFT) {
                    Toast.makeText(context, "Job dismissed", Toast.LENGTH_SHORT).show();
                }

                jobPostings.remove(position);
                notifyItemRemoved(position);
            }
        };
    }

    // Authorization headers
    private Map<String, String> getAuthorizationHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        String username = "teststudent1@example.com";
        String password = "TestStudent1234@";
        String credentials = username + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        return headers;
    }

    // Handle errors for Volley requests
    private void handleError(VolleyError error) {
        String errorMessage = "Failed to apply. Please try again.";
        if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            if (statusCode == 401) {
                errorMessage = "Authorization failed. Please check your credentials.";
            } else if (statusCode == 500) {
                errorMessage = "You have already applied for this job.";
            }
        }
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        Log.e("JobSwipeAdapter", "Error: ", error);
    }
}
