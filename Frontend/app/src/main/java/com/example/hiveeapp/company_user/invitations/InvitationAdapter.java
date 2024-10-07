package com.example.hiveeapp.company_user.invitations;

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

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.InvitationViewHolder> {

    private JSONArray invitations = new JSONArray();
    private Context context;
    private boolean isEditable;

    public InvitationAdapter(Context context, boolean isEditable) {
        this.context = context;
        this.isEditable = isEditable;
    }

    public void setInvitations(JSONArray invitations) {
        this.invitations = invitations;
        notifyDataSetChanged();
    }

    @Override
    public InvitationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_invitation, parent, false);
        return new InvitationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InvitationViewHolder holder, int position) {
        try {
            JSONObject invitation = invitations.getJSONObject(position);
            String email = invitation.getString("email");
            String message = invitation.optString("message", "No message");

            holder.emailTextView.setText(email);  // Resolving setText issue
            holder.messageTextView.setText(message);

            // Hide buttons if the list is in read-only mode
            if (!isEditable) {
                holder.updateButton.setVisibility(View.GONE);  // Resolving setVisibility issue
                holder.deleteButton.setVisibility(View.GONE);
            } else {
                // Set up click listeners for update and delete buttons if editable
                holder.updateButton.setOnClickListener(v -> {
                    // Handle update action (not necessary for read-only mode)
                });

                holder.deleteButton.setOnClickListener(v -> {
                    // Handle delete action (not necessary for read-only mode)
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return invitations.length();
    }

    class InvitationViewHolder extends RecyclerView.ViewHolder {
        TextView emailTextView, messageTextView;
        ImageButton updateButton, deleteButton;

        public InvitationViewHolder(View itemView) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.emailTextView);  // Resolving findViewById issues
            messageTextView = itemView.findViewById(R.id.messageTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}