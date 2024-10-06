package com.example.hiveeapp.company_user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.MainActivity;
import com.example.hiveeapp.R;
import com.example.hiveeapp.company_user.handleEmployers.EmployerListActivity;
import com.example.hiveeapp.company_user.invitations.InvitationManagementActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CompanyActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        // Set up the top app bar
        topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(CompanyActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Set up the bottom navigation view
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // Set the selected item to main user page
        bottomNavigationView.setSelectedItemId(R.id.navigation_main_user_page);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            String selectedTitle = item.getTitle().toString();

            if (selectedTitle.equals("manage_employers")) {
                startActivity(new Intent(CompanyActivity.this, EmployerListActivity.class));
                return true;
            } else if (selectedTitle.equals("main_user_page")) {
                Toast.makeText(CompanyActivity.this, "You are already on this page", Toast.LENGTH_SHORT).show();
                return true;
            } else if (selectedTitle.equals("manage_invitations")) {
                startActivity(new Intent(CompanyActivity.this, InvitationManagementActivity.class));
                return true;
            }

            return false;
        });
    }
}