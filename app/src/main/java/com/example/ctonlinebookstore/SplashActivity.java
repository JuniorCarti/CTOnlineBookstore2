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
    private static final String PREF_NAME = "OnboardingPrefs";
    private static final String KEY_ONBOARDING_COMPLETED = "OnboardingCompleted";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            boolean hasCompletedOnboarding = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false);

            Intent intent;
            if (hasCompletedOnboarding) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, OnboardingActivity.class);
            }
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }
}
