package com.example.hiveeapp.company_user.handleEmployers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hiveeapp.R;
import org.json.*;

import java.util.ArrayList;
import java.util.List;

public class EmployerListActivity extends AppCompatActivity {

    private RecyclerView employerRecyclerView;
    private EmployerAdapter employerAdapter;
    private ImageButton backArrowIcon;
    private Button addEmployerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_list);

        // Initialize views
        initViews();

        // Set up back navigation
        backArrowIcon.setOnClickListener(v -> finish());

        // Set up Add Employer button
        addEmployerButton.setOnClickListener(v -> {
            Intent intent = new Intent(EmployerListActivity.this, EmployerCreationActivity.class);
            startActivity(intent);
        });

        // Set up RecyclerView
        employerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        employerAdapter = new EmployerAdapter(this);
        employerRecyclerView.setAdapter(employerAdapter);

        // Load employers from the server
        loadEmployers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the employer list when returning to this activity
        loadEmployers();
    }

    /**
     * Initialize views in the activity
     */
    private void initViews() {
        employerRecyclerView = findViewById(R.id.employerRecyclerView);
        backArrowIcon = findViewById(R.id.backArrowIcon);
        addEmployerButton = findViewById(R.id.addEmployerButton);
    }

    /**
     * Load employers from the server using EmployerApi
     */
    private void loadEmployers() {
        EmployerApi.getEmployers(this,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Employer> employerList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject employerJson = response.getJSONObject(i);
                                Employer employer = parseEmployer(employerJson);
                                employerList.add(employer);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        employerAdapter.setEmployers(employerList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EmployerListActivity.this, "Error fetching employers: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Parse a JSONObject into an Employer object
     */
    private Employer parseEmployer(JSONObject employerJson) throws JSONException {
        int id = employerJson.getInt("id");
        String name = employerJson.getString("name");
        String email = employerJson.getString("email");
        String phone = employerJson.getString("phone");

        JSONObject addressJson = employerJson.getJSONObject("address");
        String street = addressJson.getString("street");
        String city = addressJson.getString("city");
        String state = addressJson.getString("state");
        String zipCode = addressJson.getString("zip_code");
        String address = street + ", " + city + ", " + state + " " + zipCode;

        return new Employer(id, name, email, phone, street, city, state, zipCode);
    }

    /**
     * RecyclerView Adapter for Employers
     */
    private class EmployerAdapter extends RecyclerView.Adapter<EmployerAdapter.EmployerViewHolder> {

        private List<Employer> employers = new ArrayList<>();
        private Context context;

        public EmployerAdapter(Context context) {
            this.context = context;
        }

        public void setEmployers(List<Employer> employers) {
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
            Employer employer = employers.get(position);
            holder.bind(employer);
        }

        @Override
        public int getItemCount() {
            return employers.size();
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

            public void bind(Employer employer) {
                nameTextView.setText(employer.getName());
                emailTextView.setText(employer.getEmail());
                phoneTextView.setText(employer.getPhone());

                // Combine the address fields into a single string for display
                String fullAddress = employer.getStreet() + ", " +
                        employer.getCity() + ", " +
                        employer.getState() + " " +
                        employer.getZipCode();

                addressTextView.setText(fullAddress);

                updateButton.setOnClickListener(v -> showUpdateDialog(employer.getId(), employer));
                deleteButton.setOnClickListener(v -> deleteEmployer(employer.getId()));
            }
        }

        // Show a dialog to update an employer
        private void showUpdateDialog(int employerId, Employer employerData) {
            // Use the context from the adapter
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Update Employer");

            // Inflate the dialog view
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_employer, null);

            // Initialize the input fields
            EditText nameField = dialogView.findViewById(R.id.nameField);
            EditText emailField = dialogView.findViewById(R.id.emailField);
            EditText phoneField = dialogView.findViewById(R.id.phoneField);
            EditText streetField = dialogView.findViewById(R.id.streetField);
            EditText cityField = dialogView.findViewById(R.id.cityField);
            EditText stateField = dialogView.findViewById(R.id.stateField);
            EditText zipField = dialogView.findViewById(R.id.zipField);

            // Set the existing values from the Employer object
            nameField.setText(employerData.getName());
            emailField.setText(employerData.getEmail());
            phoneField.setText(employerData.getPhone());
            streetField.setText(employerData.getStreet());
            cityField.setText(employerData.getCity());
            stateField.setText(employerData.getState());
            zipField.setText(employerData.getZipCode());

            // Set the view for the dialog
            builder.setView(dialogView);

            // Handle the positive button click (Update button)
            builder.setPositiveButton("Update", (dialog, which) -> {
                // Retrieve the updated values from the input fields
                String name = nameField.getText().toString().trim();
                String email = emailField.getText().toString().trim();
                String phone = phoneField.getText().toString().trim();
                String street = streetField.getText().toString().trim();
                String city = cityField.getText().toString().trim();
                String state = stateField.getText().toString().trim();
                String zip = zipField.getText().toString().trim();

                // Validate input fields
                if (name.isEmpty() || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(context, "Please enter valid employer details.", Toast.LENGTH_SHORT).show();
                } else {
                    // Call the API to update the employer details, passing the context
                    EmployerApi.updateEmployer(context, employerId, name, email, phone, street, city, state, zip,
                            response -> {
                                Toast.makeText(context, "Employer updated successfully!", Toast.LENGTH_SHORT).show();
                                // Refresh the employer list after the update
                                loadEmployers();
                            },
                            error -> Toast.makeText(context, "Error updating employer: " + error.getMessage(), Toast.LENGTH_SHORT).show());
                }
            });

            // Handle the negative button click (Cancel button)
            builder.setNegativeButton("Cancel", null);

            // Show the dialog
            builder.show();
        }

        // Delete an employer from the server
        private void deleteEmployer(int employerId) {
            EmployerApi.deleteEmployer(context, employerId,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(context, "Employer deleted successfully!", Toast.LENGTH_SHORT).show();
                            loadEmployers();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Error deleting employer: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
}
