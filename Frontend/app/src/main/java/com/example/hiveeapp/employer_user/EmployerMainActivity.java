package com.example.hiveeapp.employer_user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.display.EditJobActivity;
import com.example.hiveeapp.employer_user.chat.ChatActivity;
import com.example.hiveeapp.employer_user.model.TrackingApplicationActivity;
import com.example.hiveeapp.employer_user.setting.EmployerProfileActivity;
import com.example.hiveeapp.employer_user.setting.ViewEmployerInfoActivity;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class EmployerMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Button logoutButton, viewInfoButton;
    private TabLayout tabLayout;
    private int userId;
    private String userEmail;
    private String userPassword;
    private static final String TAG = "EmployerMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_employer_profile);

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(view -> onBackPressed());

        retrieveEmployerDetails();

        tabLayout = findViewById(R.id.tabLayouts);
        tabLayout.addTab(tabLayout.newTab().setText("Main Page"));
        tabLayout.addTab(tabLayout.newTab().setText("About"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = new employerInfoFragment();
                loadFragment(selectedFragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        loadFragment(new employerInfoFragment());

        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(view -> logout());

        viewInfoButton = findViewById(R.id.view_employer_info_button);
        viewInfoButton.setOnClickListener(view -> {
            Intent intent = new Intent(EmployerMainActivity.this, ViewEmployerInfoActivity.class);
            startActivity(intent);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void retrieveEmployerDetails() {
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navigation_main_user_page) {
            navigateToProfile();
            return true;
        } else if (itemId == R.id.nav_tracking) {
            startActivity(new Intent(this, TrackingApplicationActivity.class));
            return true;
        } else if (itemId == R.id.nav_add_job) {
            startActivity(new Intent(this, EditJobActivity.class));
            return true;
        } else if (itemId == R.id.nav_chat) {
            startActivity(new Intent(this, ChatActivity.class));
            return true;
        }
        return false;
    }

    private void navigateToProfile() {
        Intent intent = new Intent(EmployerMainActivity.this, EmployerProfileActivity.class);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("USER_PASSWORD", userPassword);
        Log.d(TAG, "Navigating to EmployerProfileActivity with userId: " + userId);
        startActivity(intent);
    }

    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}
