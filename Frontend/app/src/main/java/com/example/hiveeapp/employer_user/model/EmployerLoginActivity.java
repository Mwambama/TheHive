package com.example.hiveeapp.employer_user.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.EmployerMainActivity;

public class EmployerLoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    private static final boolean TESTING_MODE = true;
    private static final String USER_PREFS = "user_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Simulate login process
            if (isValidCredentials(username, password)) {
                // Store credentials in SharedPreferences
                if (!TESTING_MODE) {
                    SharedPreferences preferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.apply();
                }
                // Proceed to the employer main activity
                Intent intent = new Intent(EmployerLoginActivity.this, EmployerMainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(EmployerLoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidCredentials(String username, String password) {
        // Simulate credential validation
        return "iiik@gmail.com".equals(username) && "Anondwdb##444fedo".equals(password);
    }
}
