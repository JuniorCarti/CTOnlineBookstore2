package com.example.ctonlinebookstore;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ProgressBar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputEditText etResetEmailAddress;
    private MaterialButton btnSubmitReset;
    private TextView tvEmailCheckInstructions;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private ImageButton btnBack; // Back button for navigation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initializing views
        etResetEmailAddress = findViewById(R.id.etResetEmailAddress);
        btnSubmitReset = findViewById(R.id.btnSubmitReset);
        tvEmailCheckInstructions = findViewById(R.id.tvEmailCheckInstructions);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack); // Initialize the back button

        // Set up the back button to navigate to the login screen
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class); // Assuming LoginActivity is your login screen
            startActivity(intent);
            finish(); // Optional: Finish this activity to remove it from the stack
        });

        // Submit button click handler
        btnSubmitReset.setOnClickListener(v -> {
            String enteredEmail = etResetEmailAddress.getText().toString().trim();

            if (TextUtils.isEmpty(enteredEmail)) {
                tvEmailCheckInstructions.setText("*Please enter an email address.");
                tvEmailCheckInstructions.setTextColor(Color.RED);
            } else if (isValidEmail(enteredEmail)) {
                progressBar.setVisibility(ProgressBar.VISIBLE);
                sendResetPasswordInstructions(enteredEmail);
            } else {
                tvEmailCheckInstructions.setText("*This email is not registered for password reset.");
                tvEmailCheckInstructions.setTextColor(Color.RED);
            }
        });
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    private void sendResetPasswordInstructions(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(ProgressBar.GONE);
                    if (task.isSuccessful()) {
                        tvEmailCheckInstructions.setText("*Password reset instructions sent to " + email + ".");
                        tvEmailCheckInstructions.setTextColor(Color.GREEN);
                    } else {
                        tvEmailCheckInstructions.setText("*Failed to send password reset instructions.");
                        tvEmailCheckInstructions.setTextColor(Color.RED);
                    }
                });
    }
}
