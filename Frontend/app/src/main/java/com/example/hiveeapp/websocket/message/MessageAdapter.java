package com.example.hiveeapp.websocket.message;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

        String formattedTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date(message.getTimestamp()));

        if (message.isSentByUser()) {
            // Show sent message container and hide received container
            holder.sentMessageContainer.setVisibility(View.VISIBLE);
            holder.receivedMessageContainer.setVisibility(View.GONE);

            holder.sentMessageTextView.setText(message.getText());
            holder.sentTimestampTextView.setText(formattedTime);
        } else {
            // Show received message container and hide sent container
            holder.receivedMessageContainer.setVisibility(View.VISIBLE);
            holder.sentMessageContainer.setVisibility(View.GONE);

            holder.receivedMessageTextView.setText(message.getText());
            holder.receivedTimestampTextView.setText(formattedTime);
        }
    }




    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout sentMessageContainer, receivedMessageContainer;
        TextView sentMessageTextView, receivedMessageTextView;
        TextView sentTimestampTextView, receivedTimestampTextView;

        MessageViewHolder(View itemView) {
            super(itemView);
            sentMessageContainer = itemView.findViewById(R.id.sentMessageContainer);
            receivedMessageContainer = itemView.findViewById(R.id.receivedMessageContainer);
            sentMessageTextView = itemView.findViewById(R.id.sentMessageTextView);
            receivedMessageTextView = itemView.findViewById(R.id.receivedMessageTextView);
            sentTimestampTextView = itemView.findViewById(R.id.sentTimestampTextView);
            receivedTimestampTextView = itemView.findViewById(R.id.receivedTimestampTextView);
        }
    }
}