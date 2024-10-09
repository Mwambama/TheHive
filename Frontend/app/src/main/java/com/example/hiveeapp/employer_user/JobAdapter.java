package com.example.hiveeapp.employer_user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.display.PostedJobs;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    private List<PostedJobs> jobs;

    public JobAdapter(List<PostedJobs> jobs) {
        this.jobs = jobs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracking_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostedJobs job = jobs.get(position);
        holder.jobTitleTextView.setText(job.getJobTitle());
        holder.jobDescriptionTextView.setText(job.getJobDescription());
        // Set other fields as necessary
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView jobTitleTextView;
        public TextView jobDescriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            jobTitleTextView = itemView.findViewById(R.id.tracking_item_text);
            jobDescriptionTextView = itemView.findViewById(R.id.tracking_item_description);
        }
    }
}
