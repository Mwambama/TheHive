
package com.example.hiveeapp.student_user.chat.message;

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
    private final int currentUserId;  // The ID of the current user

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
        holder.timestampTextView.setText(message.getTimestamp());  // Set the timestamp directly

        // Align message based on sender
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.messageTextView.getLayoutParams();
        if (message.getSenderId() == currentUserId) {
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            holder.messageTextView.setBackgroundResource(R.drawable.background_sent_message);
        } else {
            params.addRule(RelativeLayout.ALIGN_PARENT_START);
            holder.messageTextView.setBackgroundResource(R.drawable.background_received_message);
        }

        holder.messageTextView.setLayoutParams(params); // Apply the layout parameters
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView timestampTextView;  // Add reference to timestamp TextView

        MessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);  // Initialize the timestamp TextView
        }
    }
}









//
//package com.example.hiveeapp.student_user.chat.message;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.hiveeapp.R;
//
//import java.text.SimpleDateFormat;
//import java.util.List;
//import java.util.Locale;
//
//public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
//    private final List<Message> messages;
//    private final int currentUserId;  // The ID of the current user
//
//    // Updated constructor to accept `userId`
//    public MessageAdapter(List<Message> messages, int currentUserId) {
//        this.messages = messages;
//        this.currentUserId = currentUserId;
//    }
//
//    @NonNull
//    @Override
//    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_message, parent, false);
//        return new MessageViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
//        Message message = messages.get(position);
//        holder.messageTextView.setText(message.getText());
//        holder.timestampTextView.setText(formatTimestamp(message.getTimestamp()));  // Set the formatted timestamp
//
//        // Align message based on sender
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.messageTextView.getLayoutParams();
//        if (message.getSenderId() == currentUserId) {
//            params.addRule(RelativeLayout.ALIGN_PARENT_END);
//            holder.messageTextView.setBackgroundResource(R.drawable.background_sent_message);
//        } else {
//            params.addRule(RelativeLayout.ALIGN_PARENT_START);
//            holder.messageTextView.setBackgroundResource(R.drawable.background_received_message);
//        }
//
//        holder.messageTextView.setLayoutParams(params); // Apply the layout parameters
//    }
//
//    @Override
//    public int getItemCount() {
//        return messages.size();
//    }
//
//    // Helper method to format the timestamp
//    private String formatTimestamp(long timestamp) {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
//        return sdf.format(timestamp);
//    }
//
//    static class MessageViewHolder extends RecyclerView.ViewHolder {
//        TextView messageTextView;
//        TextView timestampTextView;  // Add reference to timestamp TextView
//
//        MessageViewHolder(View itemView) {
//            super(itemView);
//            messageTextView = itemView.findViewById(R.id.messageTextView);
//            timestampTextView = itemView.findViewById(R.id.timestampTextView);  // Initialize the timestamp TextView
//        }
//    }
//}
//






//package com.example.hiveeapp.student_user.chat.message;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.hiveeapp.R;
//
//import java.util.List;
//
//public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
//    private final List<Message> messages;
//    private final int currentUserId;  // The ID of the current user
//
//    // Updated constructor to accept `userId`
//    public MessageAdapter(List<Message> messages, int currentUserId) {
//        this.messages = messages;
//        this.currentUserId = currentUserId;
//    }
//
//    @NonNull
//    @Override
//    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_message, parent, false);
//        return new MessageViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
//        Message message = messages.get(position);
//        holder.messageTextView.setText(message.getText());
//
//        // Align message based on sender
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.messageTextView.getLayoutParams();
//        if (message.getSenderId() == currentUserId) {
//            params.addRule(RelativeLayout.ALIGN_PARENT_END);
//            holder.messageTextView.setBackgroundResource(R.drawable.background_sent_message);
//        } else {
//            params.addRule(RelativeLayout.ALIGN_PARENT_START);
//            holder.messageTextView.setBackgroundResource(R.drawable.background_received_message);
//        }
//
//        holder.messageTextView.setLayoutParams(params); // Apply the layout parameters
//    }
//
//    @Override
//    public int getItemCount() {
//        return messages.size();
//    }
//
//    static class MessageViewHolder extends RecyclerView.ViewHolder {
//        TextView messageTextView;
//
//        MessageViewHolder(View itemView) {
//            super(itemView);
//            messageTextView = itemView.findViewById(R.id.messageTextView);
//        }
//    }
//}
