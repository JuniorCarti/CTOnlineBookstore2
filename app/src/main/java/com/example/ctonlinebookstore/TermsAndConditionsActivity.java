package com.example.ctonlinebookstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TermsAndConditionsActivity extends AppCompatActivity {
    private Button btnAccept, btnDecline;
    private TextView tvTermsContent;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        tvTermsContent = findViewById(R.id.tvTermsContent);
        btnAccept = findViewById(R.id.btnAccept);
        btnDecline = findViewById(R.id.btnDecline);

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        // Example Terms Content (Replace with actual content)
        tvTermsContent.setText("TERMS AND CONDITIONS EFFECTIVE DATE: 2/18/2025\n" +
                "Welcome to CT Online Bookstore. By creating an account and using this application, you agree to comply with the following terms and conditions. If you do not agree with these terms, please do not use our services.\n" +
                "•\tACCOUNT REGISTRATION\n" +
                "You must provide accurate and up-to-date information during account registration.\n" +
                "You are responsible for maintaining the confidentiality of your account credentials.\n" +
                "Any unauthorized use of your account must be reported immediately.\n" +
                "\n" +
                "•\tUSE OF SERVICES\n" +
                "This app is intended for purchasing and managing books online.\n" +
                "You must not use this app for any illegal or fraudulent activities.\n" +
                "We reserve the right to suspend or terminate your account if we detect any misuse.\n" +
                "\n" +
                "•\tPRIVACY POLICY\n" +
                "We collect and store your personal data to improve our services.\n" +
                "Your information will not be shared with third parties without your consent, except as required by law.\n" +
                "By using this app, you agree to our privacy policy.\n" +
                "\n" +
                "•\tPAYMENTS AND REFUNDS\n" +
                "All transactions are processed securely.\n" +
                "Refunds are only issued under specific circumstances outlined in our refund policy.\n" +
                "Prices of books and services are subject to change without prior notice.\n" +
                "\n" +
                "•\tUSER CONDUCT\n" +
                "You must not upload, share, or transmit any harmful content.\n" +
                "Harassment, abuse, or threatening behavior towards other users will result in account suspension.\n" +
                "\n" +
                "•\tTERMINATION OF SERVICE\n" +
                "We reserve the right to suspend or terminate accounts that violate our terms.\n" +
                "If your account is terminated, you will lose access to your purchase history and stored data.\n" +
                "\n" +
                "•\tCHANGES TO TERMS\n" +
                "We may update these terms from time to time.\n" +
                " Continued use of the app after updates means you accept the new terms.\n" +
                "\n");

        btnAccept.setOnClickListener(v -> {
            // Save acceptance in SharedPreferences
            sharedPreferences.edit().putBoolean("TermsAccepted", true).apply();

            // Go back to SignUpActivity
            Intent intent = new Intent(TermsAndConditionsActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });

        btnDecline.setOnClickListener(v -> {
            sharedPreferences.edit().putBoolean("TermsAccepted", false).apply();
            finish();
        });
    }
}
