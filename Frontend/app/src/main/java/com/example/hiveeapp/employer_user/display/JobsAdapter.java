package com.example.hiveeapp.employer_user.display;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiveeapp.R;
import com.example.hiveeapp.company_user.handleEmployers.EmployerApi;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Adapter class for managing and displaying a list of employers in a RecyclerView.
 * Supports both read-only mode and editable mode (with update and delete options).
 */
public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.EmployerViewHolder> {

    private JSONArray employers = new JSONArray();  // Holds the list of employers
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
     * @param employers The JSONArray containing the employer data
     */
    public void setEmployers(JSONArray employers) {
        this.employers = employers;
        notifyDataSetChanged();  // Notify the adapter to refresh the data
    }

    // this is what neends to be changed, it had troubls, i wasnt passing anything

    @Override
    public EmployerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each employer item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_jobs, parent, false);
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
            JSONObject job = employers.getJSONObject(position);

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




//            String name = employer.optString("name", "Unknown");
//            String email = employer.optString("email", "N/A");
//            String phone = employer.optString("phone", "N/A");
//
//            // Extract and format the address details
//            JSONObject addressJson = employer.optJSONObject("address");
//            String address = "Unknown address";
//            if (addressJson != null) {
//                String street = addressJson.optString("street", "N/A");
//                String city = addressJson.optString("city", "N/A");
//                String state = addressJson.optString("state", "N/A");
//                String zipCode = addressJson.optString("zipCode", "N/A");
//                address = street + ", " + city + ", " + state + " " + zipCode;
//            }

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


//            holder.nameTextView.setText(name);
//            holder.emailTextView.setText(email);
//            holder.phoneTextView.setText(phone);
//            holder.addressTextView.setText(address);

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
                            JSONObject currentEmployer = employers.getJSONObject(currentPosition);
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
                            JSONObject currentEmployer = employers.getJSONObject(currentPosition);
                            new AlertDialog.Builder(context)
                                    .setTitle("Delete job")
                                    .setMessage("Are you sure you want to delete this job?")
                                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                        try {
                                            long employerId = currentEmployer.getLong("userId");
                                            EmployerApi.deleteEmployer(context, employerId,
                                                    response -> {
                                                        // Remove deleted employer and refresh RecyclerView
                                                        employers.remove(currentPosition);
                                                        notifyItemRemoved(currentPosition);
                                                        notifyItemRangeChanged(currentPosition, employers.length());
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
        return employers.length();  // Return the number of employers in the list
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




//            TextView editName = view.findViewById(R.id.editName);
//            TextView editEmail = view.findViewById(R.id.editEmail);
//            TextView editPhone = view.findViewById(R.id.editPhone);
//            TextView editStreet = view.findViewById(R.id.editStreet);
//            TextView editComplement = view.findViewById(R.id.editComplement);
//            TextView editCity = view.findViewById(R.id.editCity);
//            TextView editState = view.findViewById(R.id.editState);
//            TextView editZipCode = view.findViewById(R.id.editZipCode);
//            View saveChangesButton = view.findViewById(R.id.saveChangesButton);



            // Pre-fill the fields with the current employer data

            editTitle.setText(job.optString("title"));
            editDescription.setText(job.optString("description"));
            editSummary.setText(job.optString("summary"));
            editSalary.setText(String.valueOf(job.optDouble("salary")));
            editJobType.setText(job.optString("jobType"));
            editMinimumGpa.setText(String.valueOf(job.optDouble("minimumGpa")));
            editJobStart.setText(job.optString("jobStart"));
            editApplicationStart.setText(job.optString("applicationStart"));
            editApplicationEnd.setText(job.optString("applicationEnd"));







//            editName.setText(employer.optString("name"));
//            editEmail.setText(employer.optString("email"));
//            editPhone.setText(employer.optString("phone"));
//
//            JSONObject address = employer.optJSONObject("address");
//            if (address != null) {
//                editStreet.setText(address.optString("street"));
//                editComplement.setText(address.optString("complement"));
//                editCity.setText(address.optString("city"));
//                editState.setText(address.optString("state"));
//                editZipCode.setText(address.optString("zipCode"));
//            }

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




//                String updatedName = editName.getText().toString().trim();
//                String updatedEmail = editEmail.getText().toString().trim();
//                String updatedPhone = editPhone.getText().toString().trim();
//                String updatedStreet = editStreet.getText().toString().trim();
//                String updatedComplement = editComplement.getText().toString().trim();
//                String updatedCity = editCity.getText().toString().trim();
//                String updatedState = editState.getText().toString().trim();
//                String updatedZip = editZipCode.getText().toString().trim();


                try {
                    // Retrieve companyId from SharedPreferences
                    SharedPreferences preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
                    int companyId = preferences.getInt("companyId", -1);

                    // Construct a new JSONObject with updated data
                    JSONObject updatedJob = new JSONObject();

                    updatedJob.put("userId", job.getLong("userId")); // Assuming userId corresponds to the employer
                    updatedJob.put("title", updatedTitle);
                    updatedJob.put("description", updatedDescription);
                    updatedJob.put("summary", updatedSummary);
                    updatedJob.put("salary", updatedSalary);
                    updatedJob.put("jobType", updatedJobType);
                    updatedJob.put("minimumGpa", updatedMinimumGpa);
                    updatedJob.put("jobStart", updatedJobStart);
                    updatedJob.put("applicationStart", updatedApplicationStart);
                    updatedJob.put("applicationEnd", updatedApplicationEnd);
                    updatedJob.put("companyId", companyId);




//                    updatedEmployer.put("userId", employer.getLong("userId"));
//                    updatedEmployer.put("name", updatedName);
//                    updatedEmployer.put("email", updatedEmail);
//                    updatedEmployer.put("phone", updatedPhone);
//                    updatedEmployer.put("role", employer.optString("role", "EMPLOYER"));
//                    updatedEmployer.put("companyId", employer.optLong("companyId", companyId));
//
//                    // Update address in the employer object
//                    JSONObject updatedAddress = new JSONObject();
//                    JSONObject existingAddress = employer.optJSONObject("address");
//                    if (existingAddress != null) {
//                        updatedAddress.put("addressId", existingAddress.optLong("addressId", 0));
//                    } else {
//                        updatedAddress.put("addressId", JSONObject.NULL);
//                    }
//                    updatedAddress.put("street", updatedStreet);
//                    updatedAddress.put("complement", updatedComplement.isEmpty() ? JSONObject.NULL : updatedComplement);
//                    updatedAddress.put("city", updatedCity);
//                    updatedAddress.put("state", updatedState);
//                    updatedAddress.put("zipCode", updatedZip);
//                    updatedEmployer.put("address", updatedAddress);

                    // Update employer via EmployerApi
                    EmployerApi.updateEmployer(context, updatedJob,
                            response -> {
                                try {
                                    // Update the employer in the list and refresh RecyclerView
                                    employers.put(position, updatedJob);
                                    notifyItemChanged(position);
                                    bottomSheetDialog.dismiss();
                                    Toast.makeText(context, "job updated successfully!", Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "Error updating job list", Toast.LENGTH_SHORT).show();
                                }
                            },
                            error -> Toast.makeText(context, "Error updating job", Toast.LENGTH_SHORT).show());
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

        //ImageButton updateButton, deleteButton;

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



//            nameTextView = itemView.findViewById(R.id.nameTextView);
//            emailTextView = itemView.findViewById(R.id.emailTextView);
//            phoneTextView = itemView.findViewById(R.id.phoneTextView);
//            addressTextView = itemView.findViewById(R.id.addressTextView);
//            updateButton = itemView.findViewById(R.id.updateButton);
//            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}