package com.example.hiveeapp.student_user.swipe;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class JobSwipeAdapter extends RecyclerView.Adapter<JobSwipeAdapter.JobViewHolder> {
    private List<JobPosting> jobPostings;
    private Context context;
    private int studentId;

    public JobSwipeAdapter(List<JobPosting> jobPostings, Context context, int studentId) {
        this.jobPostings = jobPostings;
        this.context = context;
        this.studentId = studentId;
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

    // Method to apply for a job
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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> Toast.makeText(context, "Application submitted successfully!", Toast.LENGTH_SHORT).show(),
                this::handleError);

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // Error handler for application request
    private void handleError(VolleyError error) {
        if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
            Toast.makeText(context, "You have already applied for this job.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to apply. Please try again.", Toast.LENGTH_SHORT).show();
        }
        Log.e("JobSwipeAdapter", "Error applying for job: ", error);
    }

    // Attach the swipe functionality
    public ItemTouchHelper.SimpleCallback getSwipeCallback() {
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;  // We are only interested in swipe actions
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                JobPosting job = jobPostings.get(position);

                if (direction == ItemTouchHelper.RIGHT) {
                    // Apply on right swipe
                    applyForJob(job.getJobPostingId());
                }

                // Remove the item from the list after swipe
                jobPostings.remove(position);
                notifyItemRemoved(position);
            }
        };
    }
}