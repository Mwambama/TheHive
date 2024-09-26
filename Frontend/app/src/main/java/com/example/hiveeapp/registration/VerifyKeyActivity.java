package com.example.hiveeapp.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hiveeapp.R;

public class VerifyKeyActivity extends AppCompatActivity {

    private EditText keyField;
    private Button verifyKeyButton;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_key);

        keyField = findViewById(R.id.keyField);
        verifyKeyButton = findViewById(R.id.verifyKeyButton);

        // Retrieve the email from the previous activity
        email = getIntent().getStringExtra("email");

        verifyKeyButton.setOnClickListener(v -> {
            String key = keyField.getText().toString().trim();
            if (key.isEmpty()) {
                Toast.makeText(VerifyKeyActivity.this, "Please enter the key.", Toast.LENGTH_SHORT).show();
            } else {

                // For now, assume the key is correct
                // Navigate to ResetPasswordActivity
                Intent intent = new Intent(VerifyKeyActivity.this, ResetPasswordActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
    }
}

