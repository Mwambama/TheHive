package com.example.hiveeapp.websocket.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiveeapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private final List<Message> messages;
    private final int currentUserId;

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

        // Format and set the timestamp
        String formattedTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date(message.getTimestamp()));
        holder.timestampTextView.setText(formattedTime);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.messageTextView.getLayoutParams();
        if (message.isSentByUser()) {
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            holder.messageTextView.setBackgroundResource(R.drawable.background_sent_message);
        } else {
            params.addRule(RelativeLayout.ALIGN_PARENT_START);
            holder.messageTextView.setBackgroundResource(R.drawable.background_received_message);
        }

        holder.messageTextView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView timestampTextView;

        MessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
        }
    }
}
