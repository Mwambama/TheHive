package com.example.hiveeapp.employer_user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hiveeapp.R;
import java.util.List;

public class TrackingAdapter extends RecyclerView.Adapter<TrackingAdapter.ViewHolder> {

    private List<String> trackingItems; // Replace String with your data model

    public TrackingAdapter(List<String> trackingItems) {
        this.trackingItems = trackingItems;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracking_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = trackingItems.get(position);
        holder.textView.setText(item);
    }

    @Override
    public int getItemCount() {
        return trackingItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tracking_item_text); // Ensure you have this ID in tracking_item.xml
        }
    }
}

