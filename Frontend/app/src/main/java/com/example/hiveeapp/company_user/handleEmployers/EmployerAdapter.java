package com.example.hiveeapp.company_user.handleEmployers;

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
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Adapter class for managing the display of a list of employers in a RecyclerView.
 * Handles both read-only and editable modes, allowing users to update or delete employers.
 */
public class EmployerAdapter extends RecyclerView.Adapter<EmployerAdapter.EmployerViewHolder> {

    private JSONArray employers = new JSONArray(); // Holds the list of employers
    private final Context context; // The context of the activity/fragment
    private final boolean isEditable; // Flag to check if the list is editable
    private static final String USER_PREFS = "UserPrefs"; // Shared preferences key for user data

    /**
     * Constructor for EmployerAdapter.
     *
     * @param context    the context in which the adapter is used
     * @param isEditable indicates if the list is editable (shows update/delete buttons)
     */
    public EmployerAdapter(Context context, boolean isEditable) {
        this.context = context;
        this.isEditable = isEditable;
    }

    /**
     * Updates the employer list and refreshes the RecyclerView.
     *
     * @param employers the JSONArray of employer data
     */
    public void setEmployers(JSONArray employers) {
        this.employers = employers;
        notifyDataSetChanged(); // Notify the adapter to refresh the RecyclerView
    }

    @Override
    public EmployerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each employer item
        View view = LayoutInflater.from(context).inflate(R.layout.item_employer, parent, false);
        return new EmployerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployerViewHolder holder, int position) {
        try {
            // Get the employer object at the current position
            JSONObject employer = employers.getJSONObject(position);

            // Extract employer details and handle null cases with default values
            String name = employer.optString("name", "Unknown");
            String email = employer.optString("email", "N/A");
            String phone = employer.optString("phone", "N/A");

            // Extract address details, handle missing address gracefully
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
                // Handle update button click to show a bottom sheet for editing employer
                holder.updateButton.setOnClickListener(v -> {
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        try {
                            JSONObject currentEmployer = employers.getJSONObject(currentPosition);
                            showEditBottomSheet(currentEmployer, currentPosition); // Show edit dialog
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                // Handle delete button click to delete the employer
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
                                                        // Remove deleted employer from list and update RecyclerView
                                                        employers.remove(currentPosition);
                                                        notifyItemRemoved(currentPosition);
                                                        notifyItemRangeChanged(currentPosition, employers.length());
                                                        Toast.makeText(context, "Employer deleted successfully", Toast.LENGTH_SHORT).show();
                                                    },
                                                    error -> {
                                                        String errorMsg = error.getMessage();
                                                        Toast.makeText(context, "Error deleting employer: " + errorMsg, Toast.LENGTH_SHORT).show();
                                                    }
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
        return employers.length(); // Return the number of employers in the list
    }

    /**
     * Displays a BottomSheetDialog for editing the employer details.
     *
     * @param employer the JSONObject containing employer details
     * @param position the position of the employer in the list
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

            // Pre-fill the fields with current employer data
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

            // Save changes logic
            saveChangesButton.setOnClickListener(v -> {
                // Get updated values from fields
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

                    // Construct a new JSON object with updated data
                    JSONObject updatedEmployer = new JSONObject();
                    updatedEmployer.put("userId", employer.getLong("userId")); // Use the correct identifier
                    updatedEmployer.put("name", updatedName);
                    updatedEmployer.put("email", updatedEmail);
                    updatedEmployer.put("phone", updatedPhone);
                    updatedEmployer.put("role", employer.optString("role", "EMPLOYER")); // Retain existing role
                    updatedEmployer.put("companyId", employer.optLong("companyId", companyId));

                    // Update address in the employer object
                    JSONObject updatedAddress = new JSONObject();
                    JSONObject existingAddress = employer.optJSONObject("address");
                    if (existingAddress != null) {
                        updatedAddress.put("addressId", existingAddress.optLong("addressId", 0));
                    } else {
                        updatedAddress.put("addressId", JSONObject.NULL);
                    }
                    updatedAddress.put("street", updatedStreet);
                    updatedAddress.put("complement", updatedComplement.isEmpty() ? JSONObject.NULL : updatedComplement);
                    updatedAddress.put("city", updatedCity);
                    updatedAddress.put("state", updatedState);
                    updatedAddress.put("zipCode", updatedZip);
                    updatedEmployer.put("address", updatedAddress);

                    // Update employer via EmployerApi
                    EmployerApi.updateEmployer(context, updatedEmployer,
                            response -> {
                                try {
                                    // Update the employer in the local list and refresh the RecyclerView
                                    employers.put(position, updatedEmployer);
                                    notifyItemChanged(position);
                                    bottomSheetDialog.dismiss();
                                    Toast.makeText(context, "Employer updated successfully!", Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "Error updating employer list", Toast.LENGTH_SHORT).show();
                                }
                            },
                            error -> Toast.makeText(context, "Error updating employer", Toast.LENGTH_SHORT).show());
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
        TextView nameTextView, emailTextView, phoneTextView, addressTextView;
        ImageButton updateButton, deleteButton;

        public EmployerViewHolder(View itemView) {
            super(itemView);
            // Initialize views from the layout
            nameTextView = itemView.findViewById(R.id.nameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}