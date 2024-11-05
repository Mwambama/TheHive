package com.example.hiveeapp.student_user.application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hiveeapp.R;
import java.util.List;

public class JobApplicationAdapter extends RecyclerView.Adapter<JobApplicationAdapter.ApplicationViewHolder> {

    private List<Application> applications;

    public JobApplicationAdapter(List<Application> applications) {
        this.applications = applications;
    }

    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_application, parent, false);
        return new ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationViewHolder holder, int position) {
        Application application = applications.get(position);
        holder.bind(application);
    }

    @Override
    public int getItemCount() {
        return applications.size();
    }

    public void updateApplications(List<Application> newApplications) {
        applications = newApplications;
        notifyDataSetChanged();
    }

    static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, status, appliedOn;

        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            status = itemView.findViewById(R.id.status);
            appliedOn = itemView.findViewById(R.id.appliedOn);
        }

        public void bind(Application application) {
            jobTitle.setText(application.getJobTitle());
            status.setText("Status: " + application.getStatus());
            appliedOn.setText("Applied On: " + application.getAppliedOn());
        }
    }
}
