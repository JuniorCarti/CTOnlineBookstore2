package com.example.ctonlinebookstore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etLoginEmail, etLoginPassword;
    private MaterialButton btnLoginSubmit;
    private TextView tvSignUpOption, tvResetPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLoginSubmit = findViewById(R.id.btnLoginSubmit);
        tvSignUpOption = findViewById(R.id.tvSignUpOption);
        tvResetPassword = findViewById(R.id.tvResetPassword);

        // When SignUp TextView is clicked, navigate to SignUpActivity
        tvSignUpOption.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // When Reset Password TextView is clicked, navigate to ResetPasswordActivity
        tvResetPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });

        // Handle login button click
        btnLoginSubmit.setOnClickListener(v -> {
            String email = etLoginEmail.getText().toString().trim();
            String password = etLoginPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            } else {
                loginUserWithFirebase(email, password);
            }
        });
    }

    private void loginUserWithFirebase(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, fetch user details
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String displayName = user.getDisplayName();
                            String greetingMessage = getPersonalizedGreeting(displayName);

                            // Pass greeting message to MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("greetingMessage", greetingMessage);
                            startActivity(intent);
                            finish();  // Close the login activity
                        }
                    } else {
                        // If sign-in fails, display a message to the user
                        Toast.makeText(LoginActivity.this, "Authentication failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to generate a personalized greeting based on the time of day
    private String getPersonalizedGreeting(String name) {
        // Get the current hour
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        String greeting = "Good ";
        if (hour >= 0 && hour < 12) {
            greeting += "Morning";
        } else if (hour >= 12 && hour < 17) {
            greeting += "Afternoon";
        } else {
            greeting += "Evening";
        }

        return greeting + ", " + (name != null ? name : "User");  // Handle null name case
    }
}
