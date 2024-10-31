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
        holder.jobTitle.setText(job.getJobTitle());
        holder.company.setText(job.getCompanyName());
    }

    @Override
    public int getItemCount() {
        return jobPostings.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, company;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            company = itemView.findViewById(R.id.company);
        }
    }
}

