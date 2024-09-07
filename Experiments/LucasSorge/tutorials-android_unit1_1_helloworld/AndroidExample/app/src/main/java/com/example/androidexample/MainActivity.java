package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable
    private Button changeTextButton;
    private int clickCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        messageText.setText("Hello World");

        changeTextButton = findViewById(R.id.change_text_btn);  // link to button to change text

        /* Set onClickListener for the changeTextButton */
        changeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCounter++;
                String newText = "Button clicked " + clickCounter + " times";
                messageText.setText(newText);  // update the text view
                Toast.makeText(MainActivity.this, newText, Toast.LENGTH_SHORT).show();
            }
        });
    }
}