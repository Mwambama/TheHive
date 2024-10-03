package com.example.hiveeapp.employer_user;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hiveeapp.R;

//import com.squareup.picasso.Picasso;
    public class employerProfileActivity extends AppCompatActivity {
        private TextView companyNameTextView;
        private ImageView companyLogoImageView;
        private TextView companyDescriptionTextView;
        private TextView recruiterNameTextView;
        private TextView contactEmailTextView;
        private TextView phoneNumberTextView;
        private TextView availableJobsTextView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.employer_activity);

            // Initialize views
            companyNameTextView = findViewById(R.id.company_name);
            //companyLogoImageView = findViewById(R.id.company_logo_url);
            companyDescriptionTextView = findViewById(R.id.company_description);
            recruiterNameTextView = findViewById(R.id.recruiter_name);
            contactEmailTextView = findViewById(R.id.contact_email);
            phoneNumberTextView = findViewById(R.id.phone_number);
          //  availableJobsTextView = findViewById(R.id.available_jobs);

            // Fetch employer profile data (this could be from an API or database)
            fetchEmployerProfileData();
        }

        private void fetchEmployerProfileData() {
            // Sample data, replace with actual data retrieval logic
            String companyName = "HiveApp";
            String companyLogoUrl = "https://example.com/logo.png";
            String companyDescription = "HiveApp is a leading tech company...";
            String recruiterName = "Jessica Bob";
            String contactEmail = "mywork@gmail.co";
            String phoneNumber = "(207)-544-3280";

            // Set data to views
            companyNameTextView.setText(companyName);
            //Picasso.get().load(companyLogoUrl).into(companyLogoImageView);
            companyDescriptionTextView.setText(companyDescription);
            recruiterNameTextView.setText(recruiterName);
            contactEmailTextView.setText(contactEmail);
            phoneNumberTextView.setText(phoneNumber);

            // Set click listener for available jobs
            availableJobsTextView.setOnClickListener(view -> {
                // Navigate to job postings activity or open job postings URL
            });
        }
    }


