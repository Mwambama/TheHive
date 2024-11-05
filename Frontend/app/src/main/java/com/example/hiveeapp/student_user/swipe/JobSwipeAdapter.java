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
import com.android.volley.toolbox.JsonObjectRequest;
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
        loadJobPostings();  // Load jobs when the adapter is initialized
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

    // Fetch all job postings and filter out those already applied
    public void loadJobPostings() {
        String jobUrl = "http://coms-3090-063.class.las.iastate.edu:8080/job-posting";
        String appliedUrl = "http://coms-3090-063.class.las.iastate.edu:8080/applications/student?studentId=" + studentId;

        JsonArrayRequest jobRequest = new JsonArrayRequest(Request.Method.GET, jobUrl, null,
                jobsResponse -> {
                    List<Integer> appliedJobIds = new ArrayList<>();

                    JsonArrayRequest appliedRequest = new JsonArrayRequest(Request.Method.GET, appliedUrl, null,
                            appliedResponse -> {
                                // Add applied job IDs to the list
                                for (int i = 0; i < appliedResponse.length(); i++) {
                                    try {
                                        JSONObject appliedJob = appliedResponse.getJSONObject(i);
                                        appliedJobIds.add(appliedJob.getInt("jobPostingId"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                // Filter out applied jobs
                                jobPostings.clear();
                                for (int i = 0; i < jobsResponse.length(); i++) {
                                    try {
                                        JSONObject jobObject = jobsResponse.getJSONObject(i);
                                        int jobId = jobObject.getInt("jobPostingId");

                                        // Only add jobs not in the applied list
                                        if (!appliedJobIds.contains(jobId)) {
                                            String companyName = jobObject.has("companyName")
                                                    ? jobObject.getString("companyName")
                                                    : "N/A";  // Default to "N/A" if companyName is missing
                                            JobPosting job = new JobPosting(
                                                    jobId,
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
                                            jobPostings.add(job);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                notifyDataSetChanged();
                            },
                            this::handleError) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            return getAuthorizationHeaders();
                        }
                    };

                    VolleySingleton.getInstance(context).addToRequestQueue(appliedRequest);
                },
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthorizationHeaders();
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(jobRequest);
    }


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

    private void handleError(VolleyError error) {
        if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
            Toast.makeText(context, "Authorization failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
        } else if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
            Toast.makeText(context, "You have already applied for this job.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to apply. Please try again.", Toast.LENGTH_SHORT).show();
        }
        Log.e("JobSwipeAdapter", "Error applying for job: ", error);
    }
}
