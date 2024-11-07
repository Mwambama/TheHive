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
import androidx.fragment.app.FragmentActivity;
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

    // Constructor to initialize context and editability flag
    public applicationAdapter(Context context, boolean isEditable) {
        this.context = context;
        this.isEditable = isEditable;
    }

    // Sets the list of applications and refreshes the RecyclerView
    public void setApplications(JSONArray applications) {
        this.applications = applications;
        notifyDataSetChanged();
    }

    // Inflates the item layout and creates a ViewHolder object
    @Override
    public ApplicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_employer_applications, parent, false);
        return new ApplicationViewHolder(view);
    }

    // Binds data to the ViewHolder for each application item
    @Override
    public void onBindViewHolder(ApplicationViewHolder holder, int position) {
        try {
            JSONObject application = applications.getJSONObject(position);

            // Extract job title, status, and applied date from JSON object
            String jobTitle = application.optString("jobTitle", "N/A");
            String status = application.optString("status", "N/A");
            String appliedOn = application.optString("appliedOn", "N/A");

            // Set text views with extracted data
            holder.titleTextView.setText(jobTitle);
            holder.statusTextView.setText(status);
            holder.appliedOnTextView.setText(appliedOn);

            // Set up view button
            holder.viewButton.setOnClickListener(v -> openStudentDetailsPopup(application));

            // Hide or show buttons based on editability
            if (!isEditable) {
                holder.acceptButton.setVisibility(View.GONE);
                holder.rejectButton.setVisibility(View.GONE);
            } else {
                setupAcceptButton(holder, application, position);
                setupRejectButton(holder, application, position);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showToast(ERROR_MESSAGE + ": " + e.getMessage());
        }
    }

    // Opens the student details popup
    private void openStudentDetailsPopup(JSONObject application) {
        // Pass student data to a new Fragment
        studentDetailsFragment detailsFragment = studentDetailsFragment.newInstance(application);
        detailsFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "studentDetails");
    }

    // Sets up the reject button with an onClick listener to reject the application
    private void setupRejectButton(ApplicationViewHolder holder, JSONObject application, int position) {
        holder.rejectButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Reject Application")
                    .setMessage("Are you sure you want to reject this application?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> rejectApplication(application, position))
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        });
    }

    // Sets up the accept button with an onClick listener to accept the application
    private void setupAcceptButton(ApplicationViewHolder holder, JSONObject application, int position) {
        holder.acceptButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Accept Application")
                    .setMessage("Are you sure you want to accept this application?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> acceptApplication(application, position))
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        });
    }

    // Handles accepting an application by sending a request to the backend
    private void acceptApplication(JSONObject application, int position) {
        try {
            long applicationId = application.getLong("applicationId");

            com.example.hiveeapp.employer_user.applications.applicationsApi.AcceptApplication(context, applicationId,
                    response -> {
                        // Remove the application from the list and refresh the adapter
                        applications.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, applications.length());
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

    // Handles rejecting an application by sending a request to the backend
    private void rejectApplication(JSONObject application, int position) {
        try {
            long applicationId = application.getLong("applicationId");

            com.example.hiveeapp.employer_user.applications.applicationsApi.RejectApplication(context, applicationId,
                    response -> {
                        // Remove the application from the list and refresh the adapter
                        applications.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, applications.length());
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

    // Returns the total number of application items
    @Override
    public int getItemCount() {
        return applications.length();
    }

    // Displays a toast message with the provided text
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // ViewHolder class to hold and recycle views as they are scrolled off screen
    static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView statusTextView;
        TextView appliedOnTextView;
        Button acceptButton;
        Button rejectButton;
        Button viewButton;  // Add this line

        // Constructor to initialize the view components
        ApplicationViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.jobTitleTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            appliedOnTextView = itemView.findViewById(R.id.appliedOnTextView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
            viewButton = itemView.findViewById(R.id.viewButton);  // Add this line
        }
    }
}



// this works perfectly, I just need to put the view button
//public class applicationAdapter extends RecyclerView.Adapter<applicationAdapter.ApplicationViewHolder> {
//    private JSONArray applications = new JSONArray();  // Holds the list of applications
//    private final Context context;  // The context in which the adapter is used
//    private final boolean isEditable;  // Indicates whether the list is editable
//    private static final String USER_PREFS = "UserPrefs";  // SharedPreferences key for user data
//    private static final String ERROR_MESSAGE = "Error";  // Generic error message for Toasts
//
//    // Constructor to initialize context and editability flag
//    public applicationAdapter(Context context, boolean isEditable) {
//        this.context = context;
//        this.isEditable = isEditable;
//    }
//
//    // Sets the list of applications and refreshes the RecyclerView
//    public void setApplications(JSONArray applications) {
//        this.applications = applications;
//        notifyDataSetChanged();
//    }
//
//    // Inflates the item layout and creates a ViewHolder object
//    @Override
//    public ApplicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_employer_applications, parent, false);
//        return new ApplicationViewHolder(view);
//    }
//
//    // Binds data to the ViewHolder for each application item
//    @Override
//    public void onBindViewHolder(ApplicationViewHolder holder, int position) {
//        try {
//            JSONObject application = applications.getJSONObject(position);
//
//            // Extract job title, status, and applied date from JSON object
//            String jobTitle = application.optString("jobTitle", "N/A");
//            String status = application.optString("status", "N/A");
//            String appliedOn = application.optString("appliedOn", "N/A");
//
//            // Set text views with extracted data
//            holder.titleTextView.setText(jobTitle);
//            holder.statusTextView.setText(status);
//            holder.appliedOnTextView.setText(appliedOn);
//
//            // Hide or show buttons based on editability
//            if (!isEditable) {
//                holder.acceptButton.setVisibility(View.GONE);
//                holder.rejectButton.setVisibility(View.GONE);
//            } else {
//                setupAcceptButton(holder, application, position);
//                setupRejectButton(holder, application, position);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            showToast(ERROR_MESSAGE + ": " + e.getMessage());
//        }
//    }
//
//    // Sets up the reject button with an onClick listener to reject the application
//    private void setupRejectButton(ApplicationViewHolder holder, JSONObject application, int position) {
//        holder.rejectButton.setOnClickListener(v -> {
//            new AlertDialog.Builder(context)
//                    .setTitle("Reject Application")
//                    .setMessage("Are you sure you want to reject this application?")
//                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//                        rejectApplication(application, position);
//                    })
//                    .setNegativeButton(android.R.string.no, null)
//                    .show();
//        });
//    }
//
//    // Sets up the accept button with an onClick listener to accept the application
//    private void setupAcceptButton(ApplicationViewHolder holder, JSONObject application, int position) {
//        holder.acceptButton.setOnClickListener(v -> {
//            new AlertDialog.Builder(context)
//                    .setTitle("Accept Application")
//                    .setMessage("Are you sure you want to accept this application?")
//                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//                        acceptApplication(application, position);
//                    })
//                    .setNegativeButton(android.R.string.no, null)
//                    .show();
//        });
//    }
//
//    // Handles accepting an application by sending a request to the backend
//    private void acceptApplication(JSONObject application, int position) {
//        try {
//            long applicationId = application.getLong("applicationId");
//
//            com.example.hiveeapp.employer_user.applications.applicationsApi.AcceptApplication(context, applicationId,
//                    response -> {
//                        // Remove the application from the list and refresh the adapter
//                        applications.remove(position);
//                        notifyItemRemoved(position);
//                        notifyItemRangeChanged(position, applications.length());
//                        Toast.makeText(context, "Application accepted successfully!", Toast.LENGTH_SHORT).show();
//                    },
//                    error -> {
//                        // Show error message to the user
//                        Toast.makeText(context, "Error accepting application", Toast.LENGTH_SHORT).show();
//                    }
//            );
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(context, "Error constructing acceptance request", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    // Handles rejecting an application by sending a request to the backend
//    private void rejectApplication(JSONObject application, int position) {
//        try {
//            long applicationId = application.getLong("applicationId");
//
//            com.example.hiveeapp.employer_user.applications.applicationsApi.RejectApplication(context, applicationId,
//                    response -> {
//                        // Remove the application from the list and refresh the adapter
//                        applications.remove(position);
//                        notifyItemRemoved(position);
//                        notifyItemRangeChanged(position, applications.length());
//                        Toast.makeText(context, "Application rejected successfully!", Toast.LENGTH_SHORT).show();
//                    },
//                    error -> {
//                        // Show error message to the user
//                        Toast.makeText(context, "Error rejecting application", Toast.LENGTH_SHORT).show();
//                    }
//            );
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(context, "Error constructing rejection request", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    // Returns the total number of application items
//    @Override
//    public int getItemCount() {
//        return applications.length();
//    }
//
//    // Displays a toast message with the provided text
//    private void showToast(String message) {
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//    }
//
//    // ViewHolder class to hold and recycle views as they are scrolled off screen
//    static class ApplicationViewHolder extends RecyclerView.ViewHolder {
//        TextView titleTextView;
//        TextView statusTextView;
//        TextView appliedOnTextView;
//        Button acceptButton;
//        Button rejectButton;
//
//        // Constructor to initialize the view components
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





// this works

//
//
//public class applicationAdapter extends RecyclerView.Adapter<applicationAdapter.ApplicationViewHolder> {
//    private JSONArray applications = new JSONArray();  // Holds the list of applications
//    private final Context context;  // The context in which the adapter is used
//    private final boolean isEditable;  // Indicates whether the list is editable
//    private static final String USER_PREFS = "UserPrefs";  // SharedPreferences key for user data
//    private static final String ERROR_MESSAGE = "Error";  // Generic error message for Toasts
//
//    // Constructor to initialize context and editability flag
//    public applicationAdapter(Context context, boolean isEditable) {
//        this.context = context;
//        this.isEditable = isEditable;
//    }
//
//    // Sets the list of applications and refreshes the RecyclerView
//    public void setApplications(JSONArray applications) {
//        this.applications = applications;
//        notifyDataSetChanged();
//    }
//
//    // Inflates the item layout and creates a ViewHolder object
//    @Override
//    public ApplicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_employer_applications, parent, false);
//        return new ApplicationViewHolder(view);
//    }
//
//    // Binds data to the ViewHolder for each application item
//    @Override
//    public void onBindViewHolder(ApplicationViewHolder holder, int position) {
//        try {
//            JSONObject application = applications.getJSONObject(position);
//
//            // Extract job title, status, and applied date from JSON object
//            String jobTitle = application.optString("jobTitle", "N/A");
//            String status = application.optString("status", "N/A");
//            String appliedOn = application.optString("appliedOn", "N/A");
//
//            // Set text views with extracted data
//            holder.titleTextView.setText(jobTitle);
//            holder.statusTextView.setText(status);
//            holder.appliedOnTextView.setText(appliedOn);
//
//            // Hide or show buttons based on editability
//            if (!isEditable) {
//                holder.acceptButton.setVisibility(View.GONE);
//                holder.rejectButton.setVisibility(View.GONE);
//            } else {
//                setupAcceptButton(holder, application, position);
//                setupRejectButton(holder, application, position);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            showToast(ERROR_MESSAGE + ": " + e.getMessage());
//        }
//    }
//
//    // Sets up the reject button with an onClick listener to reject the application
//    private void setupRejectButton(ApplicationViewHolder holder, JSONObject application, int position) {
//        holder.rejectButton.setOnClickListener(v -> {
//            new AlertDialog.Builder(context)
//                    .setTitle("Reject Application")
//                    .setMessage("Are you sure you want to reject this application?")
//                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//                        rejectApplication(application, position);
//                    })
//                    .setNegativeButton(android.R.string.no, null)
//                    .show();
//        });
//    }
//
//    // Sets up the accept button with an onClick listener to accept the application
//    private void setupAcceptButton(ApplicationViewHolder holder, JSONObject application, int position) {
//        holder.acceptButton.setOnClickListener(v -> {
//            new AlertDialog.Builder(context)
//                    .setTitle("Accept Application")
//                    .setMessage("Are you sure you want to accept this application?")
//                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//                        acceptApplication(application, position);
//                    })
//                    .setNegativeButton(android.R.string.no, null)
//                    .show();
//        });
//    }
//
//    // Handles accepting an application by sending a request to the backend
//    private void acceptApplication(JSONObject application, int position) {
//        try {
//            long applicationId = application.getLong("applicationId");
//
//            com.example.hiveeapp.employer_user.applications.applicationsApi.AcceptApplication(context, applicationId,
//                    response -> {
//                        // Remove the application from the list and refresh the adapter
//                        applications.remove(position);
//                        notifyItemRemoved(position);
//                        notifyItemRangeChanged(position, applications.length());
//                        Toast.makeText(context, "Application accepted successfully!", Toast.LENGTH_SHORT).show();
//                    },
//                    error -> {
//                        // Show error message to the user
//                        Toast.makeText(context, "Error accepting application", Toast.LENGTH_SHORT).show();
//                    }
//            );
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(context, "Error constructing acceptance request", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    // Handles rejecting an application by sending a request to the backend
//    private void rejectApplication(JSONObject application, int position) {
//        try {
//            long applicationId = application.getLong("applicationId");
//
//            com.example.hiveeapp.employer_user.applications.applicationsApi.RejectApplication(context, applicationId,
//                    response -> {
//                        // Remove the application from the list and refresh the adapter
//                        applications.remove(position);
//                        notifyItemRemoved(position);
//                        notifyItemRangeChanged(position, applications.length());
//                        Toast.makeText(context, "Application rejected successfully!", Toast.LENGTH_SHORT).show();
//                    },
//                    error -> {
//                        // Show error message to the user
//                        Toast.makeText(context, "Error rejecting application", Toast.LENGTH_SHORT).show();
//                    }
//            );
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(context, "Error constructing rejection request", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    // Returns the total number of application items
//    @Override
//    public int getItemCount() {
//        return applications.length();
//    }
//
//    // Displays a toast message with the provided text
//    private void showToast(String message) {
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//    }
//
//    // ViewHolder class to hold and recycle views as they are scrolled off screen
//    static class ApplicationViewHolder extends RecyclerView.ViewHolder {
//        TextView titleTextView;
//        TextView statusTextView;
//        TextView appliedOnTextView;
//        Button acceptButton;
//        Button rejectButton;
//
//        // Constructor to initialize the view components
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
