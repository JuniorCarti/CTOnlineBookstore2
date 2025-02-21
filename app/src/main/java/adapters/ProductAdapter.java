package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ctonlinebookstore.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;
    private List<Product> productList;
    private final OnProductClickListener listener;
    private final NumberFormat currencyFormat;

    // Constructor
    public ProductAdapter(Context context, List<Product> productList, OnProductClickListener listener) {
        this.context = context;
        this.productList = (productList != null) ? new ArrayList<>(productList) : new ArrayList<>();
        this.listener = listener;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "KE")); // Kenyan currency format
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Load product image using Glide
        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_colours) // Placeholder while loading
                .error(R.drawable.ic_colours) // Fallback on error
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache optimization
                .centerCrop()
                .into(holder.imageProduct);

        // Set product details
        holder.textProductName.setText(product.getTitle() != null ? product.getTitle() : "No Title");
        holder.textProductPrice.setText(currencyFormat.format(product.getPrice())); // Format price as "Ksh 1,000.00"

        // Handle product description visibility
        if (product.getDescription() != null && !product.getDescription().isEmpty()) {
            holder.textProductDescription.setVisibility(View.VISIBLE);
            holder.textProductDescription.setText(product.getDescription());
        } else {
            holder.textProductDescription.setVisibility(View.GONE);
        }

        // Handle rating visibility
        if (product.getRating() > 0) {
            holder.layoutRating.setVisibility(View.VISIBLE);
            holder.ratingProduct.setRating(product.getRating());
            holder.textPeopleRated.setText("(" + product.getPeopleRated() + ")");
        } else {
            holder.layoutRating.setVisibility(View.GONE);
        }

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // ViewHolder Class
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageProduct;
        final TextView textProductName, textProductDescription, textProductPrice, textPeopleRated;
        final RatingBar ratingProduct;
        final LinearLayout layoutRating;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            textProductName = itemView.findViewById(R.id.textProductName);
            textProductDescription = itemView.findViewById(R.id.textProductDescription);
            textProductPrice = itemView.findViewById(R.id.textProductPrice);
            ratingProduct = itemView.findViewById(R.id.ratingProduct);
            textPeopleRated = itemView.findViewById(R.id.textPeopleRated);
            layoutRating = itemView.findViewById(R.id.layoutRating);
        }
    }

    // Interface for Click Listener
    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    // Optimized method to update the product list using DiffUtil
    public void updateProductList(List<Product> newList) {
        if (newList == null) newList = new ArrayList<>();

        List<Product> finalNewList = newList;
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return productList.size();
            }

            @Override
            public int getNewListSize() {
                return finalNewList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return Objects.equals(productList.get(oldItemPosition).getId(), finalNewList.get(newItemPosition).getId());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return productList.get(oldItemPosition).equals(finalNewList.get(newItemPosition));
            }
        });

        productList = new ArrayList<>(newList); // Ensure thread safety
        diffResult.dispatchUpdatesTo(this); // Efficient UI updates
    }
}
