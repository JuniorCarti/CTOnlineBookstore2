package com.example.ctonlinebookstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText etName, etEmail, etPassword, etConfirmPassword;
    private MaterialButton btnCreateAccount;
    private TextView tvLogin, tvTermsConditions;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private SharedPreferences sharedPreferences;

    // Google Sign In variables
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001; // Request code for Google sign in

    // Facebook Sign In variables
    private CallbackManager callbackManager;

    // Progress dialog to show during sign in
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        tvLogin = findViewById(R.id.tvLogin);
        tvTermsConditions = findViewById(R.id.tvTermsConditions);

        // Initialize progress dialog
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Signing in...");
        progressDialog.setCancelable(false);

        // Set up the Google Sign In options and client.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Ensure this is set in strings.xml
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize Facebook CallbackManager
        callbackManager = CallbackManager.Factory.create();

        // On Create Account button click (Email sign up)
        btnCreateAccount.setOnClickListener(v -> registerUser());

        // When login button is clicked, navigate to LoginActivity
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Prevent navigating back to SignUpActivity
        });

        // Navigate to Terms and Conditions
        tvTermsConditions.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, TermsAndConditionsActivity.class);
            startActivity(intent);
        });
    }

    // Called when the Google Sign In button is clicked
    public void onGoogleLoginClicked(View view) {
        progressDialog.show();
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Called when the Facebook Sign In button is clicked
    public void onFacebookLoginClicked(View view) {
        progressDialog.show();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this, "Facebook sign in canceled", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException error) {
                progressDialog.dismiss();
                Log.w("SignUpActivity", "Facebook sign in failed", error);
                Toast.makeText(SignUpActivity.this, "Facebook sign in failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass result to Facebook callback manager
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                progressDialog.dismiss();
                Log.w("SignUpActivity", "Google sign in failed", e);
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            saveUserToDatabase(firebaseUser.getUid(), firebaseUser.getEmail(), firebaseUser.getDisplayName());
                        }
                    } else {
                        Log.w("SignUpActivity", "signInWithCredential:failure", task.getException());
                        Toast.makeText(SignUpActivity.this, "Firebase Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            saveUserToDatabase(firebaseUser.getUid(), firebaseUser.getEmail(), firebaseUser.getDisplayName());
                        }
                    } else {
                        Log.w("SignUpActivity", "signInWithCredential:failure", task.getException());
                        Toast.makeText(SignUpActivity.this, "Firebase Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void registerUser() {
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

        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }

        if (!isValidPassword(password)) {
            etPassword.setError("Password must be at least 6 characters, contain 1 uppercase letter, and 1 symbol");
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }

        // Optionally, you can show a progress dialog here too
        progressDialog.setMessage("Creating account...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            saveUserToDatabase(firebaseUser.getUid(), email, name);
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Log.e("FirebaseAuthError", errorMessage);
                        Toast.makeText(SignUpActivity.this, "Registration failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToDatabase(String userId, String email, String name) {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("userId", userId);
        userMap.put("email", email);
        userMap.put("name", name);
        userMap.put("createdAt", System.currentTimeMillis());

        usersRef.child(userId).setValue(userMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        navigateToMainActivity();
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Log.e("DatabaseError", errorMessage);
                        Toast.makeText(SignUpActivity.this, "Database error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isValidPassword(String password) {
        Pattern passwordPattern = Pattern.compile("^(?=.*[A-Z])(?=.*[@#$%^&+=]).{6,}$");
        return passwordPattern.matcher(password).matches();
    }
}
