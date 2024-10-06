package com.example.hiveeapp.company_user.handleEmployers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiveeapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmployerAdapter extends RecyclerView.Adapter<EmployerAdapter.EmployerViewHolder> {

    private JSONArray employers = new JSONArray();
    private Context context;
    private boolean isEditable;

    public EmployerAdapter(Context context, boolean isEditable) {
        this.context = context;
        this.isEditable = isEditable;
    }

    public void setEmployers(JSONArray employers) {
        this.employers = employers;
        notifyDataSetChanged();
    }

    @Override
    public EmployerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_employer, parent, false);
        return new EmployerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployerViewHolder holder, int position) {
        try {
            JSONObject employer = employers.getJSONObject(position);
            String name = employer.getString("name");
            String email = employer.getString("email");
            String phone = employer.getString("phone");

            JSONObject addressJson = employer.getJSONObject("address");
            String address = addressJson.getString("street") + ", " +
                    addressJson.getString("city") + ", " +
                    addressJson.getString("state") + " " +
                    addressJson.getString("zip_code");

            holder.nameTextView.setText(name);
            holder.emailTextView.setText(email);
            holder.phoneTextView.setText(phone);
            holder.addressTextView.setText(address);

            // If it's in read-only mode, hide update and delete buttons
            if (!isEditable) {
                holder.updateButton.setVisibility(View.GONE);
                holder.deleteButton.setVisibility(View.GONE);
            } else {
                // Set up click listeners for update and delete buttons if editable
                holder.updateButton.setOnClickListener(v -> {
                    // Implement update functionality
                });

                holder.deleteButton.setOnClickListener(v -> {
                    // Implement delete functionality
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return employers.length();
    }

    class EmployerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, emailTextView, phoneTextView, addressTextView;
        ImageButton updateButton, deleteButton;

        public EmployerViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}