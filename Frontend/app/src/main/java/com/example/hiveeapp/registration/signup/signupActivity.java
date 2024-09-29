package com.example.hiveeapp.registration.signup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.R;

public class signupActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView messageText;   // define message textview variable
    private Button signupButton;    // define signup button variable
    private EditText usernameEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private EditText confirmEditText;   // define confirm edittext variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);

        // Find the ImageView from the layout
        //imageView = findViewById(R.id.imageView);

        /* initialize UI elements */
      //  usernameEditText = findViewById(R.id.signup_username_edt);  // link to username edtext in the activity register XML
      //  passwordEditText = findViewById(R.id.signup_password_edt);  // link to password edtext in the activity register XML
     //   confirmEditText = findViewById(R.id.signup_confirm_edt);    // link to confirm edtext in the  activity register XML
     //   loginButton = findViewById(R.id.signup_login_btn);    // link to login button in the Signup activity registerXML
     //   signupButton = findViewById(R.id.signup_signup_btn);  // link to signup button in the Signup activity register XML




        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirm = confirmEditText.getText().toString();

                if (!password.equals(confirm)) {
                    Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_LONG).show();
                } else if (!isPasswordValid(password)) {
                    Toast.makeText(getApplicationContext(), "Password must be at least 7 characters long, contain an uppercase letter, and a number", Toast.LENGTH_LONG).show(); // limitation of characters etc.
                } else {
                    Toast.makeText(getApplicationContext(), "Signing up", Toast.LENGTH_LONG).show();   // all else if everything is good, sign up;
                }
            }
        });
    }
    private boolean isPasswordValid(String password) {
        if (password.length() < 7) {
            return false;
        }
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasDigit = password.matches(".*\\d.*");
        return hasUppercase && hasDigit;


    }
}
