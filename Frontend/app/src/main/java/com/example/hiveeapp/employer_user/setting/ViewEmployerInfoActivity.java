package com.example.hiveeapp.employer_user.setting;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.StudentMainActivity;
import com.example.hiveeapp.student_user.profile.StudentProfileActivity;
import com.example.hiveeapp.student_user.setting.StudentApi;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewEmployerInfoActivity extends AppCompatActivity {

    private static final String TAG = "StudentProfileView";
    private int userId;
    private TextView nameTextView, emailTextView, phoneTextView,fieldTextView;
    private TextView streetTextView, complementTextView, cityTextView, stateTextView, zipCodeTextView;
    private Button updateInfoButton;
    private ImageButton backArrowIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_info);

        // Retrieve userId from Intent
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Debug: Log userId received from Intent
        Log.d(TAG, "Received userId from Intent: " + userId);

        if (userId == -1) {
            Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Invalid userId. Cannot load profile.");
            return;
        }

        // Initialize TextViews and Button with the corresponding views in XML
//        nameTextView = findViewById(R.id.profileNameView);
//        emailTextView = findViewById(R.id.profileEmailView);
//        phoneTextView = findViewById(R.id.profilePhoneView);
//        streetTextView = findViewById(R.id.profileStreet);
//        complementTextView = findViewById(R.id.profileComplement);
//        cityTextView = findViewById(R.id.profileCity);
//        stateTextView = findViewById(R.id.profileState);
//        zipCodeTextView = findViewById(R.id.profileZipCode);
        // Initialize TextViews and Button with the corresponding views in XML

        nameTextView = findViewById(R.id.profileName);
        emailTextView = findViewById(R.id.profileEmail);
        phoneTextView = findViewById(R.id.profilePhone);
        streetTextView = findViewById(R.id.profileStreet);
        complementTextView = findViewById(R.id.profileComplement);
        cityTextView = findViewById(R.id.profileCity);
        stateTextView = findViewById(R.id.profileState);
        zipCodeTextView = findViewById(R.id.profileZipCode);
        fieldTextView =  findViewById(R.id.profileField);
        updateInfoButton = findViewById(R.id.updateProfileButton);

        // Set up the back button
        backArrowIcon = findViewById(R.id.backArrowIcon);
        backArrowIcon.setOnClickListener(v -> finish());

        // Set up the update button to navigate to the update activity
        updateInfoButton.setOnClickListener(v -> {
            Log.d(TAG, "Update button clicked, navigating to ViewEmployerInfoActivity");
            Intent intent = new Intent(com.example.hiveeapp.employer_user.setting.ViewEmployerInfoActivity.this, StudentProfileActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
        // Load student information from the backend
        loadEmployerProfile(userId);

    }

    private void loadEmployerProfile(int userId) {
        Log.d(TAG, "Loading student profile with userId: " + userId);

        StudentApi.getStudent(this, userId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject student) {
                try {
                    displayProfile(student);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(com.example.hiveeapp.employer_user.setting.ViewEmployerInfoActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(com.example.hiveeapp.employer_user.setting.ViewEmployerInfoActivity.this, "Error fetching profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProfile(JSONObject student) throws JSONException {
        Log.d(TAG, "Displaying profile data for student: " + student.optString("name"));

        // Set basic profile fields
        nameTextView.setText(student.optString("name"));
        emailTextView.setText(student.optString("email"));
        phoneTextView.setText(student.optString("phone"));


        // Set address fields
        JSONObject address = student.optJSONObject("address");
        if (address != null) {
            streetTextView.setText(address.optString("street"));
            complementTextView.setText(address.optString("complement"));
            fieldTextView.setText(student.optString("field"));
            cityTextView.setText(address.optString("city"));
            stateTextView.setText(address.optString("state"));
            zipCodeTextView.setText(address.optString("zipCode"));
        } else {
            // Clear fields if no address data is available
            streetTextView.setText("");
            complementTextView.setText("");
            cityTextView.setText("");
            stateTextView.setText("");
            zipCodeTextView.setText("");
        }
    }
}













//
//
//import android.os.Bundle;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.example.hiveeapp.R;
//import com.example.hiveeapp.employer_user.setting.employerinfoApi;
//import com.google.android.material.button.MaterialButton;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class ViewEmployerInfoActivity extends AppCompatActivity {
//
//    private EditText nameEditText, emailEditText, phoneEditText, streetEditText, complementEditText, cityEditText, stateEditText, zipCodeEditText, fieldEditText;
//    private MaterialButton updateButton;
//    private MaterialButton saveButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_employer_info);
//
//        // Initialize views
//        nameEditText = findViewById(R.id.profileName);
//        emailEditText = findViewById(R.id.profileEmail);
//        phoneEditText = findViewById(R.id.profilePhone);
//        streetEditText = findViewById(R.id.profileStreet);
//        complementEditText = findViewById(R.id.profileComplement);
//        cityEditText = findViewById(R.id.profileCity);
//        stateEditText = findViewById(R.id.profileState);
//        zipCodeEditText = findViewById(R.id.profileZipCode);
//        fieldEditText = findViewById(R.id.profileField);
//        updateButton = findViewById(R.id.updateProfileButton);
//
//        // Load employer information
//        loadEmployerProfile();
//
//        // Handle Update button click
//        updateButton.setOnClickListener(v -> updateEmployerProfile());
//    }
//
//    /**
//     * Loads the employer profile data from the backend.
//     */
//    private void loadEmployerProfile() {
//        // Call the EmployerInfoApi to get employer info
//        employerinfoApi.getStudent(this, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                try {
//                    // Assuming the first element is the employer
//                    JSONObject employer = response.getJSONObject(0);
//                    populateProfileFields(employer);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(ViewEmployerInfoActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ViewEmployerInfoActivity.this, "Error fetching profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    /**
//     * Populates the UI fields with the employer data.
//     */
//    private void populateProfileFields(JSONObject employer) throws JSONException {
//        nameEditText.setText(employer.optString("name"));
//        emailEditText.setText(employer.optString("email"));
//        phoneEditText.setText(employer.optString("phone"));
//
//        JSONObject address = employer.optJSONObject("address");
//        if (address != null) {
//            streetEditText.setText(address.optString("street"));
//            complementEditText.setText(address.optString("complement"));
//            cityEditText.setText(address.optString("city"));
//            stateEditText.setText(address.optString("state"));
//            zipCodeEditText.setText(address.optString("zipCode"));
//        }
//
//        fieldEditText.setText(employer.optString("field"));
//    }
//
//    /**
//     * Sends the updated profile information to the backend.
//     */
//    private void updateEmployerProfile() {
//        try {
//            // Create a JSONObject with the updated values
//            JSONObject updatedEmployer = new JSONObject();
//            updatedEmployer.put("name", nameEditText.getText().toString());
//            updatedEmployer.put("email", emailEditText.getText().toString());
//            updatedEmployer.put("phone", phoneEditText.getText().toString());
//
//            JSONObject updatedAddress = new JSONObject();
//            updatedAddress.put("street", streetEditText.getText().toString());
//            updatedAddress.put("complement", complementEditText.getText().toString());
//            updatedAddress.put("city", cityEditText.getText().toString());
//            updatedAddress.put("state", stateEditText.getText().toString());
//            updatedAddress.put("zipCode", zipCodeEditText.getText().toString());
//            updatedEmployer.put("address", updatedAddress);
//
//            updatedEmployer.put("field", fieldEditText.getText().toString());
//
//            // Call the API to update the employer
//            employerinfoApi.updateStudent(this, updatedEmployer, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    Toast.makeText(ViewEmployerInfoActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(ViewEmployerInfoActivity.this, "Error updating profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(ViewEmployerInfoActivity.this, "Error creating update request", Toast.LENGTH_SHORT).show();
//        }
//    }
//}
//














//
//import android.os.Bundle;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.example.hiveeapp.R;
//import com.example.hiveeapp.student_user.setting.StudentApi;
//import com.google.android.material.button.MaterialButton;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class ViewEmployerInfoActivity extends AppCompatActivity {
//
//    private EditText nameEditText, emailEditText, phoneEditText, universityEditText, gpaEditText;
//    private MaterialButton saveButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student_profile);
//
//        // Initialize views
//        nameEditText = findViewById(R.id.profileName);
//        emailEditText = findViewById(R.id.profileEmail);
//        phoneEditText = findViewById(R.id.profilePhone);
//        universityEditText = findViewById(R.id.profileUniversity);
//        gpaEditText = findViewById(R.id.profileGPA);
//        saveButton = findViewById(R.id.saveProfileButton);
//
//        // Load student information
//        loadStudentProfile();
//
//        // Handle Save button click
//        saveButton.setOnClickListener(v -> updateStudentProfile());
//    }
//
//    /**
//     * Loads the student profile data from the backend.
//     */
//    private void loadStudentProfile() {
//        // Call the StudentApi to get student info
//        employerinfoApi.getStudents(this, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                try {
//                    // Assuming the first element is the student
//                    JSONObject employer = response.getJSONObject(0);
//                    populateProfileFields(employer);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(ViewEmployerInfoActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ViewEmployerInfoActivity.this, "Error fetching profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    /**
//     * Populates the UI fields with the student data.
//     */
//    private void populateProfileFields(JSONObject student) throws JSONException {
//        nameEditText.setText(student.optString("name"));
//        emailEditText.setText(student.optString("email"));
//        phoneEditText.setText(student.optString("phone"));
//        universityEditText.setText(student.optString("university"));
//        gpaEditText.setText(String.valueOf(student.optDouble("gpa")));
//    }
//
//    /**
//     * Sends the updated profile information to the backend.
//     */
//    private void updateStudentProfile() {
//        try {
//            // Create a JSONObject with the updated values
//            JSONObject updatedStudent = new JSONObject();
//            updatedStudent.put("name", nameEditText.getText().toString());
//            updatedStudent.put("email", emailEditText.getText().toString());
//            updatedStudent.put("phone", phoneEditText.getText().toString());
//            updatedStudent.put("university", universityEditText.getText().toString());
//            updatedStudent.put("gpa", Double.parseDouble(gpaEditText.getText().toString()));
//
//            // Call the API to update the student
//            StudentApi.updateStudent(this, updatedStudent, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    Toast.makeText(ViewEmployerInfoActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(ViewEmployerInfoActivity.this, "Error updating profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(ViewEmployerInfoActivity.this, "Error creating update request", Toast.LENGTH_SHORT).show();
//        }
//    }
//}
















//
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.util.Log;
//import android.util.Patterns;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.StringRequest;
//import com.example.hiveeapp.R;
//import com.example.hiveeapp.volley.VolleySingleton;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class ViewEmployerInfoActivity extends AppCompatActivity {
//
//    private EditText nameField, emailField, phoneField, streetField, complementField, cityField, stateField, zipField, field;
//    private Button updateButton, deleteButton;
//    private ImageButton backArrowIcon;
//
//    private static final int MAX_PHONE_LENGTH = 15;
//    private static final int MIN_PHONE_LENGTH = 7;
//    private static final int ZIP_CODE_LENGTH = 5;
//
//    private static final String USER_PREFS = "MyAppPrefs"; // Shared preferences key
//    private static final String COMPANY_ID_KEY = "companyId"; // Key to store/retrieve companyId from SharedPreferences
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_employer_info);
//
//        // Initialize the input fields and buttons
//        initViews();
//
//        // Set up back arrow functionality to close the current activity and return to the previous one
//        backArrowIcon.setOnClickListener(v -> finish());
//
//        // Fetch employer information
//        fetchEmployerInfo();
//
//        // Handle the "Update" button click and validate inputs
//        updateButton.setOnClickListener(v -> {
//            if (validateInput()) {
//                try {
//                    updateEmployerInfo();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(ViewEmployerInfoActivity.this, "Error updating employer information.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        // Handle the "Delete" button click
//        deleteButton.setOnClickListener(v -> deleteEmployerInfo());
//    }
//
//    private void initViews() {
//        nameField = findViewById(R.id.name);
//        emailField = findViewById(R.id.email);
//        phoneField = findViewById(R.id.phone);
//        streetField = findViewById(R.id.street);
//        complementField = findViewById(R.id.complement);
//        cityField = findViewById(R.id.city);
//        stateField = findViewById(R.id.state);
//        zipField = findViewById(R.id.zip_code);
//        field = findViewById(R.id.field);
//        updateButton = findViewById(R.id.update_button);
//        deleteButton = findViewById(R.id.delete_button);
//        backArrowIcon = findViewById(R.id.backArrowIcon);
//    }
//
//    private boolean validateInput() {
//        boolean isValid = true;
//
//        // Reset any previous error messages
//        nameField.setError(null);
//        emailField.setError(null);
//        phoneField.setError(null);
//        streetField.setError(null);
//        cityField.setError(null);
//        stateField.setError(null);
//        zipField.setError(null);
//
//        // Get the input values from the fields
//        String name = nameField.getText().toString().trim();
//        String email = emailField.getText().toString().trim();
//        String phone = phoneField.getText().toString().trim();
//        String street = streetField.getText().toString().trim();
//        String city = cityField.getText().toString().trim();
//        String state = stateField.getText().toString().trim();
//        String zip = zipField.getText().toString().trim();
//
//        // Validate the name field
//        if (name.isEmpty()) {
//            nameField.setError("Name is required");
//            isValid = false;
//        }
//
//        // Validate the email field using Patterns.EMAIL_ADDRESS
//        if (email.isEmpty()) {
//            emailField.setError("Email is required");
//            isValid = false;
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            emailField.setError("Invalid email format");
//            isValid = false;
//        }
//
//        // Validate the phone field
//        if (phone.isEmpty()) {
//            phoneField.setError("Phone number is required");
//            isValid = false;
//        } else if (phone.length() > MAX_PHONE_LENGTH || phone.length() < MIN_PHONE_LENGTH) {
//            phoneField.setError("Phone number must be between " + MIN_PHONE_LENGTH + " and " + MAX_PHONE_LENGTH + " digits");
//            isValid = false;
//        } else if (!phone.matches("\\d+")) {
//            phoneField.setError("Phone number must contain only digits");
//            isValid = false;
//        }
//
//        // Validate the address fields
//        if (street.isEmpty()) {
//            streetField.setError("Street address is required");
//            isValid = false;
//        }
//
//        if (city.isEmpty()) {
//            cityField.setError("City is required");
//            isValid = false;
//        }
//
//        if (state.isEmpty()) {
//            stateField.setError("State is required");
//            isValid = false;
//        }
//
//        // Validate the ZIP code format (assuming US ZIP code)
//        if (zip.isEmpty()) {
//            zipField.setError("Zip code is required");
//            isValid = false;
//        } else if (zip.length() != ZIP_CODE_LENGTH || !zip.matches("\\d{" + ZIP_CODE_LENGTH + "}")) {
//            zipField.setError("Zip code must be " + ZIP_CODE_LENGTH + " digits");
//            isValid = false;
//        }
//
//        return isValid;
//    }
//
//    private void fetchEmployerInfo() {
//        // Retrieve the companyId from SharedPreferences (or use a hardcoded value for testing)
//        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
//        int companyId = prefs.getInt(COMPANY_ID_KEY, 177);  // Default to 177 for testing
//
//        String url = "http://coms-3090-063.class.las.iastate.edu:8080/employer?companyId=" + companyId;
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                // Log the complete JSON response
//                Log.d("ViewEmployerInfoActivity", "Response: " + response.toString());
//                try {
//                    nameField.setText(response.getString("name"));
//                    emailField.setText(response.getString("email"));
//                    phoneField.setText(response.getString("phone"));
//                    JSONObject address = response.getJSONObject("address");
//                    streetField.setText(address.getString("street"));
//                    complementField.setText(address.getString("complement"));
//                    cityField.setText(address.getString("city"));
//                    stateField.setText(address.getString("state"));
//                    zipField.setText(address.getString("zipCode"));
//                    field.setText(response.getString("field"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(ViewEmployerInfoActivity.this, "Error parsing employer information.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ViewEmployerInfoActivity.this, "Error fetching employer information.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
//    }
//
//    private void updateEmployerInfo() throws JSONException {
//        String url = "http://coms-3090-063.class.las.iastate.edu:8080/employer"; // Replace with actual employer ID
//        JSONObject employerData = new JSONObject();
//        employerData.put("name", nameField.getText().toString().trim());
//        employerData.put("email", emailField.getText().toString().trim());
//        employerData.put("phone", phoneField.getText().toString().trim());
//        JSONObject addressData = new JSONObject();
//        addressData.put("street", streetField.getText().toString().trim());
//        addressData.put("complement", complementField.getText().toString().trim());
//        addressData.put("city", cityField.getText().toString().trim());
//        addressData.put("state", stateField.getText().toString().trim());
//        addressData.put("zipCode", zipField.getText().toString().trim());
//        employerData.put("address", addressData);
//        employerData.put("field", field.getText().toString().trim());
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.PUT, url, employerData, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Toast.makeText(ViewEmployerInfoActivity.this, "Employer information updated successfully.", Toast.LENGTH_SHORT).show();
//                finish();  // Close the activity and return to the previous screen
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ViewEmployerInfoActivity.this, "Error updating employer information.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
//    }
//
//    private void deleteEmployerInfo() {
//        // Retrieve the companyId from SharedPreferences (or use a hardcoded value for testing)
//        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
//        int companyId = prefs.getInt(COMPANY_ID_KEY, 177);  // Default to 177 for testing
//
//        String url = "http://coms-3090-063.class.las.iastate.edu:8080/employer?companyId=" + companyId;
//        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(ViewEmployerInfoActivity.this, "Employer information deleted successfully.", Toast.LENGTH_SHORT).show();
//                finish();  // Close the activity and return to the previous screen
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ViewEmployerInfoActivity.this, "Error deleting employer information.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
//    }
//
//    private String getErrorMessage(VolleyError error) {
//        if (error.networkResponse != null && error.networkResponse.data != null) {
//            return new String(error.networkResponse.data);
//        }
//       return "An error occurred. Please try again.";
//    }
//}













//import android.os.Bundle;
//import android.util.Log;
//import android.util.Patterns;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.StringRequest;
//import com.example.hiveeapp.R;
//import com.example.hiveeapp.volley.VolleySingleton;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class ViewEmployerInfoActivity extends AppCompatActivity {
//
//    private EditText nameField, emailField, phoneField, streetField, complementField, cityField, stateField, zipField, field;
//    private Button updateButton, deleteButton;
//    private ImageButton backArrowIcon;
//
//    private static final int MAX_PHONE_LENGTH = 15;
//    private static final int MIN_PHONE_LENGTH = 7;
//    private static final int ZIP_CODE_LENGTH = 5;
//
//    private String companyId = "200"; // Initialize with actual company ID
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_employer_info);
//
//        // Initialize the input fields and buttons
//        initViews();
//
//        // Set up back arrow functionality to close the current activity and return to the previous one
//        backArrowIcon.setOnClickListener(v -> finish());
//
//        // Fetch employer information
//        fetchEmployerInfo();
//
//        // Handle the "Update" button click and validate inputs
//        updateButton.setOnClickListener(v -> {
//            if (validateInput()) {
//                try {
//                    updateEmployerInfo();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(ViewEmployerInfoActivity.this, "Error updating employer information.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        // Handle the "Delete" button click
//        deleteButton.setOnClickListener(v -> deleteEmployerInfo());
//    }
//
//    private void initViews() {
//        nameField = findViewById(R.id.name);
//        emailField = findViewById(R.id.email);
//        phoneField = findViewById(R.id.phone);
//        streetField = findViewById(R.id.street);
//        complementField = findViewById(R.id.complement);
//        cityField = findViewById(R.id.city);
//        stateField = findViewById(R.id.state);
//        zipField = findViewById(R.id.zip_code);
//        field = findViewById(R.id.field);
//        updateButton = findViewById(R.id.update_button);
//        deleteButton = findViewById(R.id.delete_button);
//        backArrowIcon = findViewById(R.id.backArrowIcon);
//    }
//
//    private boolean validateInput() {
//        boolean isValid = true;
//
//        // Reset any previous error messages
//        nameField.setError(null);
//        emailField.setError(null);
//        phoneField.setError(null);
//        streetField.setError(null);
//        cityField.setError(null);
//        stateField.setError(null);
//        zipField.setError(null);
//
//        // Get the input values from the fields
//        String name = nameField.getText().toString().trim();
//        String email = emailField.getText().toString().trim();
//        String phone = phoneField.getText().toString().trim();
//        String street = streetField.getText().toString().trim();
//        String city = cityField.getText().toString().trim();
//        String state = stateField.getText().toString().trim();
//        String zip = zipField.getText().toString().trim();
//
//        // Validate the name field
//        if (name.isEmpty()) {
//            nameField.setError("Name is required");
//            isValid = false;
//        }
//
//        // Validate the email field using Patterns.EMAIL_ADDRESS
//        if (email.isEmpty()) {
//            emailField.setError("Email is required");
//            isValid = false;
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            emailField.setError("Invalid email format");
//            isValid = false;
//        }
//
//        // Validate the phone field
//        if (phone.isEmpty()) {
//            phoneField.setError("Phone number is required");
//            isValid = false;
//        } else if (phone.length() > MAX_PHONE_LENGTH || phone.length() < MIN_PHONE_LENGTH) {
//            phoneField.setError("Phone number must be between " + MIN_PHONE_LENGTH + " and " + MAX_PHONE_LENGTH + " digits");
//            isValid = false;
//        } else if (!phone.matches("\\d+")) {
//            phoneField.setError("Phone number must contain only digits");
//            isValid = false;
//        }
//
//        // Validate the address fields
//        if (street.isEmpty()) {
//            streetField.setError("Street address is required");
//            isValid = false;
//        }
//
//        if (city.isEmpty()) {
//            cityField.setError("City is required");
//            isValid = false;
//        }
//
//        if (state.isEmpty()) {
//            stateField.setError("State is required");
//            isValid = false;
//        }
//
//        // Validate the ZIP code format (assuming US ZIP code)
//        if (zip.isEmpty()) {
//            zipField.setError("Zip code is required");
//            isValid = false;
//        } else if (zip.length() != ZIP_CODE_LENGTH || !zip.matches("\\d{" + ZIP_CODE_LENGTH + "}")) {
//            zipField.setError("Zip code must be " + ZIP_CODE_LENGTH + " digits");
//            isValid = false;
//        }
//
//        return isValid;
//    }
//
//    private void fetchEmployerInfo() {
//
//        String url = "http://coms-3090-063.class.las.iastate.edu:8080/employer?companyId=" + companyId;
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                // Log the complete JSON response
//                Log.d("ViewEmployerInfoActivity", "Response: " + response.toString());
//                try {
//                    nameField.setText(response.getString("name"));
//                    emailField.setText(response.getString("email"));
//                    phoneField.setText(response.getString("phone"));
//                    JSONObject address = response.getJSONObject("address");
//                    streetField.setText(address.getString("street"));
//                    complementField.setText(address.getString("complement"));
//                    cityField.setText(address.getString("city"));
//                    stateField.setText(address.getString("state"));
//                    zipField.setText(address.getString("zipCode"));
//                    field.setText(response.getString("field"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(ViewEmployerInfoActivity.this, "Error parsing employer information.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ViewEmployerInfoActivity.this, getErrorMessage(error), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
//    }
//
//    private void updateEmployerInfo() throws JSONException {
//        String url = "http://coms-3090-063.class.las.iastate.edu:8080/employer"; // Replace with actual employer ID
//        JSONObject employerData = new JSONObject();
//        employerData.put("name", nameField.getText().toString().trim());
//        employerData.put("email", emailField.getText().toString().trim());
//        employerData.put("phone", phoneField.getText().toString().trim());
//        JSONObject addressData = new JSONObject();
//        addressData.put("street", streetField.getText().toString().trim());
//        addressData.put("complement", complementField.getText().toString().trim());
//        addressData.put("city", cityField.getText().toString().trim());
//        addressData.put("state", stateField.getText().toString().trim());
//        addressData.put("zipCode", zipField.getText().toString().trim());
//        employerData.put("address", addressData);
//        employerData.put("field", field.getText().toString().trim());
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.PUT, url, employerData, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Toast.makeText(ViewEmployerInfoActivity.this, "Employer information updated successfully.", Toast.LENGTH_SHORT).show();
//                finish();  // Close the activity and return to the previous screen
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ViewEmployerInfoActivity.this, getErrorMessage(error), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
//    }
//
//    private void deleteEmployerInfo() {
//        String url = "http://coms-3090-063.class.las.iastate.edu:8080/employer?companyId=" + companyId;
//
//        StringRequest stringRequest = new StringRequest(
//                Request.Method.DELETE, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(ViewEmployerInfoActivity.this, "Employer information deleted successfully.", Toast.LENGTH_SHORT).show();
//                finish(); // Close the activity and return to the previous screen
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ViewEmployerInfoActivity.this, getErrorMessage(error), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
//    }
//
//    private String getErrorMessage(VolleyError error) {
//        if (error.networkResponse != null && error.networkResponse.data != null) {
//            return new String(error.networkResponse.data);
//        }
//        return "An error occurred. Please try again.";
//    }
//}

