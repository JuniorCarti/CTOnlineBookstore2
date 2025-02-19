package com.example.ctonlinebookstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

import adapters.OnboardingAdapter;
import models.OnboardingItem;

public class OnboardingActivity extends AppCompatActivity implements OnboardingAdapter.OnboardingNavigationListener {
    private static final String PREF_NAME = "OnboardingPrefs";
    private static final String KEY_ONBOARDING_COMPLETED = "OnboardingCompleted";

    private ViewPager2 viewPager;
    private List<OnboardingItem> onboardingItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🔹 Check if onboarding was already completed
        if (isOnboardingCompleted()) {
            navigateToWelcome();
            return;
        }

        setContentView(R.layout.activity_onboarding);
        viewPager = findViewById(R.id.viewPager);
        onboardingItems = getOnboardingData();

        // 🔹 Use improved adapter with navigation listener
        OnboardingAdapter adapter = new OnboardingAdapter(onboardingItems, this);
        viewPager.setAdapter(adapter);
    }

    private List<OnboardingItem> getOnboardingData() {
        List<OnboardingItem> items = new ArrayList<>();
        items.add(new OnboardingItem(1, R.drawable.onboarding1, "Explore Our Products",
                "Find Exercise Books, Drawing Books, Office Supplies & More!"));
        items.add(new OnboardingItem(2, R.drawable.onboarding2, "Easy Ordering & Fast Delivery",
                "Order & Get Your Stationery Delivered – Fast & Secure!"));
        items.add(new OnboardingItem(3, R.drawable.onboarding3, "Secure Payments & Reliable Service",
                "Multiple Payment Options & 24/7 Customer Support!"));
        return items;
    }

    @Override
    public void onSkip() {
        completeOnboarding();
    }

    @Override
    public void onNext(int position) {
        if (position < onboardingItems.size() - 1) {
            viewPager.setCurrentItem(position + 1, true); // 🔹 Smooth transition
        } else {
            completeOnboarding();
        }
    }

    private void completeOnboarding() {
        getSharedPreferences(PREF_NAME, MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_ONBOARDING_COMPLETED, true)
                .apply();
        navigateToWelcome();
    }

    private void navigateToWelcome() {
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }

    private boolean isOnboardingCompleted() {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return preferences.getBoolean(KEY_ONBOARDING_COMPLETED, false);
    }
}
