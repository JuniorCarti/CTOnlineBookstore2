package com.example.ctonlinebookstore;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private TextView tvGreeting;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        tvGreeting = findViewById(R.id.tvGreeting);

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            // Get userId of the currently logged-in user
            String userId = firebaseUser.getUid();
            fetchUserName(userId);
        }
    }

    private void fetchUserName(String userId) {
        usersRef.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Get user data
                String name = task.getResult().child("name").getValue(String.class);
                if (name != null) {
                    displayGreeting(name);
                }
            } else {
                Toast.makeText(MainActivity.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayGreeting(String name) {
        // Get current hour
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String greeting = "Good ";

        // Set greeting based on the time of day
        if (hour < 12) {
            greeting += "Morning";
        } else if (hour < 17) {
            greeting += "Afternoon";
        } else {
            greeting += "Evening";
        }

        // Set personalized greeting
        tvGreeting.setText(greeting + ", " + name + "!");
    }
}
