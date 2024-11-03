package com.example.hiveeapp.employer_user.applications;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.display.EmployerApis;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





public class applicationAdapter extends RecyclerView.Adapter<applicationAdapter.ApplicationViewHolder> {
    private JSONArray applications = new JSONArray();  // Holds the list of applications
    private final Context context;  // The context in which the adapter is used
    private final boolean isEditable;  // Indicates whether the list is editable
    private static final String USER_PREFS = "UserPrefs";  // SharedPreferences key for user data
    private static final String ERROR_MESSAGE = "Error";  // Generic error message for Toasts

    public applicationAdapter(Context context, boolean isEditable) {
        this.context = context;
        this.isEditable = isEditable;
    }

    public void setApplications(JSONArray applications) {
        this.applications = applications;
        notifyDataSetChanged();
    }

    @Override
    public ApplicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_employer_applications, parent, false);
        return new ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ApplicationViewHolder holder, int position) {
        try {
            JSONObject applications = this.applications.getJSONObject(position);

            String jobTitle = applications.optString("jobTitle", "N/A");  // Use "N/A" directly
            String status = applications.optString("status", "N/A");  // Use "N/A" directly
            String appliedOn = applications.optString("appliedOn", "N/A");  // Use "N/A" directly

            holder.titleTextView.setText(jobTitle);
            holder.statusTextView.setText(status);
            holder.appliedOnTextView.setText(appliedOn);

            if (!isEditable) {
                holder.acceptButton.setVisibility(View.GONE);
                holder.rejectButton.setVisibility(View.GONE);
            } else {
                setupAcceptButton(holder, applications, position);
                setupRejectButton(holder, applications, position);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showToast(ERROR_MESSAGE + ": " + e.getMessage());
        }
    }


    private void setupRejectButton(ApplicationViewHolder holder, JSONObject job, int position) {
        holder.rejectButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Reject Application")
                    .setMessage("Are you sure you want to reject this application?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        try {
                            // Create a new JSONObject for the rejection
                            JSONObject rejectionData = new JSONObject();
                            rejectionData.put("jobPostingId", job.getLong("jobPostingId"));

                            applicationsApi.RejectApplication(context, rejectionData,
                                    response -> {
                                        // Update the applications list and refresh RecyclerView
                                        applications.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, applications.length()); // Correct usage
                                        Toast.makeText(context, "Application rejected successfully!", Toast.LENGTH_SHORT).show();
                                    },
                                    error -> {
                                        // Show error message to the user
                                        Toast.makeText(context, "Error rejecting application", Toast.LENGTH_SHORT).show();
                                    });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error constructing rejection request", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        });
    }




    private void setupAcceptButton(ApplicationViewHolder holder, JSONObject job, int position) {
        holder.acceptButton.setOnClickListener(v -> {
            showEditBottomSheet(job, position, "Accepted");
        });
    }

//    private void setupRejectButton(ApplicationViewHolder holder, JSONObject job, int position) {
//        holder.rejectButton.setOnClickListener(v -> {
//            new AlertDialog.Builder(context)
//                    .setTitle("Reject Application")
//                    .setMessage("Are you sure you want to reject this application?")
//                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//                        try {
//                            long jobId = job.getLong("jobPostingId");
//                            applicationsApi.RejectApplication(context, jobId,
//                                    response -> {
//                                        applications.remove(position);
//                                        notifyItemRemoved(position);
//                                        notifyItemRangeChanged(position, applications.length());
//                                        showToast("Application rejected successfully");
//                                    },
//                                    error -> showToast("Error rejecting application: " + error.getMessage()));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            showToast(ERROR_MESSAGE + ": " + e.getMessage());
//                        }
//                    })
//                    .setNegativeButton(android.R.string.no, null)
//                    .show();
//        });
//    }

    private void showEditBottomSheet(JSONObject job, int position, String action) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_employer_application, null);

        // Declare the EditText fields and Buttons
        EditText editJobTitle = bottomSheetView.findViewById(R.id.editJobTitle);
        EditText editStatus = bottomSheetView.findViewById(R.id.editStatus);
        EditText editAppliedOn = bottomSheetView.findViewById(R.id.editAppliedOn);
        Button acceptButton = bottomSheetView.findViewById(R.id.acceptButton);
        Button rejectButton = bottomSheetView.findViewById(R.id.rejectButton);

        // Populate the fields with existing job data
        editJobTitle.setText(job.optString("jobTitle", ""));
        editStatus.setText(action); // Set the status based on the action (Accepted or Rejected)
        editAppliedOn.setText(job.optString("appliedOn", ""));

        acceptButton.setOnClickListener(v -> {
            // Use the acceptApplication method
            acceptApplication(job, position, editJobTitle, editAppliedOn);
            bottomSheetDialog.dismiss();
        });

        rejectButton.setOnClickListener(v -> {
            // Use the rejectApplication method
            rejectApplication(job, position, editJobTitle, editAppliedOn);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();  // Show the dialog
    }

    private void acceptApplication(JSONObject job, int position, EditText jobTitleField, EditText appliedOnField) {
        try {
            long jobId = job.getLong("jobPostingId");
            JSONObject acceptedJob = new JSONObject();
            acceptedJob.put("jobTitle", jobTitleField.getText().toString());
            acceptedJob.put("status", "Accepted");
            acceptedJob.put("appliedOn", appliedOnField.getText().toString());

            applicationsApi.AcceptApplication(context, acceptedJob,
                    response -> {
                        try {
                            applications.put(position, acceptedJob); // Update the local applications array
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        notifyItemChanged(position);
                        Toast.makeText(context, "Application accepted successfully!", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        // Show error message to the user
                        Toast.makeText(context, "Error accepting application", Toast.LENGTH_SHORT).show();
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error constructing acceptance request", Toast.LENGTH_SHORT).show();
        }
    }
    private void rejectApplication(JSONObject job, int position, EditText jobTitleField, EditText appliedOnField) {
        try {
            long applicationId = job.getLong("jobPostingId");
            JSONObject rejectedJob = new JSONObject();
            rejectedJob.put("jobTitle", jobTitleField.getText().toString());
            rejectedJob.put("status", "Rejected");
            rejectedJob.put("appliedOn", appliedOnField.getText().toString());

            applicationsApi.RejectApplication(context, rejectedJob,
                    response -> {
                        applications.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, applications.length()); // Correct usage
                        Toast.makeText(context, "Application rejected successfully!", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        // Show error message to the user
                        Toast.makeText(context, "Error rejecting application", Toast.LENGTH_SHORT).show();
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error constructing rejection request", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return applications.length();
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView statusTextView;
        TextView appliedOnTextView;
        ImageButton acceptButton;
        ImageButton rejectButton;

        ApplicationViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.jobTitleTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            appliedOnTextView = itemView.findViewById(R.id.appliedOnTextView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }
    }
}










// only has one one button to update  which makes no sense when I am trying to have  two buttons for that job


//
//public class applicationAdapter extends RecyclerView.Adapter<applicationAdapter.ApplicationViewHolder> {
//    private JSONArray applications = new JSONArray();  // Holds the list of applications
//    private final Context context;  // The context in which the adapter is used
//    private final boolean isEditable;  // Indicates whether the list is editable
//    private static final String USER_PREFS = "UserPrefs";  // SharedPreferences key for user data
//    private static final String ERROR_MESSAGE = "Error";  // Generic error message for Toasts
//
//    public applicationAdapter(Context context, boolean isEditable) {
//        this.context = context;
//        this.isEditable = isEditable;
//    }
//
//    public void setApplications(JSONArray applications) {
//        this.applications = applications;
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public ApplicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_employer_applications, parent, false);
//        return new ApplicationViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ApplicationViewHolder holder, int position) {
//        try {
//            JSONObject job = applications.getJSONObject(position);
//            String jobTitle = job.optString("jobTitle", "N/A");  // Use "N/A" directly
//            String status = job.optString("status", "N/A");  // Use "N/A" directly
//            String appliedOn = job.optString("appliedOn", "N/A");  // Use "N/A" directly
//
//            holder.titleTextView.setText(jobTitle);
//            holder.statusTextView.setText(status);
//            holder.appliedOnTextView.setText(appliedOn);
//
//            if (!isEditable) {
//                holder.acceptButton.setVisibility(View.GONE);
//                holder.rejectButton.setVisibility(View.GONE);
//            } else {
//                setupAcceptButton(holder, job, position);
//                setupRejectButton(holder, job, position);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            showToast(ERROR_MESSAGE + ": " + e.getMessage());
//        }
//    }
//
//    private void setupAcceptButton(ApplicationViewHolder holder, JSONObject job, int position) {
//        holder.acceptButton.setOnClickListener(v -> {
//            showEditBottomSheet(job, position, "Accepted");
//        });
//    }
//
//    private void setupRejectButton(ApplicationViewHolder holder, JSONObject job, int position) {
//        holder.rejectButton.setOnClickListener(v -> {
//            new AlertDialog.Builder(context)
//                    .setTitle("Reject Application")
//                    .setMessage("Are you sure you want to reject this application?")
//                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//                        try {
//                            long jobId = job.getLong("jobPostingId");
//                            applicationsApi.RejectApplication(context, jobId,
//                                    response -> {
//                                        applications.remove(position);
//                                        notifyItemRemoved(position);
//                                        notifyItemRangeChanged(position, applications.length());
//                                        showToast("Application rejected successfully");
//                                    },
//                                    error -> showToast("Error rejecting application: " + error.getMessage()));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            showToast(ERROR_MESSAGE + ": " + e.getMessage());
//                        }
//                    })
//                    .setNegativeButton(android.R.string.no, null)
//                    .show();
//        });
//    }
//
//    private void showEditBottomSheet(JSONObject job, int position, String action) {
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
//        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_employer_application, null);
//
//        // Declare the EditText fields and Buttons
//        EditText editJobTitle = bottomSheetView.findViewById(R.id.editJobTitle);
//        EditText editStatus = bottomSheetView.findViewById(R.id.editStatus);
//        EditText editAppliedOn = bottomSheetView.findViewById(R.id.editAppliedOn);
//        Button acceptButton = bottomSheetView.findViewById(R.id.acceptButton);
//        Button rejectButton = bottomSheetView.findViewById(R.id.rejectButton);
//
//        // Populate the fields with existing job data
//        try {
//            editJobTitle.setText(job.optString("jobTitle", ""));
//            editStatus.setText(action); // Set the status based on the action (Accepted or Rejected)
//            editAppliedOn.setText(job.optString("appliedOn", ""));
//        } catch (JSONException e) {
//            e.printStackTrace();
//            showToast(ERROR_MESSAGE + ": " + e.getMessage());
//        }
//
//        acceptButton.setOnClickListener(v -> {
//            // Update the application status to "Accepted"
//            updateApplicationStatus(job, position, "Accepted", editJobTitle, editAppliedOn);
//            bottomSheetDialog.dismiss();
//        });
//
//        rejectButton.setOnClickListener(v -> {
//            // Update the application status to "Rejected"
//            updateApplicationStatus(job, position, "Rejected", editJobTitle, editAppliedOn);
//            bottomSheetDialog.dismiss();
//        });
//
//        bottomSheetDialog.setContentView(bottomSheetView);
//        bottomSheetDialog.show();  // Show the dialog
//    }
//
//    private void updateApplicationStatus(JSONObject job, int position, String status, EditText jobTitleField, EditText appliedOnField) {
//        try {
//            JSONObject updateApplication = new JSONObject();
//            updateApplication.put("jobTitle", jobTitleField.getText().toString());
//            updateApplication.put("status", status);
//            updateApplication.put("appliedOn", appliedOnField.getText().toString());
//
//            // Call your API to update the job application
//            applicationsApi.UpdateApplication(context, updateApplication, position,
//                    response -> {
//                        applications.put(position, updateApplication); // Update the local applications array
//                        notifyItemChanged(position);
//                        showToast("Application " + status.toLowerCase() + " successfully");
//                    },
//                    error -> {
//                        showToast("Error updating application: " + error.getMessage());
//                    }
//            );
//        } catch (JSONException e) {
//            e.printStackTrace();
//            showToast(ERROR_MESSAGE + ": " + e.getMessage());
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return applications.length();
//    }
//
//    private void showToast(String message) {
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//    }
//
//    static class ApplicationViewHolder extends RecyclerView.ViewHolder {
//        TextView titleTextView;
//        TextView statusTextView;
//        TextView appliedOnTextView;
//        ImageButton acceptButton;
//        ImageButton rejectButton;
//
//        ApplicationViewHolder(View itemView) {
//            super(itemView);
//            titleTextView = itemView.findViewById(R.id.jobTitleTextView);
//            statusTextView = itemView.findViewById(R.id.statusTextView);
//            appliedOnTextView = itemView.findViewById(R.id.appliedOnTextView);
//            acceptButton = itemView.findViewById(R.id.acceptButton);
//            rejectButton = itemView.findViewById(R.id.rejectButton);
//        }
//    }
//}
//
//
//
//











///**
// * Adapter class for managing and displaying a list of applications in a RecyclerView.
// * Supports both read-only mode and editable mode (with update and delete options).
// */
//public class applicationAdapter extends RecyclerView.Adapter<applicationAdapter.ApplicationViewHolder> {
//
//    private JSONArray applications = new JSONArray();  // Holds the list of applications
//    private final Context context;  // The context in which the adapter is used
//    private final boolean isEditable;  // Indicates whether the list is editable (shows accept/reject buttons)
//    private static final String USER_PREFS = "UserPrefs";  // SharedPreferences key for user data
//
//    /**
//     * Constructor for the ApplicationAdapter.
//     *
//     * @param context    The context in which the adapter is used
//     * @param isEditable Indicates if the application list is editable (shows accept/reject buttons)
//     */
//    public applicationAdapter (Context context, boolean isEditable) {
//        this.context = context;
//        this.isEditable = isEditable;
//    }
//
//    /**
//     * Updates the application list and refreshes the RecyclerView.
//     *
//     * @param applications The JSONArray containing the application data
//     */
//    public void setApplications(JSONArray applications) {
//        this.applications = applications;
//        notifyDataSetChanged();  // Notify the adapter to refresh the data
//    }
//
//    @Override
//    public ApplicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        // Inflate the layout for each application item in the RecyclerView
//        View view = LayoutInflater.from(context).inflate(R.layout.item_employer_applications, parent, false);
//        return new ApplicationViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ApplicationViewHolder holder, int position) {
//        try {
//            // Get the application object at the current position
//            JSONObject job = applications.getJSONObject(position);
//
//            // Extract the relevant data from the JSON object
//            String jobTitle = job.optString("jobTitle", "N/A");
//            String status = job.optString("status", "N/A");
//            String appliedOn = job.optString("appliedOn", "N/A");
//
//            // Set application details to the corresponding TextViews
//            holder.titleTextView.setText(jobTitle);
//            holder.statusTextView.setText(status);
//            holder.appliedOnTextView.setText(appliedOn);
//
//            // If the list is not editable, hide the accept and reject buttons
//            if (!isEditable) {
//                holder.acceptButton.setVisibility(View.GONE);
//                holder.rejectButton.setVisibility(View.GONE);
//            } else {
//                holder.acceptButton.setOnClickListener(v -> {
//                    int currentPosition = holder.getAdapterPosition();
//                    if (currentPosition != RecyclerView.NO_POSITION) {
//                        try {
//                            JSONObject currentJob = applications.getJSONObject(currentPosition);
//                            showEditBottomSheet(currentJob, currentPosition);  // Show edit dialog
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//                holder.rejectButton.setOnClickListener(v -> {
//                    int currentPosition = holder.getAdapterPosition();
//                    if (currentPosition != RecyclerView.NO_POSITION) {
//                        try {
//                            JSONObject currentJob = applications.getJSONObject(currentPosition);
//                            new AlertDialog.Builder(context)
//                                    .setTitle("Reject Application")
//                                    .setMessage("Are you sure you want to reject this application?")
//                                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//                                        try {
//                                            long jobId = currentJob.getLong("jobPostingId");
//                                            applicationsApi.RejectApplication(context, jobId,
//                                                    response -> {
//                                                        applications.remove(currentPosition);
//                                                        notifyItemRemoved(currentPosition);
//                                                        notifyItemRangeChanged(currentPosition, applications.length());
//                                                        Toast.makeText(context, "Application rejected successfully", Toast.LENGTH_SHORT).show();
//                                                    },
//                                                    error -> Toast.makeText(context, "Error rejecting application: " + error.getMessage(), Toast.LENGTH_SHORT).show()
//                                            );
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                            Toast.makeText(context, "Error rejecting application", Toast.LENGTH_SHORT).show();
//                                        }
//                                    })
//                                    .setNegativeButton(android.R.string.no, null)
//                                    .show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(context, "Error retrieving application data", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(context, "Error parsing application data", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return applications.length();  // Return the number of applications in the list
//    }
//
//    private void showEditBottomSheet(JSONObject job, int position) {
//        try {
//            View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_employer_application, null);
//            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
//            bottomSheetDialog.setContentView(view);
//
//            TextView editJobTitle = view.findViewById(R.id.editJobTitle);
//            TextView editStatus = view.findViewById(R.id.editStatus);
//            TextView editAppliedOn = view.findViewById(R.id.editAppliedOn);
//            ImageButton acceptButton = view.findViewById(R.id.acceptButton);
//            ImageButton rejectButton = view.findViewById(R.id.rejectButton);
//
//            editJobTitle.setText(job.optString("jobTitle"));
//            editStatus.setText(job.optString("status"));
//            editAppliedOn.setText(job.optString("appliedOn"));
//
//            acceptButton.setOnClickListener(v -> {
//                String updatedJobTitle = editJobTitle.getText().toString().trim();
//                String updatedStatus = editStatus.getText().toString().trim();
//                String updatedAppliedOn = editAppliedOn.getText().toString().trim();
//
//                try {
//                    SharedPreferences preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
//                    long employerId = job.optLong("employerId", -1);
//                    if (employerId == -1) {
//                        Toast.makeText(context, "Error: employerId not found", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    long jobPostingId = job.optLong("jobPostingId", -1);
//                    if (jobPostingId == -1) {
//                        Toast.makeText(context, "Error: Job PostingId not found", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    JSONObject updatedJob = new JSONObject();
//                    updatedJob.put("jobPostingId", jobPostingId);
//                    updatedJob.put("jobTitle", updatedJobTitle);
//                    updatedJob.put("status", updatedStatus);
//                    updatedJob.put("appliedOn", updatedAppliedOn);
//                    updatedJob.put("applicationId", employerId);
//
//                    Log.d("applicationsApi", "Update Job Payload: " + updatedJob.toString());
//
//                    applicationsApi.AcceptApplication(context, updatedJob,
//                            response -> {
//                                try {
//                                    applications.put(position, updatedJob);
//                                    notifyItemChanged(position);
//                                    bottomSheetDialog.dismiss();
//                                    Toast.makeText(context, "Job updated successfully!", Toast.LENGTH_SHORT).show();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                    Toast.makeText(context, "Error updating job list", Toast.LENGTH_SHORT).show();
//                                }
//                            },
//                            error -> {
//                                Log.e("EmployerApis", "Error updating job: " + error.toString());
//                                Toast.makeText(context, "Error updating job: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                            });
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(context, "Error creating updated job object", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            bottomSheetDialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(context, "Error displaying bottom sheet", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    static class ApplicationViewHolder extends RecyclerView.ViewHolder {
//        TextView titleTextView;  // Title of the job
//        TextView statusTextView;  // Status of the application
//        TextView appliedOnTextView;  // Date the application was submitted
//        ImageButton acceptButton;  // Button to accept the application
//        ImageButton rejectButton;  // Button to reject the application
//
//        @SuppressLint("WrongViewCast")
//        ApplicationViewHolder(View itemView) {
//            super(itemView);
//            titleTextView = itemView.findViewById(R.id.jobTitleTextView);
//            statusTextView = itemView.findViewById(R.id.statusTextView);
//            appliedOnTextView = itemView.findViewById(R.id.appliedOnTextView);
//            acceptButton = itemView.findViewById(R.id.acceptButton);
//            rejectButton = itemView.findViewById(R.id.rejectButton);
//        }
//    }
//}










//
//
//
//
//
//
//
//
//
//
//
//
//
///**
// * Adapter class for managing and displaying a list of employers in a RecyclerView.
// * Supports both read-only mode and editable mode (with update and delete options).
// */
//public class applicationAdapter extends RecyclerView.Adapter<com.example.hiveeapp.employer_user.applications.applicationAdapter.EmployerViewHolder> {
//
//    private JSONArray applications = new JSONArray();  // Holds the list of employers
//    private final Context context;  // The context in which the adapter is used
//    private final boolean isEditable;  // Indicates whether the list is editable (shows update/delete buttons)
//    private static final String USER_PREFS = "UserPrefs";  // SharedPreferences key for user data
//
//    /**
//     * Constructor for the EmployerAdapter.
//     *
//     * @param context    The context in which the adapter is used
//     * @param isEditable Indicates if the employer list is editable (shows update/delete buttons)
//     */
//    public applicationAdapter(Context context, boolean isEditable) {
//        this.context = context;
//        this.isEditable = isEditable;
//    }
//
//    /**
//     * Updates the employer list and refreshes the RecyclerView.
//     *
//     * @param applications The JSONArray containing the employer data
//     */
//    public void setApplications(JSONArray applications) {
//        this.applications = applications;
//        notifyDataSetChanged();  // Notify the adapter to refresh the data
//    }
//
//
//    @Override
//    public com.example.hiveeapp.employer_user.applications.applicationAdapter.EmployerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        // Inflate the layout for each employer item in the RecyclerView
//        View view = LayoutInflater.from(context).inflate(R.layout.item_employer_applications, parent, false);
//        return new com.example.hiveeapp.employer_user.applications.applicationAdapter.EmployerViewHolder(view);
//    }
//
//    /**
//     * Binds the data for a specific employer to the view holder and handles
//     * the display of employer information such as name, email, phone, and address.
//     * It also sets up functionality for updating and deleting employers, depending
//     * on the editability of the list.
//     *
//     * @param holder   The view holder to bind the employer data to.
//     * @param position The position of the employer in the RecyclerView list.
//     */
//    @Override
//    public void onBindViewHolder(com.example.hiveeapp.employer_user.applications.applicationAdapter.EmployerViewHolder holder, int position) {
//        try {
//            // Get the employer object at the current position
//            JSONObject job = applications.getJSONObject(position);
//
//            // Extract employer details, handling null cases with default values
//
////            String title = job.optString("title", "N/A");
////            String description = job.optString("description", "N/A");
////            String summary = job.optString("summary", "N/A");
////            double salary = job.optDouble("salary", 0.0);
////            String jobType = job.optString("jobType", "N/A");
////            double minimumGpa = job.optDouble("minimumGpa", 0.0);
////            String jobStart = job.optString("jobStart", "N/A");
////            String applicationStart = job.optString("applicationStart", "N/A");
////            String applicationEnd = job.optString("applicationEnd", "N/A");
//
//            // Extract the relevant data from the JSON object
//            String jobTitle = job.optString("jobTitle", "N/A");
//            String status = job.optString("status", "N/A");
//            String appliedOn = job.optString("appliedOn", "N/A");
//
////            // Set employer details to the corresponding TextViews
////            holder.titleTextView.setText(title);
////            holder.descriptionTextView.setText(description);
////            holder.summaryTextView.setText(summary);
////            holder.salaryTextView.setText(String.valueOf(salary));
////            holder.jobTypeTextView.setText(jobType);
////            holder.minimumGpaTextView.setText(String.valueOf(minimumGpa));
////            holder.jobStartTextView.setText(jobStart);
////            holder.applicationStartTextView.setText(applicationStart);
////            holder.applicationEndTextView.setText(applicationEnd);
//
//            // Set employer details to the corresponding TextViews
//            holder.titleTextView.setText(jobTitle);
//            holder.statusTextView.setText(status);
//            holder.appliedOnTextView.setText(appliedOn);
//
//
//
//            // If the list is not editable, hide the update and delete buttons
//            if (!isEditable) {
//                holder.acceptButton.setVisibility(View.GONE);
//                holder.rejectButton.setVisibility(View.GONE);
//            } else {
//                // Setup update button click listener
//                holder.acceptButton.setOnClickListener(v -> {
//                    int currentPosition = holder.getAdapterPosition();
//                    if (currentPosition != RecyclerView.NO_POSITION) {
//                        try {
//                            JSONObject currentEmployer = applications.getJSONObject(currentPosition);
//                            showEditBottomSheet(currentEmployer, currentPosition);  // Show edit dialog
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//                // Setup delete button click listener
//                holder.rejectButton.setOnClickListener(v -> {
//                    int currentPosition = holder.getAdapterPosition();
//                    if (currentPosition != RecyclerView.NO_POSITION) {
//                        try {
//                            JSONObject currentEmployer = applications.getJSONObject(currentPosition);
//                            new AlertDialog.Builder(context)
//                                    .setTitle("Delete job")
//                                    .setMessage("Are you sure you want to delete this job?")
//                                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//                                        try {
//                                            long employerId = currentEmployer.getLong("jobPostingId");
//                                            EmployerApis.deleteEmployer(context, employerId,
//                                                    response -> {
//                                                        // Remove deleted employer and refresh RecyclerView
//                                                        applications.remove(currentPosition);
//                                                        notifyItemRemoved(currentPosition);
//                                                        notifyItemRangeChanged(currentPosition, applications.length());
//                                                        Toast.makeText(context, "job deleted successfully", Toast.LENGTH_SHORT).show();
//                                                    },
//                                                    error -> Toast.makeText(context, "Error deleting job: " + error.getMessage(), Toast.LENGTH_SHORT).show()
//                                            );
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                            Toast.makeText(context, "Error deleting job", Toast.LENGTH_SHORT).show();
//                                        }
//                                    })
//                                    .setNegativeButton(android.R.string.no, null)
//                                    .show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(context, "Error retrieving job data", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(context, "Error parsing job data", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return applications.length();  // Return the number of employers in the list
//    }
//
//    /**
//     * Displays a BottomSheetDialog for editing employer details.
//     *
//     * @param //employer The JSONObject containing the employer details
//     * @param position The position of the employer in the list
//     */
//    private void showEditBottomSheet(JSONObject job, int position) {
//        try {
//            // Inflate the bottom sheet layout
//            View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_employer_application null);
//            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
//            bottomSheetDialog.setContentView(view);
//
//            // Initialize the fields from the layout
////            TextView editTitle = view.findViewById(R.id.editTitle);
////            TextView editDescription = view.findViewById(R.id.editDescription);
////            TextView editSummary = view.findViewById(R.id.editSummary);
////            TextView editSalary = view.findViewById(R.id.editSalary);
////            TextView editJobType = view.findViewById(R.id.editJobType);
////            TextView editMinimumGpa = view.findViewById(R.id.editMinimumGpa);
////            TextView editJobStart = view.findViewById(R.id.editJobStart);
////            TextView editApplicationStart = view.findViewById(R.id.editApplicationStart);
////            TextView editApplicationEnd = view.findViewById(R.id.editApplicationEnd);
////            View saveChangesButton = view.findViewById(R.id.saveChangesButton);
//
//            TextView editJobTitle = view.findViewById(R.id.editJobTitle);
//            TextView  editStatus =  view.findViewById(R.id.editStatus);
//            TextView editAppliedOn  = view.findViewById(R.id.editAppliedOn);
//            TextView acceptButton = view.findViewById(R.id.acceptButton);
//            TextView rejectButton = view.findViewById(R.id.rejectButton);
//
//
//
//
//            // Pre-fill the fields with the current job data
//
////            editTitle.setText(job.optString("title"));
////            editDescription.setText(job.optString("description"));
////            editSummary.setText(job.optString("summary"));
////            editSalary.setText(String.valueOf(job.optDouble("salary")));
////            editJobType.setText(job.optString("jobType"));
////            editMinimumGpa.setText(String.valueOf(job.optDouble("minimumGpa")));
////            editJobStart.setText(job.optString("jobStart"));
////            editApplicationStart.setText(job.optString("applicationStart"));
////            editApplicationEnd.setText(job.optString("applicationEnd"));
//
//            // Pre-fill the fields with the current job data
//            editJobTitle.setText(job.optString("jobTitle"));
//            editStatus.setText(job.optString("status"));
//            editAppliedOn.setText(job.optString("appliedOn"));
//
//
//
//            // Save changes when button is clicked
//           // saveChangesButton.setOnClickListener(v -> {
//
//                acceptButton.setOnClickListener(v -> {
//
//
//                // Get updated values from the fields
//
//                String updatedTitle = editTitle.getText().toString().trim();
//                String updatedDescription = editDescription.getText().toString().trim();
//                String updatedSummary = editSummary.getText().toString().trim();
//                double updatedSalary = Double.parseDouble(editSalary.getText().toString().trim());
//                String updatedJobType = editJobType.getText().toString().trim();
//                double updatedMinimumGpa = Double.parseDouble(editMinimumGpa.getText().toString().trim());
//                String updatedJobStart = editJobStart.getText().toString().trim();
//                String updatedApplicationStart = editApplicationStart.getText().toString().trim();
//                String updatedApplicationEnd = editApplicationEnd.getText().toString().trim();
//
//
//                try {
//                    // Retrieve companyId from SharedPreferences
//                    SharedPreferences preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
//                    // Retrieve companyId directly from the job JSONObject
//                    long employerId = job.optLong("employerId", -1);
//                    Log.d("applicationsApi", "Retrieved employerId: " + employerId);
//
//                    if (employerId == -1) {
//                        Toast.makeText(context, "Error: employerId not found", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    // Ensure jobPostingId is provided
//                    long jobPostingId = job.optLong("jobPostingId", -1);
//                    if (jobPostingId == -1) {
//                        Toast.makeText(context, "Error: Job PostingId not found", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    // Construct a new JSONObject with updated data
//                    JSONObject updatedJob = new JSONObject();
//                    updatedJob.put("jobPostingId", jobPostingId); // Assuming this is the correct ID
//                    updatedJob.put("title", updatedTitle);
//                    updatedJob.put("description", updatedDescription);
//                    updatedJob.put("summary", updatedSummary);
//                    updatedJob.put("salary", updatedSalary);
//                    updatedJob.put("jobType", updatedJobType);
//                    updatedJob.put("minimumGpa", updatedMinimumGpa);
//                    updatedJob.put("jobStart", updatedJobStart);
//                    updatedJob.put("applicationStart", updatedApplicationStart);
//                    updatedJob.put("applicationEnd", updatedApplicationEnd);
//                    updatedJob.put("employerId", employerId); // or updatedJob.put("employerId", companyId);
//
//                    // Print the JSON payload for debugging
//                    Log.d("EmployerApis", "Update Job Payload: " + updatedJob.toString());
//
//                    // Update employer via EmployerApis
//                    applicationsApi.AcceptApplication(context, updatedJob,
//                            response -> {
//                                try {
//                                    // Update the employer in the list and refresh RecyclerView
//                                    applications.put(position, updatedJob);
//                                    notifyItemChanged(position);
//                                    bottomSheetDialog.dismiss();
//                                    Toast.makeText(context, "Job updated successfully!", Toast.LENGTH_SHORT).show();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                    Toast.makeText(context, "Error updating job list", Toast.LENGTH_SHORT).show();
//                                }
//                            },
//                            error -> {
//                                // Log error response for debugging
//                                // Log.e("EmployerApis", "Error updating employer: " + error.toString());
//                                Toast.makeText(context, "Error updating job", Toast.LENGTH_SHORT).show();
//                            });
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(context, "Error constructing update request", Toast.LENGTH_SHORT).show();
//                }
//
//            });
//
//            bottomSheetDialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(context, "Error opening edit form", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * ViewHolder class to hold views for each employer item.
//     */
//    class EmployerViewHolder extends RecyclerView.ViewHolder {
//        //  TextView nameTextView, emailTextView, phoneTextView, addressTextView;
//
//        TextView titleTextView;         // TextView for job title
////        TextView descriptionTextView;   // TextView for job description
////        TextView summaryTextView;       // TextView for job summary
////        TextView salaryTextView;        // TextView for job salary
////        TextView jobTypeTextView;       // TextView for job type
////        TextView minimumGpaTextView;    // TextView for minimum GPA
////        TextView jobStartTextView;      // TextView for job start date
////        TextView applicationStartTextView; // TextView for application start date
////        TextView applicationEndTextView; // TextView for application end date
////        ImageButton updateButton;       // Button for updating job details
////        ImageButton deleteButton;       // Button for deleting job
//
//        //ImageButton updateButton, deleteButton;
//
//        TextView  statusTextView;
//        TextView  appliedOnTextView;
//
//
//        public EmployerViewHolder(View itemView) {
//            super(itemView);
//            // Initialize views from the layout
//
//            titleTextView = itemView.findViewById(R.id.titleTextView);
////            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
////            summaryTextView = itemView.findViewById(R.id.summaryTextView);
////            salaryTextView = itemView.findViewById(R.id.salaryTextView);
////            jobTypeTextView = itemView.findViewById(R.id.jobTypeTextView);
////            minimumGpaTextView = itemView.findViewById(R.id.minimumGpaTextView);
////            jobStartTextView = itemView.findViewById(R.id.jobStartTextView);
////            applicationStartTextView = itemView.findViewById(R.id.applicationStartTextView);
////            applicationEndTextView = itemView.findViewById(R.id.applicationEndTextView);
////            updateButton = itemView.findViewById(R.id.updateButton);
////            deleteButton = itemView.findViewById(R.id.deleteButton);
//
//             statusTextView = itemView.findViewById(R.id.statusTextView);
//            appliedOnTextView = itemView.findViewById(R.id.appliedOnTextView);
//            updateButton = itemView.findViewById(R.id.updateButton);
//            rejectButton = itemView.findViewById(R.id.deleteButton);
//
//        }
//    }
//}
