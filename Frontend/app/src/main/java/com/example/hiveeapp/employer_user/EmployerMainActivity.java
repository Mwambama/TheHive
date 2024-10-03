package com.example.hiveeapp.employer_user;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

    public class EmployerMainActivity extends AppCompatActivity {
        private EditText companyNameEditText;
        private EditText companyLogoUrlEditText;
        private EditText companyDescriptionEditText;
        private EditText industryEditText;
        private EditText locationEditText;
        private EditText websiteEditText;
        private EditText recruiterNameEditText;
        private EditText contactEmailEditText;
        private EditText phoneNumberEditText;
        private EditText socialMediaLinksEditText;
        private Button saveProfileButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.employer_activities);

            // Initialize EditText fields
            companyNameEditText = findViewById(R.id.company_name);
            //companyLogoUrlEditText = findViewById(R.id.company_logo_url);
            companyDescriptionEditText = findViewById(R.id.company_description);
            industryEditText = findViewById(R.id.industry);
            locationEditText = findViewById(R.id.location);
            websiteEditText = findViewById(R.id.website);
            recruiterNameEditText = findViewById(R.id.recruiter_name);
            contactEmailEditText = findViewById(R.id.contact_email);
            phoneNumberEditText = findViewById(R.id.phone_number);
            socialMediaLinksEditText = findViewById(R.id.social_media_links);
            saveProfileButton = findViewById(R.id.save_profile_button);

            // Save button click listener
            saveProfileButton.setOnClickListener(view -> {
                String companyName = companyNameEditText.getText().toString();
                String companyLogoUrl = companyLogoUrlEditText.getText().toString();
                String companyDescription = companyDescriptionEditText.getText().toString();
                String industry = industryEditText.getText().toString();
                String location = locationEditText.getText().toString();
                String website = websiteEditText.getText().toString();
                String recruiterName = recruiterNameEditText.getText().toString();
                String contactEmail = contactEmailEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();
                String socialMediaLinks = socialMediaLinksEditText.getText().toString();

                // Check for empty fields
                if (companyName.isEmpty() || companyDescription.isEmpty() || industry.isEmpty() || location.isEmpty() || website.isEmpty() ||
                        recruiterName.isEmpty() || contactEmail.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(EmployerMainActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Create JSON object with profile data
                JSONObject profileData = new JSONObject();
                try {
                    profileData.put("companyName", companyName);
                    profileData.put("companyLogoUrl", companyLogoUrl);
                    profileData.put("companyDescription", companyDescription);
                    profileData.put("industry", industry);
                    profileData.put("location", location);
                    profileData.put("website", website);
                    profileData.put("recruiterName", recruiterName);
                    profileData.put("contactEmail", contactEmail);
                    profileData.put("phoneNumber", phoneNumber);
                    profileData.put("socialMediaLinks", socialMediaLinks.split(","));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // URL for the Postman mock server
                String url = "https://8c5d8b24-4a9a-4ce2-bf22-1aa5316f76a2.mock.pstmn.io/employer/profile"; // Replace with your mock server URL

                // Create JSON request
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        profileData,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Handle successful profile save
                                Toast.makeText(EmployerMainActivity.this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error
                                Toast.makeText(EmployerMainActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );

                // Add request to the Volley request queue
                VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
            });
        }
    }




