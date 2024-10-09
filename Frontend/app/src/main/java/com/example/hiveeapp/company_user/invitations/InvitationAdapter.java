package com.example.hiveeapp.company_user.invitations;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
    private static final String TAG = "InvitationAdapter";

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

            Log.d(TAG, "Invitation JSON: " + invitation.toString());

            // Extract the invitation ID using the correct key
            int invitationId = invitation.optInt("employerInvitationId", -1);
            if (invitationId == -1) {
                Log.e(TAG, "employerInvitationId not found in the JSON object.");
                return;
            }

            // Extract the email
            String email = invitation.optString("email", "No email");

            // Extract the message (if available)
            String message = invitation.optString("message", "No message");

            holder.emailTextView.setText(email);
            holder.messageTextView.setText(message);

            // Hide buttons if the list is in read-only mode
            if (!isEditable) {
                holder.updateButton.setVisibility(View.GONE);
                holder.deleteButton.setVisibility(View.GONE);
            } else {
                // Set up click listeners for update and delete buttons if editable
                holder.updateButton.setOnClickListener(v -> {
                    // Handle update action
                    Intent intent = new Intent(context, InvitationCreationActivity.class);
                    intent.putExtra("invitationId", invitationId);
                    intent.putExtra("email", email);
                    intent.putExtra("message", message);
                    context.startActivity(intent);
                });

                holder.deleteButton.setOnClickListener(v -> {
                    // Handle delete action
                    // Prompt user for confirmation
                    new androidx.appcompat.app.AlertDialog.Builder(context)
                            .setTitle("Delete Invitation")
                            .setMessage("Are you sure you want to delete this invitation?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                // Call the API to delete the invitation
                                InvitationApi.deleteInvitation(context, invitationId, response -> {
                                    // Remove the item from the list and notify the adapter
                                    invitations.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, invitations.length());
                                }, error -> {
                                    // Handle error
                                    String errorMsg = error.getMessage();
                                    android.widget.Toast.makeText(context, "Error deleting invitation: " + errorMsg, android.widget.Toast.LENGTH_SHORT).show();
                                });
                            })
                            .setNegativeButton("No", null)
                            .show();
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error parsing invitation JSON: " + e.getMessage());
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
            emailTextView = itemView.findViewById(R.id.emailTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}