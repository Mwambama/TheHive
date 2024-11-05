package com.example.hiveeapp.employer_user.chat.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiveeapp.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private final List<Message> messages;
    private final int currentUserId;

    // Updated constructor to accept `userId`
    public MessageAdapter(List<Message> messages, int currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.messageTextView.setText(message.getText());

        // Align message based on sender
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.messageTextView.getLayoutParams();
        if (message.getSenderId() == currentUserId) {
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            holder.messageTextView.setBackgroundResource(R.drawable.bg_message_employer);
        } else {
            params.addRule(RelativeLayout.ALIGN_PARENT_START);
            holder.messageTextView.setBackgroundResource(R.drawable.bg_message_student);
        }

        holder.messageTextView.setLayoutParams(params); // Apply the layout parameters
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        MessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }
    }
}
