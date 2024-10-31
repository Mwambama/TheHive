package com.example.hiveeapp.student_user.swipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hiveeapp.R;
import java.util.List;

public class JobSwipeAdapter extends RecyclerView.Adapter<JobSwipeAdapter.JobViewHolder> {
    private List<JobPosting> jobPostings;
    private Context context;

    public JobSwipeAdapter(List<JobPosting> jobPostings, Context context) {
        this.jobPostings = jobPostings;
        this.context = context;
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

        // Set each field with job details
        holder.jobTitle.setText(job.getTitle());
        holder.company.setText("Employer ID: " + job.getEmployerId());
        holder.description.setText(job.getDescription());
        holder.summary.setText(job.getSummary());
        holder.salary.setText("Salary: $" + job.getSalary());
        holder.jobType.setText("Job Type: " + job.getJobType());
        holder.minimumGpa.setText("Minimum GPA: " + job.getMinimumGpa());
        holder.jobStart.setText("Job Start Date: " + job.getJobStart());
        holder.applicationStart.setText("Application Start: " + job.getApplicationStart());
        holder.applicationEnd.setText("Application End: " + job.getApplicationEnd());
    }

    @Override
    public int getItemCount() {
        return jobPostings.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, company, description, summary, salary, jobType, minimumGpa, jobStart, applicationStart, applicationEnd;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize all TextViews with corresponding IDs from item_job_card.xml
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
    }
}
