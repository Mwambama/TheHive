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

public class EmployerAdapter extends RecyclerView.Adapter<EmployerAdapter.EmployerViewHolder> {

    private JSONArray employers = new JSONArray();
    private Context context;
    private boolean isEditable;
    private static final String USER_PREFS = "UserPrefs"; // Shared preferences key

    public EmployerAdapter(Context context, boolean isEditable) {
        this.context = context;
        this.isEditable = isEditable;
    }

    public void setEmployers(JSONArray employers) {
        this.employers = employers;
        notifyDataSetChanged();
    }

    @Override
    public EmployerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_employer, parent, false);
        return new EmployerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployerViewHolder holder, int position) {
        try {
            // Extract employer details
            JSONObject employer = employers.getJSONObject(position);

            // Debugging: Log the employer object
            // Log.d("EmployerAdapter", "Employer JSON: " + employer.toString());

            String name = employer.optString("name", "Unknown");
            String email = employer.optString("email", "N/A");
            String phone = employer.optString("phone", "N/A");

            // Address extraction with null checks
            JSONObject addressJson = employer.optJSONObject("address");
            String address = "Unknown address";
            if (addressJson != null) {
                String street = addressJson.optString("street", "N/A");
                String city = addressJson.optString("city", "N/A");
                String state = addressJson.optString("state", "N/A");
                String zipCode = addressJson.optString("zipCode", "N/A");

                address = street + ", " + city + ", " + state + " " + zipCode;
            }

            // Set the extracted data to the corresponding views
            holder.nameTextView.setText(name);
            holder.emailTextView.setText(email);
            holder.phoneTextView.setText(phone);
            holder.addressTextView.setText(address);

            // Check if edit mode is enabled, otherwise hide buttons
            if (!isEditable) {
                holder.updateButton.setVisibility(View.GONE);
                holder.deleteButton.setVisibility(View.GONE);
            } else {
                // Setup the update button to show a BottomSheetDialog for editing
                holder.updateButton.setOnClickListener(v -> {
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        try {
                            JSONObject currentEmployer = employers.getJSONObject(currentPosition);
                            showEditBottomSheet(currentEmployer, currentPosition);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                // Setup the delete button click event
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
                                            long employerId = currentEmployer.getLong("userId"); // Ensure you're using the correct key
                                            EmployerApi.deleteEmployer(context, employerId,
                                                    response -> {
                                                        Toast.makeText(context, "Employer deleted successfully", Toast.LENGTH_SHORT).show();
                                                        // Remove the deleted employer from the list and refresh the RecyclerView
                                                        employers.remove(currentPosition);
                                                        notifyItemRemoved(currentPosition);
                                                        notifyItemRangeChanged(currentPosition, employers.length());
                                                    },
                                                    error -> {
                                                        Toast.makeText(context, "Error deleting employer", Toast.LENGTH_SHORT).show();
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
        return employers.length();
    }

    // Method to show BottomSheetDialog for editing employer details
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
                    // Retrieve companyId from SharedPreferences or other source
                    SharedPreferences preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
                    int companyId = preferences.getInt("companyId", -1); // Replace with actual key and default value

                    // Construct a new JSON object with updated data
                    JSONObject updatedEmployer = new JSONObject();
                    updatedEmployer.put("userId", employer.getLong("userId")); // Use the correct identifier
                    updatedEmployer.put("name", updatedName);
                    updatedEmployer.put("email", updatedEmail);
                    updatedEmployer.put("phone", updatedPhone);
                    updatedEmployer.put("role", employer.optString("role", "EMPLOYER")); // Retain existing role
                    updatedEmployer.put("companyId", employer.optLong("companyId", companyId)); // Use the companyId
                    updatedEmployer.put("field", JSONObject.NULL);
                    updatedEmployer.put("jobPostings", employer.optJSONArray("jobPostings"));

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
                            error -> {
                                Toast.makeText(context, "Error updating employer", Toast.LENGTH_SHORT).show();
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

    class EmployerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, emailTextView, phoneTextView, addressTextView;
        ImageButton updateButton, deleteButton;

        public EmployerViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}