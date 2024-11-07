package com.example.hiveeapp.websocket.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

    private OnMessageClickListener onMessageClickListener;

    // Interface for click events
    public interface OnMessageClickListener {
        void onMessageClick(Message message);
    }

    // Setter for the click listener
    public void setOnMessageClickListener(OnMessageClickListener listener) {
        this.onMessageClickListener = listener;
    }

    public MessageAdapter(List<Message> messages, int currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ensure you're inflating the correct layout
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

            // Display replied message if exists
            if (message.getReplyToId() != null) {
                Message originalMessage = findMessageById(message.getReplyToId());
                if (originalMessage != null) {
                    holder.sentReplyTextView.setText("Replying to: " + originalMessage.getText());
                    holder.sentReplyTextView.setVisibility(View.VISIBLE);
                } else {
                    holder.sentReplyTextView.setVisibility(View.GONE);
                }
            } else {
                holder.sentReplyTextView.setVisibility(View.GONE);
            }

            // Set click listener on the sent message container
            holder.sentMessageContainer.setOnClickListener(v -> {
                if (onMessageClickListener != null) {
                    onMessageClickListener.onMessageClick(message);
                }
            });
        } else {
            // Show received message container and hide sent container
            holder.receivedMessageContainer.setVisibility(View.VISIBLE);
            holder.sentMessageContainer.setVisibility(View.GONE);

            holder.receivedMessageTextView.setText(message.getText());
            holder.receivedTimestampTextView.setText(formattedTime);

            // Display replied message if exists
            if (message.getReplyToId() != null) {
                Message originalMessage = findMessageById(message.getReplyToId());
                if (originalMessage != null) {
                    holder.receivedReplyTextView.setText("Replying to: " + originalMessage.getText());
                    holder.receivedReplyTextView.setVisibility(View.VISIBLE);
                } else {
                    holder.receivedReplyTextView.setVisibility(View.GONE);
                }
            } else {
                holder.receivedReplyTextView.setVisibility(View.GONE);
            }

            // Set click listener on the received message container
            holder.receivedMessageContainer.setOnClickListener(v -> {
                if (onMessageClickListener != null) {
                    onMessageClickListener.onMessageClick(message);
                }
            });
        }
    }

    // Helper method to find a message by its ID
    private Message findMessageById(int messageId) {
        for (Message msg : messages) {
            if (msg.getMessageId() == messageId) {
                return msg;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout sentMessageContainer, receivedMessageContainer;
        TextView sentMessageTextView, receivedMessageTextView;
        TextView sentTimestampTextView, receivedTimestampTextView;
        TextView sentReplyTextView, receivedReplyTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views with correct IDs
            sentMessageContainer = itemView.findViewById(R.id.sentMessageContainer);
            receivedMessageContainer = itemView.findViewById(R.id.receivedMessageContainer);
            sentMessageTextView = itemView.findViewById(R.id.sentMessageTextView);
            receivedMessageTextView = itemView.findViewById(R.id.receivedMessageTextView);
            sentTimestampTextView = itemView.findViewById(R.id.sentTimestampTextView);
            receivedTimestampTextView = itemView.findViewById(R.id.receivedTimestampTextView);
            sentReplyTextView = itemView.findViewById(R.id.sentReplyTextView);
            receivedReplyTextView = itemView.findViewById(R.id.receivedReplyTextView);
        }
    }
}
