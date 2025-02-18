package com.example.ctonlinebookstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText etName, etEmail, etPassword, etConfirmPassword;
    private MaterialButton btnCreateAccount;
    private TextView tvLogin, tvTermsConditions;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        etName = findViewById(R.id.etName); // Name input field
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        tvLogin = findViewById(R.id.tvLogin);
        tvTermsConditions = findViewById(R.id.tvTermsConditions);

        // OnCreate Account button click
        btnCreateAccount.setOnClickListener(v -> registerUser());

        // When login button is clicked, navigate to LoginActivity
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close SignUpActivity to avoid navigating back to it
        });

        // Navigate to Terms and Conditions
        tvTermsConditions.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, TermsAndConditionsActivity.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        // Check if terms and conditions are accepted
        boolean isTermsAccepted = sharedPreferences.getBoolean("TermsAccepted", false);
        Log.d("RegisterUser", "Terms Accepted: " + isTermsAccepted);

        if (!isTermsAccepted) {
            Toast.makeText(this, "You must accept the Terms & Conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validate name
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            return;
        }

        // Validate email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            return;
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }

        if (!isValidPassword(password)) {
            etPassword.setError("Password must be at least 6 characters, contain 1 uppercase letter, and 1 symbol");
            return;
        }

        // Validate confirm password
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }

        // Create user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            // Save user data in Realtime Database
                            saveUserToDatabase(firebaseUser.getUid(), email, name);
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Log.e("FirebaseAuthError", errorMessage);
                        Toast.makeText(SignUpActivity.this, "Registration failed: " + errorMessage,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToDatabase(String userId, String email, String name) {
        // Prepare user data for saving in Realtime Database
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("userId", userId);
        userMap.put("email", email);
        userMap.put("name", name);  // Save the name as well
        userMap.put("createdAt", System.currentTimeMillis());

        // Save user data in Realtime Database
        usersRef.child(userId).setValue(userMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        navigateToMainActivity();
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Log.e("DatabaseError", errorMessage);
                        Toast.makeText(SignUpActivity.this, "Database error: " + errorMessage,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close SignUpActivity to prevent back navigation
    }

    private boolean isValidPassword(String password) {
        Pattern passwordPattern = Pattern.compile("^(?=.*[A-Z])(?=.*[@#$%^&+=]).{6,}$");
        return passwordPattern.matcher(password).matches();
    }
}
