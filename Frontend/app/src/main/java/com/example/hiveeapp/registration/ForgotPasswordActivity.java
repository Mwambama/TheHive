package com.example.hiveeapp.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hiveeapp.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailField;
    private Button sendKeyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailField = findViewById(R.id.forgotPasswordEmailField);
        sendKeyButton = findViewById(R.id.sendKeyButton);

        sendKeyButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Please enter your email.", Toast.LENGTH_SHORT).show();
            } else {


                // Navigate to VerifyKeyActivity
                Intent intent = new Intent(ForgotPasswordActivity.this, VerifyKeyActivity.class);
                intent.putExtra("email", email); // Pass the email to the next activity
                startActivity(intent);
            }
        });
    }
}

