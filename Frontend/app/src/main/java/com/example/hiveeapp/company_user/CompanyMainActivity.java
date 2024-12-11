package com.example.hiveeapp.company_user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.hiveeapp.MainActivity;
import com.example.hiveeapp.R;
import com.example.hiveeapp.company_user.handleEmployers.EmployerListActivity;
import com.example.hiveeapp.company_user.invitations.InvitationManagementActivity;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

/**
 * CompanyMainActivity handles the main UI for the company user, including top app bar,
 * tab layout for fragment switching, and bottom navigation for accessing different activities.
 */
public class CompanyMainActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private BottomNavigationView bottomNavigationView;
    private TabLayout tabLayout;

    private int userId;
    private String userEmail;
    private String userPassword;

    /**
     * Called when the activity is first created. Sets up the UI components and navigation listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied in onSaveInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        retrieveUserDetails();

        // Initialize views
        topAppBar = findViewById(R.id.topAppBar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        tabLayout = findViewById(R.id.tabLayout);

        // Set up the top app bar with a back button
        topAppBar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(CompanyMainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Add tabs programmatically for "Company Info", "Invitations", and "Employers"
        tabLayout.addTab(tabLayout.newTab().setText("Company Info"));
        tabLayout.addTab(tabLayout.newTab().setText("Invitations"));
        tabLayout.addTab(tabLayout.newTab().setText("Employers"));

        // Set up the tab layout listener for fragment switching
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment;
                switch (tab.getPosition()) {
                    case 0:
                        selectedFragment = new CompanyInfoFragment();
                        break;
                    case 1:
                        selectedFragment = new InvitationsFragment();
                        break;
                    case 2:
                        selectedFragment = new EmployersFragment();
                        break;
                    default:
                        selectedFragment = new CompanyInfoFragment();
                }
                loadFragment(selectedFragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Load default fragment (Company Info) when the activity is opened
        loadFragment(new CompanyInfoFragment());

        // Set up bottom navigation view for navigation to Employers and Invitations
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_invitations) {
                startActivity(new Intent(CompanyMainActivity.this, InvitationManagementActivity.class));
                return true;
            } else if (itemId == R.id.navigation_employers) {
                startActivity(new Intent(CompanyMainActivity.this, EmployerListActivity.class));
                return true;
            } else if (itemId == R.id.navigation_main_user_page) {
                return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_main_user_page);
    }

    /**
     * Retrieves user details from SharedPreferences and checks if they are valid.
     * If user details are missing, prompts the user to log in again.
     */
    private void retrieveUserDetails() {
        SharedPreferences preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);
        userEmail = preferences.getString("email", "");
        userPassword = preferences.getString("password", "");

        if (userId == -1 || userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this, "User credentials not found. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    /**
     * Helper method to load fragments into the frame layout.
     *
     * @param fragment The fragment to be loaded.
     */
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}