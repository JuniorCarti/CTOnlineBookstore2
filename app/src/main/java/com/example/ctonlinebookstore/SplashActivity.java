package com.example.ctonlinebookstore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 2000; // 2 seconds
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_ONBOARDING_COMPLETED = "OnboardingCompleted";
    private static final String KEY_WELCOME_COMPLETED = "WelcomeCompleted";
    private static final String KEY_IS_LOGGED_IN = "IsLoggedIn";
    private static final String KEY_LAST_ACTIVE_TIME = "LastActiveTime";

    private static final long SESSION_TIMEOUT = 30 * 60 * 1000; // 30 minutes

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            boolean hasCompletedOnboarding = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false);
            boolean hasCompletedWelcome = prefs.getBoolean(KEY_WELCOME_COMPLETED, false);
            boolean isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false);
            long lastActiveTime = prefs.getLong(KEY_LAST_ACTIVE_TIME, 0);
            long currentTime = System.currentTimeMillis();

            Intent intent;

            if (!hasCompletedOnboarding) {
                intent = new Intent(SplashActivity.this, OnboardingActivity.class);
            } else if (!hasCompletedWelcome) {
                intent = new Intent(SplashActivity.this, WelcomeActivity.class);
            } else if (!isLoggedIn) {
                intent = new Intent(SplashActivity.this, SignUpActivity.class);
            } else if (currentTime - lastActiveTime > SESSION_TIMEOUT) {
                // If session is expired, log the user out
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(KEY_IS_LOGGED_IN, false);
                editor.apply();
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }

            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }
}
