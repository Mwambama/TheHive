package com.example.hiveeapp.company_user.handleEmployers;

import android.content.Context;
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
            String name = employer.optString("name", "Unknown");
            String email = employer.optString("email", "N/A");
            String phone = employer.optString("phone", "N/A");

            // Address extraction with null checks
            JSONObject addressJson = employer.optJSONObject("address");
            String address = "Unknown address";
            if (addressJson != null) {
                address = addressJson.optString("street", "N/A") + ", " +
                        addressJson.optString("city", "N/A") + ", " +
                        addressJson.optString("state", "N/A") + " " +
                        addressJson.optString("zip_code", "N/A");
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
                holder.updateButton.setOnClickListener(v -> showEditBottomSheet(employer, position));

                // Setup the delete button click event
                holder.deleteButton.setOnClickListener(v -> {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete Employer")
                            .setMessage("Are you sure you want to delete this employer?")
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                try {
                                    int employerId = employer.getInt("id");
                                    EmployerApi.deleteEmployer(context, employerId, response -> {
                                        Toast.makeText(context, "Employer deleted successfully", Toast.LENGTH_SHORT).show();
                                        // Remove the deleted employer from the list and refresh the RecyclerView
                                        employers.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, employers.length());
                                    }, error -> {
                                        Toast.makeText(context, "Error deleting employer", Toast.LENGTH_SHORT).show();
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "Error deleting employer", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
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
                editCity.setText(address.optString("city"));
                editState.setText(address.optString("state"));
                editZipCode.setText(address.optString("zip_code"));
            }

            // Save changes logic
            saveChangesButton.setOnClickListener(v -> {
                // Get updated values from fields
                String updatedName = editName.getText().toString().trim();
                String updatedEmail = editEmail.getText().toString().trim();
                String updatedPhone = editPhone.getText().toString().trim();
                String updatedStreet = editStreet.getText().toString().trim();
                String updatedCity = editCity.getText().toString().trim();
                String updatedState = editState.getText().toString().trim();
                String updatedZip = editZipCode.getText().toString().trim();

                // Update employer via EmployerApi
                int employerId = employer.optInt("id");
                EmployerApi.updateEmployer(context, employerId, updatedName, updatedEmail, updatedPhone, updatedStreet, updatedCity, updatedState, updatedZip,
                        response -> {
                            Toast.makeText(context, "Employer updated successfully!", Toast.LENGTH_SHORT).show();

                            // Update the employer in the local list and refresh the RecyclerView
                            try {
                                employer.put("name", updatedName);
                                employer.put("email", updatedEmail);
                                employer.put("phone", updatedPhone);

                                JSONObject updatedAddress = new JSONObject();
                                updatedAddress.put("street", updatedStreet);
                                updatedAddress.put("city", updatedCity);
                                updatedAddress.put("state", updatedState);
                                updatedAddress.put("zip_code", updatedZip);
                                employer.put("address", updatedAddress);

                                employers.put(position, employer);
                                notifyItemChanged(position);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            bottomSheetDialog.dismiss();
                        },
                        error -> {
                            Toast.makeText(context, "Error updating employer", Toast.LENGTH_SHORT).show();
                        });
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