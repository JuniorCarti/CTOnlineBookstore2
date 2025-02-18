package com.example.ctonlinebookstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    private static final String PREF_NAME = "OnboardingPrefs";
    private static final String KEY_WELCOME_COMPLETED = "WelcomeCompleted";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // 🔹 Find the Get Started button
        Button getStartedButton = findViewById(R.id.getStartedButton);

        // 🔹 Set up click listener
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeWelcome();
            }
        });
    }

    private void completeWelcome() {
        // ✅ Mark Welcome as completed
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_WELCOME_COMPLETED, true);
        editor.apply();

        // ✅ Go to SignupActivity
        startActivity(new Intent(this, SignUpActivity.class));
        finish(); // Close WelcomeActivity
    }
}
