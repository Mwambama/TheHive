package com.example.hiveeapp.company_user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.hiveeapp.MainActivity;
import com.example.hiveeapp.R;
import com.example.hiveeapp.company_user.handleEmployers.EmployerListActivity;
import com.example.hiveeapp.company_user.invitations.InvitationManagementActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class CompanyActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private BottomNavigationView bottomNavigationView;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        // Initialize views
        topAppBar = findViewById(R.id.topAppBar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        tabLayout = findViewById(R.id.tabLayout);

        // Set up the top app bar with a back button
        topAppBar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(CompanyActivity.this, MainActivity.class);
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
            public void onTabUnselected(TabLayout.Tab tab) {
                // Optional: Handle tab unselected (e.g., saving state or stopping tasks)
                Toast.makeText(CompanyActivity.this, "Tab " + tab.getText() + " unselected.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Optional: Handle reselecting the current tab (e.g., refreshing or scrolling to top)
                Toast.makeText(CompanyActivity.this, "Tab " + tab.getText() + " reselected. Refreshing content.", Toast.LENGTH_SHORT).show();

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                if (currentFragment != null) {
                    loadFragment(currentFragment); // Refresh the current fragment
                }
            }
        });

        // Load default fragment (Company Info) when the activity is opened
        loadFragment(new CompanyInfoFragment());

        // Set up bottom navigation view for navigation to Employers and Invitations
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_invitations) {
                startActivity(new Intent(CompanyActivity.this, InvitationManagementActivity.class));
                return true;
            } else if (itemId == R.id.navigation_employers) {
                startActivity(new Intent(CompanyActivity.this, EmployerListActivity.class));
                return true;
            } else if (itemId == R.id.navigation_main_user_page) {
                Toast.makeText(CompanyActivity.this, "You are already on this page", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        // Set the default selected item in the bottom navigation
        bottomNavigationView.setSelectedItemId(R.id.navigation_main_user_page);
    }

    // Helper method to load fragments into the frame layout
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}