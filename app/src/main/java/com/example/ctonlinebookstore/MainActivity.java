package com.example.ctonlinebookstore;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton wishlistBtn, cartBtn, searchBtn, settingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // buttons init process
        wishlistBtn = findViewById(R.id.wishlistButton);
        cartBtn = findViewById(R.id.cartButton);
        searchBtn = findViewById(R.id.searchButton);
        settingsBtn = findViewById(R.id.settingsButton);

        // button click event
        wishlistBtn.setOnClickListener(this);
        cartBtn.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
        settingsBtn.setOnClickListener(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // load default fragment process
        loadFragment(new WishlistFragment());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        
        try {
            if (id == R.id.wishlistButton) {
                loadFragment(new WishlistFragment());
            } else if (id == R.id.cartButton) {
                loadFragment(new CartFragment());
            } else if (id == R.id.searchButton) {
                loadFragment(new SearchFragment());
            } else if (id == R.id.settingsButton) {
                loadFragment(new SettingsFragment());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFragment(Fragment fragment) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}