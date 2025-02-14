package adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ctonlinebookstore.OnboardingActivity;
import com.example.ctonlinebookstore.R;
import java.util.List;

import models.OnboardingItem;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.ViewHolder> {
    private final Context context;
    private final List<models.OnboardingItem> onboardingItems;

    public OnboardingAdapter(Context context, List<models.OnboardingItem> onboardingItems) {
        this.context = context;
        this.onboardingItems = onboardingItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_onboarding, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        models.OnboardingItem item = onboardingItems.get(position);
        holder.counter.setText(item.getCounterText());
        holder.image.setImageResource(item.getImageRes());
        holder.title.setText(item.getTitle());
        holder.subtitle.setText(item.getSubtitle());

        holder.skipButton.setOnClickListener(v -> ((OnboardingActivity) context).skipOnboarding(v));
        holder.nextButton.setOnClickListener(v -> ((OnboardingActivity) context).nextScreen(v));
    }

    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }

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
}
