package com.example.hiveeapp.employer_user.display;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiveeapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Adapter class for managing and displaying a list of employers in a RecyclerView.
 * Supports both read-only mode and editable mode (with update and delete options).
 */
public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.EmployerViewHolder> {

    private JSONArray jobs = new JSONArray();  // Holds the list of employers
    private final Context context;  // The context in which the adapter is used
    private final boolean isEditable;  // Indicates whether the list is editable (shows update/delete buttons)
    private static final String USER_PREFS = "UserPrefs";  // SharedPreferences key for user data

    /**
     * Constructor for the EmployerAdapter.
     *
     * @param context    The context in which the adapter is used
     * @param isEditable Indicates if the employer list is editable (shows update/delete buttons)
     */
    public JobsAdapter(Context context, boolean isEditable) {
        this.context = context;
        this.isEditable = isEditable;
    }

    /**
     * Updates the employer list and refreshes the RecyclerView.
     *
     * @param jobs The JSONArray containing the employer data
     */
    public void setJobs(JSONArray jobs) {
        this.jobs = jobs;
        notifyDataSetChanged();  // Notify the adapter to refresh the data
    }


    @Override
    public EmployerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each employer item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.activity_apply, parent, false);
        return new EmployerViewHolder(view);
    }

    /**
     * Binds the data for a specific employer to the view holder and handles
     * the display of employer information such as name, email, phone, and address.
     * It also sets up functionality for updating and deleting employers, depending
     * on the editability of the list.
     *
     * @param holder   The view holder to bind the employer data to.
     * @param position The position of the employer in the RecyclerView list.
     */
    @Override
    public void onBindViewHolder(EmployerViewHolder holder, int position) {
        try {



            // Get the employer object at the current position
            JSONObject job = jobs.getJSONObject(position);

            holder.analyticsButton.setOnClickListener(v -> {
                long jobId = job.optLong("jobPostingId", -1); // Get the job ID from the JSON object
                if (jobId == -1) {
                    Toast.makeText(context, "Invalid Job ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                EmployerApis.getGraphImage(context, jobId,
                        response -> {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(response, 0, response.length); // Convert byte[] to Bitmap
                            displayPopupWithImage(context, bitmap); // Display the image in a popup
                        },
                        error -> {
                            Toast.makeText(context, "Failed to fetch graph image", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Error fetching graph image: ", error);
                        });
            });


            // Extract employer details, handling null cases with default values

            String title = job.optString("title", "N/A");
            String description = job.optString("description", "N/A");
            String summary = job.optString("summary", "N/A");
            double salary = job.optDouble("salary", 0.0);
            String jobType = job.optString("jobType", "N/A");
            double minimumGpa = job.optDouble("minimumGpa", 0.0);
            String jobStart = job.optString("jobStart", "N/A");
            String applicationStart = job.optString("applicationStart", "N/A");
            String applicationEnd = job.optString("applicationEnd", "N/A");

            // Set employer details to the corresponding TextViews


            holder.titleTextView.setText(title);
            holder.descriptionTextView.setText(description);
            holder.summaryTextView.setText(summary);
            holder.salaryTextView.setText(String.valueOf(salary));
            holder.jobTypeTextView.setText(jobType);
            holder.minimumGpaTextView.setText(String.valueOf(minimumGpa));
            holder.jobStartTextView.setText(jobStart);
            holder.applicationStartTextView.setText(applicationStart);
            holder.applicationEndTextView.setText(applicationEnd);

            // If the list is not editable, hide the update and delete buttons
            if (!isEditable) {
                holder.updateButton.setVisibility(View.GONE);
                holder.deleteButton.setVisibility(View.GONE);
            } else {
                // Setup update button click listener
                holder.updateButton.setOnClickListener(v -> {
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        try {
                            JSONObject currentEmployer = jobs.getJSONObject(currentPosition);
                            showEditBottomSheet(currentEmployer, currentPosition);  // Show edit dialog
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                // Setup delete button click listener
                holder.deleteButton.setOnClickListener(v -> {
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        try {
                            JSONObject currentEmployer = jobs.getJSONObject(currentPosition);
                            new AlertDialog.Builder(context)
                                    .setTitle("Delete job")
                                    .setMessage("Are you sure you want to delete this job?")
                                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                        try {
                                            long employerId = currentEmployer.getLong("jobPostingId");
                                            EmployerApis.deleteEmployer(context, employerId,
                                                    response -> {
                                                        // Remove deleted employer and refresh RecyclerView
                                                        jobs.remove(currentPosition);
                                                        notifyItemRemoved(currentPosition);
                                                        notifyItemRangeChanged(currentPosition, jobs.length());
                                                        Toast.makeText(context, "job deleted successfully", Toast.LENGTH_SHORT).show();
                                                    },
                                                    error -> Toast.makeText(context, "Error deleting job: " + error.getMessage(), Toast.LENGTH_SHORT).show()
                                            );
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(context, "Error deleting job", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null)
                                    .show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error retrieving job data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error parsing job data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return jobs.length();  // Return the number of employers in the list
    }

    /**
     * Displays a BottomSheetDialog for editing employer details.
     *
     * @param //employer The JSONObject containing the employer details
     * @param position The position of the employer in the list
     */
    private void showEditBottomSheet(JSONObject job, int position) {
        try {
            // Inflate the bottom sheet layout
            View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_edit_job, null);
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(view);

            // Initialize the fields from the layout
            TextView editTitle = view.findViewById(R.id.editTitle);
            TextView editDescription = view.findViewById(R.id.editDescription);
            TextView editSummary = view.findViewById(R.id.editSummary);
            TextView editSalary = view.findViewById(R.id.editSalary);
            TextView editJobType = view.findViewById(R.id.editJobType);
            TextView editMinimumGpa = view.findViewById(R.id.editMinimumGpa);
            TextView editJobStart = view.findViewById(R.id.editJobStart);
            TextView editApplicationStart = view.findViewById(R.id.editApplicationStart);
            TextView editApplicationEnd = view.findViewById(R.id.editApplicationEnd);
            View saveChangesButton = view.findViewById(R.id.saveChangesButton);

            // Pre-fill the fields with the current job data

            editTitle.setText(job.optString("title"));
            editDescription.setText(job.optString("description"));
            editSummary.setText(job.optString("summary"));
            editSalary.setText(String.valueOf(job.optDouble("salary")));
            editJobType.setText(job.optString("jobType"));
            editMinimumGpa.setText(String.valueOf(job.optDouble("minimumGpa")));
            editJobStart.setText(job.optString("jobStart"));
            editApplicationStart.setText(job.optString("applicationStart"));
            editApplicationEnd.setText(job.optString("applicationEnd"));


            // Save changes when button is clicked
            saveChangesButton.setOnClickListener(v -> {
                // Get updated values from the fields

                String updatedTitle = editTitle.getText().toString().trim();
                String updatedDescription = editDescription.getText().toString().trim();
                String updatedSummary = editSummary.getText().toString().trim();
                double updatedSalary = Double.parseDouble(editSalary.getText().toString().trim());
                String updatedJobType = editJobType.getText().toString().trim();
                double updatedMinimumGpa = Double.parseDouble(editMinimumGpa.getText().toString().trim());
                String updatedJobStart = editJobStart.getText().toString().trim();
                String updatedApplicationStart = editApplicationStart.getText().toString().trim();
                String updatedApplicationEnd = editApplicationEnd.getText().toString().trim();


                try {
                    // Retrieve companyId from SharedPreferences
                    SharedPreferences preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
                    // Retrieve companyId directly from the job JSONObject
                    long employerId = job.optLong("employerId", -1);
                    Log.d("EmployerApis", "Retrieved employerId: " + employerId);

                    if (employerId == -1) {
                        Toast.makeText(context, "Error: employerId not found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Ensure jobPostingId is provided
                    long jobPostingId = job.optLong("jobPostingId", -1);
                    if (jobPostingId == -1) {
                        Toast.makeText(context, "Error: Job PostingId not found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Construct a new JSONObject with updated data
                    JSONObject updatedJob = new JSONObject();
                    updatedJob.put("jobPostingId", jobPostingId); // Assuming this is the correct ID
                    updatedJob.put("title", updatedTitle);
                    updatedJob.put("description", updatedDescription);
                    updatedJob.put("summary", updatedSummary);
                    updatedJob.put("salary", updatedSalary);
                    updatedJob.put("jobType", updatedJobType);
                    updatedJob.put("minimumGpa", updatedMinimumGpa);
                    updatedJob.put("jobStart", updatedJobStart);
                    updatedJob.put("applicationStart", updatedApplicationStart);
                    updatedJob.put("applicationEnd", updatedApplicationEnd);
                   updatedJob.put("employerId", employerId); // or updatedJob.put("employerId", companyId);

                    // Print the JSON payload for debugging
                    Log.d("EmployerApis", "Update Job Payload: " + updatedJob.toString());

                    // Update employer via EmployerApis
                    EmployerApis.performJobUpdate(context, updatedJob,
                            response -> {
                                try {
                                    // Update the employer in the list and refresh RecyclerView
                                    jobs.put(position, updatedJob);
                                    notifyItemChanged(position);
                                    bottomSheetDialog.dismiss();
                                    Toast.makeText(context, "Job updated successfully!", Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "Error updating job list", Toast.LENGTH_SHORT).show();
                                }
                            },
                            error -> {
                                // Log error response for debugging
                               // Log.e("EmployerApis", "Error updating employer: " + error.toString());
                                Toast.makeText(context, "Error updating job", Toast.LENGTH_SHORT).show();
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error constructing update request", Toast.LENGTH_SHORT).show();
                }

            });

            bottomSheetDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error opening edit form", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayPopupWithImage(Context context, Bitmap bitmap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Analytics Image");

        // Create an ImageView to display the image
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setAdjustViewBounds(true);

        builder.setView(imageView);

        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    /**
     * ViewHolder class to hold views for each employer item.
     */
    class EmployerViewHolder extends RecyclerView.ViewHolder {
      //  TextView nameTextView, emailTextView, phoneTextView, addressTextView;

        TextView titleTextView;         // TextView for job title
        TextView descriptionTextView;   // TextView for job description
        TextView summaryTextView;       // TextView for job summary
        TextView salaryTextView;        // TextView for job salary
        TextView jobTypeTextView;       // TextView for job type
        TextView minimumGpaTextView;    // TextView for minimum GPA
        TextView jobStartTextView;      // TextView for job start date
        TextView applicationStartTextView; // TextView for application start date
        TextView applicationEndTextView; // TextView for application end date
        ImageButton updateButton;       // Button for updating job details
        ImageButton deleteButton;       // Button for deleting job

        MaterialButton analyticsButton;  // Button for fetching the graph
        ImageView analyticsImageView;    // ImageView for displaying the graph


        public EmployerViewHolder(View itemView) {
            super(itemView);
            // Initialize views from the layout

            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            summaryTextView = itemView.findViewById(R.id.summaryTextView);
            salaryTextView = itemView.findViewById(R.id.salaryTextView);
            jobTypeTextView = itemView.findViewById(R.id.jobTypeTextView);
            minimumGpaTextView = itemView.findViewById(R.id.minimumGpaTextView);
            jobStartTextView = itemView.findViewById(R.id.jobStartTextView);
            applicationStartTextView = itemView.findViewById(R.id.applicationStartTextView);
            applicationEndTextView = itemView.findViewById(R.id.applicationEndTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            analyticsButton = itemView.findViewById(R.id.analyticsButton);  // Added Graph Button
            analyticsImageView = itemView.findViewById(R.id.analyticsImageView);  // Added ImageView for Graph

        }
    }
}