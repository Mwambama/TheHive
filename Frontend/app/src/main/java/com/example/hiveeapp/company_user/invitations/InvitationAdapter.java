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

/**
 * InvitationAdapter handles the display of invitations in a RecyclerView.
 * It supports both editable and non-editable modes, allowing users to view, update, or delete invitations.
 */
public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.InvitationViewHolder> {

    private JSONArray invitations = new JSONArray();  // List of invitations
    private final Context context;                     // Activity/Fragment context
    private final boolean isEditable;                  // Editable flag for showing update/delete buttons
    private static final String TAG = "InvitationAdapter"; // Tag for logging

    /**
     * Constructor for the InvitationAdapter.
     *
     * @param context    The context of the activity/fragment.
     * @param isEditable Flag indicating if the invitations are editable.
     */
    public InvitationAdapter(Context context, boolean isEditable) {
        this.context = context;
        this.isEditable = isEditable;
    }

    /**
     * Sets the list of invitations and refreshes the RecyclerView.
     * The invitations are displayed in reverse order (newest first).
     *
     * @param invitations The JSONArray of invitation data.
     */
    public void setInvitations(JSONArray invitations) {
        this.invitations = reverseJSONArray(invitations);  // Reverse the order of invitations
        notifyDataSetChanged();  // Notify the adapter that the data has changed
    }

    /**
     * Reverses the order of the given JSONArray.
     *
     * @param array The JSONArray to be reversed.
     * @return A new JSONArray in reversed order.
     */
    private JSONArray reverseJSONArray(JSONArray array) {
        JSONArray reversedArray = new JSONArray();
        for (int i = array.length() - 1; i >= 0; i--) {
            try {
                reversedArray.put(array.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return reversedArray;
    }

    @Override
    public InvitationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item view for each invitation
        View view = LayoutInflater.from(context).inflate(R.layout.item_invitation, parent, false);
        return new InvitationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InvitationViewHolder holder, int position) {
        try {
            // Get the current invitation object
            JSONObject invitation = invitations.getJSONObject(position);

            Log.d(TAG, "Invitation JSON: " + invitation.toString());

            // Extract the invitation ID
            int invitationId = invitation.optInt("employerInvitationId", -1);
            if (invitationId == -1) {
                Log.e(TAG, "employerInvitationId not found in the JSON object.");
                return;
            }

            // Extract email and message details
            String email = invitation.optString("email", "No email");
            String message = invitation.optString("message", "No message");

            // Set the invitation details to the corresponding views
            holder.emailTextView.setText(email);
            holder.messageTextView.setText(message);

            // If not editable, hide the update and delete buttons
            if (!isEditable) {
                holder.updateButton.setVisibility(View.GONE);
                holder.deleteButton.setVisibility(View.GONE);
            } else {
                // Handle the update button click
                holder.updateButton.setOnClickListener(v -> {
                    Intent intent = new Intent(context, InvitationCreationActivity.class);
                    intent.putExtra("invitationId", invitationId);
                    intent.putExtra("email", email);
                    intent.putExtra("message", message);
                    context.startActivity(intent);  // Navigate to the InvitationCreationActivity
                });

                // Handle the delete button click
                holder.deleteButton.setOnClickListener(v -> {
                    new androidx.appcompat.app.AlertDialog.Builder(context)
                            .setTitle("Delete Invitation")
                            .setMessage("Are you sure you want to delete this invitation?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                // Call the API to delete the invitation
                                InvitationApi.deleteInvitation(context, invitationId, response -> {
                                    invitations.remove(position);  // Remove the invitation from the list
                                    notifyItemRemoved(position);   // Notify that the item was removed
                                    notifyItemRangeChanged(position, invitations.length());  // Update the range
                                }, error -> {
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
        return invitations.length();  // Return the total number of invitations
    }

    /**
     * ViewHolder class for holding views related to each invitation item.
     */
    class InvitationViewHolder extends RecyclerView.ViewHolder {
        TextView emailTextView, messageTextView;
        ImageButton updateButton, deleteButton;

        public InvitationViewHolder(View itemView) {
            super(itemView);
            // Initialize views for email, message, update, and delete buttons
            emailTextView = itemView.findViewById(R.id.emailTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}