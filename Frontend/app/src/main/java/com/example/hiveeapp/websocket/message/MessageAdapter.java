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

    // Variable to track the selected message ID
    private int selectedMessageId = -1;

    // Interface for click events
    public interface OnMessageClickListener {
        void onMessageClick(Message message);
        void onMessageUnselected();
    }

    // Setter for the click listener
    public void setOnMessageClickListener(OnMessageClickListener listener) {
        this.onMessageClickListener = listener;
    }

    public MessageAdapter(List<Message> messages, int currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    // Method to set the selected message ID
    public void setSelectedMessageId(int messageId) {
        this.selectedMessageId = messageId;
        notifyDataSetChanged(); // Refresh the list to apply visual changes
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

        // Check if the current message is selected
        boolean isSelected = message.getMessageId() == selectedMessageId;

        if (message.isSentByUser()) {
            // Show sent message container and hide received container
            holder.sentMessageContainer.setVisibility(View.VISIBLE);
            holder.receivedMessageContainer.setVisibility(View.GONE);

            holder.sentMessageTextView.setText(message.getText());
            holder.sentTimestampTextView.setText(formattedTime);

            // Apply visual indication if selected
            holder.sentMessageContainer.setBackgroundResource(isSelected ?
                    R.drawable.bg_message_sent_selected : R.drawable.bg_message_sent);

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

            // Set click listener
            holder.sentMessageContainer.setOnClickListener(v -> {
                if (onMessageClickListener != null) {
                    if (selectedMessageId == message.getMessageId()) {
                        // Message is already selected, unselect it
                        onMessageClickListener.onMessageUnselected();
                        setSelectedMessageId(-1);
                    } else {
                        onMessageClickListener.onMessageClick(message);
                        setSelectedMessageId(message.getMessageId());
                    }
                }
            });

        } else {
            // Show received message container and hide sent container
            holder.receivedMessageContainer.setVisibility(View.VISIBLE);
            holder.sentMessageContainer.setVisibility(View.GONE);

            holder.receivedMessageTextView.setText(message.getText());
            holder.receivedTimestampTextView.setText(formattedTime);

            // Apply visual indication if selected
            holder.receivedMessageContainer.setBackgroundResource(isSelected ?
                    R.drawable.bg_message_received_selected : R.drawable.bg_message_received);

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

            // Set click listener
            holder.receivedMessageContainer.setOnClickListener(v -> {
                if (onMessageClickListener != null) {
                    if (selectedMessageId == message.getMessageId()) {
                        // Message is already selected, unselect it
                        onMessageClickListener.onMessageUnselected();
                        setSelectedMessageId(-1);
                    } else {
                        onMessageClickListener.onMessageClick(message);
                        setSelectedMessageId(message.getMessageId());
                    }
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
