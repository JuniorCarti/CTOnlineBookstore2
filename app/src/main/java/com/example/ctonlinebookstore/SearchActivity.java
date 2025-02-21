package com.example.ctonlinebookstore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import android.widget.ProgressBar;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import adapters.ProductAdapter;
import models.Product;

public class SearchActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private TextView textNoResults;
    private FirebaseFirestore db;
    private ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        progressBar = findViewById(R.id.progressBar);  // Initialize the ProgressBar

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        recyclerView = findViewById(R.id.recyclerViewProducts);
        textNoResults = findViewById(R.id.textNoResults);

        // Set RecyclerView to use GridLayout with 2 columns
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Initialize product list
        productList = new ArrayList<>();

        // Show the ProgressBar while fetching data
        progressBar.setVisibility(View.VISIBLE);

        // Fetch products from Firestore categories
        fetchProductsFromFirestore();
    }

    private void fetchProductsFromFirestore() {
        fetchCategoryProducts("Drawing Books and Music Books");
        fetchCategoryProducts("Exercise Books");
        fetchCategoryProducts("Ledger Books");
    }

    private void fetchCategoryProducts(String categoryName) {
        CollectionReference productsRef = db.collection(categoryName);

        productsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot documents = task.getResult();
                if (!documents.isEmpty()) {
                    Log.d("Firestore", "Fetched " + documents.size() + " products from " + categoryName);
                    for (DocumentSnapshot doc : documents) {
                        Log.d("Firestore", "Product Data: " + doc.getData());

                        // Retrieve Firestore fields (All categories now share the same fields)
                        String name = doc.getString("name");
                        double price = doc.contains("price") ? doc.getDouble("price") : 0.0;
                        String category = doc.getString("category");
                        String imageUrl = doc.getString("imageUrl");

                        long stockQuantity = doc.contains("stockQuantity") ? doc.getLong("stockQuantity") : 0;

                        Product product = new Product(
                                doc.getId(), category, name, price, imageUrl,
                                (int) stockQuantity // Convert long to int safely
                        );

                        productList.add(product);
                    }

                    // Update RecyclerView Adapter
                    if (productAdapter == null) {
                        productAdapter = new ProductAdapter(SearchActivity.this, productList, SearchActivity.this);
                        recyclerView.setAdapter(productAdapter);
                    } else {
                        productAdapter.notifyDataSetChanged();
                    }

                    // Check if list is empty
                    checkIfListEmpty();
                } else {
                    Log.e("Firestore", "No products found in " + categoryName);
                    checkIfListEmpty();
                }
            } else {
                Toast.makeText(this, "Failed to fetch " + categoryName + " products", Toast.LENGTH_SHORT).show();
                Log.e("Firestore", "Error fetching " + categoryName + " products", task.getException());
            }

            // Hide the ProgressBar after the data is fetched
            progressBar.setVisibility(View.GONE);
        });
    }

    private void checkIfListEmpty() {
        if (productList.isEmpty()) {
            textNoResults.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textNoResults.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onProductClick(Product product) {
        Toast.makeText(this, "Clicked: " + product.getTitle(), Toast.LENGTH_SHORT).show();

        // Navigate to ProductDetailsActivity
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("title", product.getTitle());
        intent.putExtra("description", product.getDescription());
        intent.putExtra("price", product.getPrice());
        intent.putExtra("imageUrl", product.getImageUrl());
        intent.putExtra("rating", product.getRating());
        intent.putExtra("peopleRated", product.getPeopleRated());
        startActivity(intent);
    }
}
