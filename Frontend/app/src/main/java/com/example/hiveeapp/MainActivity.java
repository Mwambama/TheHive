package com.example.hiveeapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hiveeapp.company_user.CompanyActivity;
import com.example.hiveeapp.employer_user.AddJobActivity;
import com.example.hiveeapp.employer_user.mainEmployer;
import com.example.hiveeapp.registration.LoginActivity;
import com.example.hiveeapp.employer_user.EmployerMainActivity; // Import the EmployerMainActivity

public class MainActivity extends AppCompatActivity {

    //private Button goToCompanyActivityButton;
    //private Button goToLoginActivityButton; // Declare the login button
    private Button goToEmployerActivityButton; // Declare the employer profile button
   // private Button go

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //goToCompanyActivityButton = findViewById(R.id.goToCompanyActivityButton);
        //goToLoginActivityButton = findViewById(R.id.goToLoginActivityButton); // Initialize the login button
        goToEmployerActivityButton = findViewById(R.id.goToEmployerActivityButton); // Initialize the employer profile button


//        // Navigate to CompanyActivity
//        goToCompanyActivityButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, CompanyActivity.class);
//                startActivity(intent);
//            }
//        });

//        // Navigate to LoginActivity
//        goToLoginActivityButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
        // Navigate to EmployerMainActivity
        goToEmployerActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, mainEmployer.class);
                startActivity(intent);
            }
        });
    }
}