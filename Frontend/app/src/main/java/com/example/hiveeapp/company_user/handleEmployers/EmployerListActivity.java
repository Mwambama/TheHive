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
        employerRecyclerView = findViewById(R.id.employerRecyclerView);
        backArrowIcon = findViewById(R.id.backArrowIcon);
        addEmployerButton = findViewById(R.id.addEmployerButton);

        // Set up back navigation
        backArrowIcon.setOnClickListener(v -> finish());

        // Set up RecyclerView
        employerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        employerAdapter = new EmployerAdapter(this);
        employerRecyclerView.setAdapter(employerAdapter);

        // Load employers from the server
        loadEmployers();

        // Set up Add Employer button
        addEmployerButton.setOnClickListener(v -> {
            Intent intent = new Intent(EmployerListActivity.this, AddEmployerActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the employer list when returning to this activity
        loadEmployers();
    }

    // Load employers from the server using EmployerApi
    private void loadEmployers() {
        EmployerApi.getEmployers(this,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        employerAdapter.setEmployers(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EmployerListActivity.this, "Error fetching employers: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    // Adapter class for RecyclerView
    private class EmployerAdapter extends RecyclerView.Adapter<EmployerAdapter.EmployerViewHolder> {

        private JSONArray employers = new JSONArray();
        private Context context;

        public EmployerAdapter(Context context) {
            this.context = context;
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
                JSONObject employer = employers.getJSONObject(position);
                holder.bind(employer);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return employers.length();
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

            public void bind(JSONObject employer) {
                try {
                    int employerId = employer.getInt("id");
                    String name = employer.getString("name");
                    String email = employer.getString("email");
                    String phone = employer.getString("phone");

                    JSONObject address = employer.getJSONObject("address");
                    String street = address.getString("street");
                    String city = address.getString("city");
                    String state = address.getString("state");
                    String zipCode = address.getString("zip_code");

                    String fullAddress = street + ", " + city + ", " + state + " " + zipCode;

                    nameTextView.setText(name);
                    emailTextView.setText(email);
                    phoneTextView.setText(phone);
                    addressTextView.setText(fullAddress);

                    updateButton.setOnClickListener(v -> showUpdateDialog(employerId, employer));
                    deleteButton.setOnClickListener(v -> deleteEmployer(employerId));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Show a dialog to update an employer
    private void showUpdateDialog(int employerId, JSONObject employerData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Employer");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_employer, null);
        EditText nameField = dialogView.findViewById(R.id.nameField);
        EditText emailField = dialogView.findViewById(R.id.emailField);
        EditText phoneField = dialogView.findViewById(R.id.phoneField);
        EditText streetField = dialogView.findViewById(R.id.streetField);
        EditText cityField = dialogView.findViewById(R.id.cityField);
        EditText stateField = dialogView.findViewById(R.id.stateField);
        EditText zipField = dialogView.findViewById(R.id.zipField);

        try {
            nameField.setText(employerData.getString("name"));
            emailField.setText(employerData.getString("email"));
            phoneField.setText(employerData.getString("phone"));

            JSONObject address = employerData.getJSONObject("address");
            streetField.setText(address.getString("street"));
            cityField.setText(address.getString("city"));
            stateField.setText(address.getString("state"));
            zipField.setText(address.getString("zip_code"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        builder.setView(dialogView);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String name = nameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String phone = phoneField.getText().toString().trim();
            String street = streetField.getText().toString().trim();
            String city = cityField.getText().toString().trim();
            String state = stateField.getText().toString().trim();
            String zip = zipField.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter valid employer details.", Toast.LENGTH_SHORT).show();
            } else {
                EmployerApi.updateEmployer(this, employerId, name, email, phone, street, city, state, zip,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(EmployerListActivity.this, "Employer updated successfully!", Toast.LENGTH_SHORT).show();
                                loadEmployers();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(EmployerListActivity.this, "Error updating employer: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // Delete an employer from the server
    private void deleteEmployer(int employerId) {
        EmployerApi.deleteEmployer(this, employerId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(EmployerListActivity.this, "Employer deleted successfully!", Toast.LENGTH_SHORT).show();
                        loadEmployers();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EmployerListActivity.this, "Error deleting employer: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}