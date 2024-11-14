package com.example.hiveeapp.student_user.application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hiveeapp.R;
import java.util.List;

/**
 * Adapter for displaying a list of job applications in a RecyclerView.
 */
public class JobApplicationAdapter extends RecyclerView.Adapter<JobApplicationAdapter.ApplicationViewHolder> {

    private List<Application> applications;

    /**
     * Constructor for JobApplicationAdapter.
     *
     * @param applications List of applications to display.
     */
    public JobApplicationAdapter(List<Application> applications) {
        this.applications = applications;
    }

    /**
     * Inflates the layout for each job application item in the RecyclerView.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The view type of the new View.
     * @return A new ApplicationViewHolder that holds the view for each job application item.
     */
    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_application, parent, false);
        return new ApplicationViewHolder(view);
    }

    /**
     * Binds data to the job application item view.
     *
     * @param holder   The ApplicationViewHolder.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ApplicationViewHolder holder, int position) {
        Application application = applications.get(position);
        holder.bind(application);
    }

    /**
     * Returns the total number of job application items.
     *
     * @return The size of the applications list.
     */
    @Override
    public int getItemCount() {
        return applications.size();
    }

    /**
     * Updates the list of applications and refreshes the RecyclerView.
     *
     * @param newApplications The new list of applications to display.
     */
    public void updateApplications(List<Application> newApplications) {
        applications = newApplications;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for job application items.
     */
    static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, status, appliedOn;

        /**
         * Constructor for ApplicationViewHolder.
         *
         * @param itemView The view of the job application item.
         */
        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            status = itemView.findViewById(R.id.status);
            appliedOn = itemView.findViewById(R.id.appliedOn);
        }

        /**
         * Binds the application data to the item view.
         *
         * @param application The application data to bind.
         */
        public void bind(Application application) {
            jobTitle.setText(application.getJobTitle());
            status.setText("Status: " + application.getStatus());
            appliedOn.setText("Applied On: " + application.getAppliedOn());
        }
    }
}
