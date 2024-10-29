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

    @Override
    public JobsAdapter.EmployerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each employer item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_employer, parent, false);
        return new JobsAdapter.EmployerViewHolder(view);
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
    public void onBindViewHolder(JobsAdapter.EmployerViewHolder holder, int position) {
        try {
            // Get the employer object at the current position
            JSONObject employer = employers.getJSONObject(position);

            // Extract employer details, handling null cases with default values
            String name = employer.optString("name", "Unknown");
            String email = employer.optString("email", "N/A");
            String phone = employer.optString("phone", "N/A");

            // Extract and format the address details
            JSONObject addressJson = employer.optJSONObject("address");
            String address = "Unknown address";
            if (addressJson != null) {
                String street = addressJson.optString("street", "N/A");
                String city = addressJson.optString("city", "N/A");
                String state = addressJson.optString("state", "N/A");
                String zipCode = addressJson.optString("zipCode", "N/A");
                address = street + ", " + city + ", " + state + " " + zipCode;
            }

            // Set employer details to the corresponding TextViews
            holder.nameTextView.setText(name);
            holder.emailTextView.setText(email);
            holder.phoneTextView.setText(phone);
            holder.addressTextView.setText(address);

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
                                    .setTitle("Delete Employer")
                                    .setMessage("Are you sure you want to delete this employer?")
                                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                        try {
                                            long employerId = currentEmployer.getLong("userId");
                                            EmployerApi.deleteEmployer(context, employerId,
                                                    response -> {
                                                        // Remove deleted employer and refresh RecyclerView
                                                        employers.remove(currentPosition);
                                                        notifyItemRemoved(currentPosition);
                                                        notifyItemRangeChanged(currentPosition, employers.length());
                                                        Toast.makeText(context, "Employer deleted successfully", Toast.LENGTH_SHORT).show();
                                                    },
                                                    error -> Toast.makeText(context, "Error deleting employer: " + error.getMessage(), Toast.LENGTH_SHORT).show()
                                            );
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(context, "Error deleting employer", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null)
                                    .show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error retrieving employer data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error parsing employer data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return employers.length();  // Return the number of employers in the list
    }

    /**
     * Displays a BottomSheetDialog for editing employer details.
     *
     * @param employer The JSONObject containing the employer details
     * @param position The position of the employer in the list
     */
    private void showEditBottomSheet(JSONObject employer, int position) {
        try {
            // Inflate the bottom sheet layout
            View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_edit_employer, null);
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(view);

            // Initialize the fields from the layout
            TextView editName = view.findViewById(R.id.editName);
            TextView editEmail = view.findViewById(R.id.editEmail);
            TextView editPhone = view.findViewById(R.id.editPhone);
            TextView editStreet = view.findViewById(R.id.editStreet);
            TextView editComplement = view.findViewById(R.id.editComplement);
            TextView editCity = view.findViewById(R.id.editCity);
            TextView editState = view.findViewById(R.id.editState);
            TextView editZipCode = view.findViewById(R.id.editZipCode);
            View saveChangesButton = view.findViewById(R.id.saveChangesButton);

            // Pre-fill the fields with the current employer data
            editName.setText(employer.optString("name"));
            editEmail.setText(employer.optString("email"));
            editPhone.setText(employer.optString("phone"));

            JSONObject address = employer.optJSONObject("address");
            if (address != null) {
                editStreet.setText(address.optString("street"));
                editComplement.setText(address.optString("complement"));
                editCity.setText(address.optString("city"));
                editState.setText(address.optString("state"));
                editZipCode.setText(address.optString("zipCode"));
            }

            // Save changes when button is clicked
            saveChangesButton.setOnClickListener(v -> {
                // Get updated values from the fields
                String updatedName = editName.getText().toString().trim();
                String updatedEmail = editEmail.getText().toString().trim();
                String updatedPhone = editPhone.getText().toString().trim();
                String updatedStreet = editStreet.getText().toString().trim();
                String updatedComplement = editComplement.getText().toString().trim();
                String updatedCity = editCity.getText().toString().trim();
                String updatedState = editState.getText().toString().trim();
                String updatedZip = editZipCode.getText().toString().trim();

                try {
                    // Retrieve companyId from SharedPreferences
                    SharedPreferences preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
                    int companyId = preferences.getInt("companyId", -1);

                    // Construct a new JSONObject with updated data
                    JSONObject updatedEmployer = new JSONObject();
                    updatedEmployer.put("userId", employer.getLong("userId"));
                    updatedEmployer.put("name", updatedName);
                    updatedEmployer.put("email", updatedEmail);
                    updatedEmployer.put("phone", updatedPhone);
                    updatedEmployer.put("role", employer.optString("role", "EMPLOYER"));
                    updatedEmployer.put("companyId", companyId);

                    JSONObject updatedAddress = new JSONObject();
                    updatedAddress.put("street", updatedStreet);
                    updatedAddress.put("complement", updatedComplement);
                    updatedAddress.put("city", updatedCity);
                    updatedAddress.put("state", updatedState);
                    updatedAddress.put("zipCode", updatedZip);

                    updatedEmployer.put("address", updatedAddress);

                    // Send update request to API
                    EmployerApi.updateEmployer(context, updatedEmployer,
                            response -> {
                                try {
                                    // Update local list and refresh RecyclerView
                                    employers.put(position, updatedEmployer);
                                    notifyItemChanged(position);
                                    Toast.makeText(context, "Employer updated successfully", Toast.LENGTH_SHORT).show();
                                    bottomSheetDialog.dismiss();  // Dismiss the dialog
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "Error updating employer", Toast.LENGTH_SHORT).show();
                                }
                            },
                            error -> Toast.makeText(context, "Error updating employer: " + error.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error saving changes", Toast.LENGTH_SHORT).show();
                }
            });

            bottomSheetDialog.show();  // Show the BottomSheetDialog
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error showing edit dialog", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ViewHolder class for representing an employer item in the RecyclerView.
     * Manages the views for displaying employer details and buttons for update/delete operations.
     */
    static class EmployerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;    // TextView for employer name
        TextView emailTextView;   // TextView for employer email
        TextView phoneTextView;   // TextView for employer phone number
        TextView addressTextView; // TextView for employer address
        ImageButton updateButton; // Button for updating employer details
        ImageButton deleteButton; // Button for deleting employer

        EmployerViewHolder(View itemView) {
            super(itemView);
            // Initialize the views
            nameTextView = itemView.findViewById(R.id.nameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
