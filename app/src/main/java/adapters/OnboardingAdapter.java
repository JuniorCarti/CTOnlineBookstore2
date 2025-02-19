package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ctonlinebookstore.R;
import java.util.List;
import models.OnboardingItem;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.ViewHolder> {
    private final List<OnboardingItem> onboardingItems;
    private final OnboardingNavigationListener navigationListener;

    // 🔹 Constructor
    public OnboardingAdapter(List<OnboardingItem> onboardingItems, OnboardingNavigationListener navigationListener) {
        this.onboardingItems = onboardingItems;
        this.navigationListener = navigationListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OnboardingItem item = onboardingItems.get(position);

        // 🔹 Set values dynamically
        holder.counter.setText(item.getCounterText());
        holder.image.setImageResource(item.getImageRes());
        holder.title.setText(item.getTitle());
        holder.subtitle.setText(item.getSubtitle());

        // 🔹 Handle button clicks using interface
        holder.skipButton.setOnClickListener(v -> navigationListener.onSkip());
        holder.nextButton.setOnClickListener(v -> navigationListener.onNext(position));
    }

    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }

    // 🔹 ViewHolder class
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView counter, title, subtitle, skipButton, nextButton;
        ImageView image;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            counter = itemView.findViewById(R.id.counter);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            image = itemView.findViewById(R.id.image);
            skipButton = itemView.findViewById(R.id.skipButton);
            nextButton = itemView.findViewById(R.id.nextButton);
        }
    }

    // 🔹 Interface for handling navigation actions
    public interface OnboardingNavigationListener {
        void onSkip();
        void onNext(int position);
    }
}
