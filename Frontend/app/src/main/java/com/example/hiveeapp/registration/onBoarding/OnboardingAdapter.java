package com.example.hiveeapp.onboarding;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hiveeapp.MainActivity;
import com.example.hiveeapp.R;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {

    // private final int[] images = {R.drawable.onboarding1, R.drawable.onboarding2, R.drawable.onboarding3};
    private final String[] titles = {"Welcome to the Hive", "Fast-Paced Job Application", "Let’s Get Started!"};
    private final String[] descriptions = {
            "Swipe right to apply for jobs faster and connect with employers instantly.",
            "Find your next job easily with a Tinder-style swipe feature.",
            "Get started on your journey towards a new career!"
    };

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding, parent, false);
        return new OnboardingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        // holder.imageView.setImageResource(images[position]);
        holder.title.setText(titles[position]);
        holder.description.setText(descriptions[position]);

        // Handle the "Get Started" button on the last page
        if (position == titles.length - 1) {
            holder.getStartedButton.setVisibility(View.VISIBLE);
            holder.getStartedButton.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), MainActivity.class);
                holder.itemView.getContext().startActivity(intent);
            });
        } else {
            holder.getStartedButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    static class OnboardingViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView description;
        View getStartedButton;

        OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.onboardingImage);
            title = itemView.findViewById(R.id.onboardingTitle);
            description = itemView.findViewById(R.id.onboardingDescription);
            getStartedButton = itemView.findViewById(R.id.getStartedButton);
        }
    }
}
