package com.example.hiveeapp.employer_user.applications;
import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.display.EmployerApis;
import com.example.hiveeapp.volley.VolleySingleton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class applicationAdapter extends RecyclerView.Adapter<applicationAdapter.ApplicationViewHolder> {
    private JSONArray applications = new JSONArray();  // Holds the list of applications
    private final Context context;  // The context in which the adapter is used
    private final boolean isEditable;  // Indicates whether the list is editable
    private static final String ERROR_MESSAGE = "Error";  // Generic error message for Toasts
    private OnItemClickListener listener; // Listener for item clicks

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
    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_employer_applications, parent, false);
        return new ApplicationViewHolder(view);
    }

    // Binds data to the ViewHolder for each application item
    @Override
    public void onBindViewHolder(@NonNull ApplicationViewHolder holder, int position) {
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

            // Set up view button to open student details popup
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

    // Opens the student details popup by passing student data to the student details fragment
    private void openStudentDetailsPopup(JSONObject application) {
        try {
            // Extract the studentId to fetch student details
            long studentId = application.getLong("studentId");
            Log.d(TAG, "Fetching details for student ID: " + studentId);

            // Fetch student details using the studentId
            applicationsApi.getStudentDetails(
                    context,
                    studentId,
                    response -> {
                        Log.d(TAG, "Received Student Details: " + response.toString());

                        // Create the student details fragment
                        studentDetailsFragment detailsFragment = new studentDetailsFragment();

                        // Create a bundle with the student data from the response
                        Bundle args = new Bundle();
                        args.putString("name", response.optString("name", "N/A"));
                        args.putString("email", response.optString("email", "N/A"));
                        args.putString("phone", response.optString("phone", "N/A"));
                        args.putString("university", response.optString("university", "N/A"));
                        args.putString("graduationDate", response.optString("graduationDate", "N/A"));
                        args.putString("gpa", String.valueOf(response.optDouble("gpa", 0.0))); // Ensuring GPA is passed as a string
                        args.putString("resumePath", response.optString("resumePath", "N/A"));
                        detailsFragment.setArguments(args); // Pass arguments to fragment

                        // Show the dialog fragment
                        detailsFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "studentDetails");
                    },
                    error -> {
                        Log.e(TAG, "Error fetching student details", error);
                        Toast.makeText(context, "Error fetching student details", Toast.LENGTH_SHORT).show();
                    }
            );
        } catch (JSONException e) {
            Log.e(TAG, "Error extracting student ID", e);
            Toast.makeText(context, "Error fetching student ID: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    // Fetch student details based on studentId
    private void getStudentDetails(long studentId, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/student/" + studentId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);
        VolleySingleton.getInstance(context).addToRequestQueue(request);
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

            // Assuming applicationApi accepts an application
            com.example.hiveeapp.employer_user.applications.applicationsApi.AcceptApplication(context, applicationId,
                    response -> {
                        // Remove the application from the list and refresh the adapter
                        applications.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, applications.length());
                        Toast.makeText(context, "Application accepted successfully!", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
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

            // Assuming applicationApi rejects an application
            com.example.hiveeapp.employer_user.applications.applicationsApi.RejectApplication(context, applicationId,
                    response -> {
                        // Remove the application from the list and refresh the adapter
                        applications.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, applications.length());
                        Toast.makeText(context, "Application rejected successfully!", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
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

    // Interface for handling item clicks
    public interface OnItemClickListener {
        void onItemClick(JSONObject application);
    }
}
