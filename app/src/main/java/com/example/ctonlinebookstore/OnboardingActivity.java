package com.example.ctonlinebookstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

import adapters.OnboardingAdapter;
import models.OnboardingItem;

public class OnboardingActivity extends AppCompatActivity {
    private static final String PREF_NAME = "OnboardingPrefs";
    private static final String KEY_ONBOARDING_COMPLETED = "OnboardingCompleted";

    private ViewPager2 viewPager;
    private List<OnboardingItem> onboardingItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);
        onboardingItems = getOnboardingData();
        OnboardingAdapter adapter = new OnboardingAdapter(this, onboardingItems);
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

    public void skipOnboarding(View view) {
        completeOnboarding();
    }

    public void nextScreen(View view) {
        if (viewPager != null && viewPager.getCurrentItem() < onboardingItems.size() - 1) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        } else {
            completeOnboarding();
        }
    }

    private void completeOnboarding() {
        getSharedPreferences(PREF_NAME, MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_ONBOARDING_COMPLETED, true)
                .apply();

        // ✅ Go to WelcomeActivity instead of MainActivity
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }
}
