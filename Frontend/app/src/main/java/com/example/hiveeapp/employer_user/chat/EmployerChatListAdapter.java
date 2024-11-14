package com.example.hiveeapp.employer_user.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hiveeapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Adapter for displaying a list of chats in a RecyclerView for the employer user.
 */
public class EmployerChatListAdapter extends RecyclerView.Adapter<EmployerChatListAdapter.ChatViewHolder> {

    private final List<EmployerChatDto> chatList;
    private final OnChatClickListener onChatClickListener;
    private final Context context;

    /**
     * Interface for handling chat item click events.
     */
    public interface OnChatClickListener {
        /**
         * Called when a chat item is clicked.
         *
         * @param chat The chat object that was clicked.
         */
        void onChatClick(EmployerChatDto chat);
    }

    /**
     * Constructor for EmployerChatListAdapter.
     *
     * @param chatList            List of chat objects to display.
     * @param onChatClickListener Listener for chat item click events.
     * @param context             The context of the activity or fragment.
     */
    public EmployerChatListAdapter(List<EmployerChatDto> chatList, OnChatClickListener onChatClickListener, Context context) {
        this.chatList = chatList;
        this.onChatClickListener = onChatClickListener;
        this.context = context;
    }

    /**
     * Inflates the layout for each chat item in the RecyclerView.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The view type of the new View.
     * @return A new ChatViewHolder that holds the view for each chat item.
     */
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_employer, parent, false);
        return new ChatViewHolder(view);
    }

    /**
     * Binds data to the chat item view.
     *
     * @param holder   The ChatViewHolder.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        EmployerChatDto chat = chatList.get(position);
        holder.studentNameTextView.setText(chat.getStudentName());

        // Optional: Display last message preview
        if (chat.getLastMessage() != null && !chat.getLastMessage().isEmpty()) {
            holder.lastMessageTextView.setText(chat.getLastMessage());
            holder.lastMessageTextView.setVisibility(View.VISIBLE);
        } else {
            holder.lastMessageTextView.setVisibility(View.GONE);
        }

        if (chat.getLastMessageTime() != null && !chat.getLastMessageTime().isEmpty()) {
            holder.lastMessageTimeTextView.setText(formatTimestamp(chat.getLastMessageTime()));
            holder.lastMessageTimeTextView.setVisibility(View.VISIBLE);
        } else {
            holder.lastMessageTimeTextView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> onChatClickListener.onChatClick(chat));
    }

    /**
     * Returns the total number of chat items.
     *
     * @return The size of the chat list.
     */
    @Override
    public int getItemCount() {
        return chatList.size();
    }

    /**
     * ViewHolder class for chat items.
     */
    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView studentNameTextView;
        TextView lastMessageTextView;
        TextView lastMessageTimeTextView;

        /**
         * Constructor for ChatViewHolder.
         *
         * @param itemView The view of the chat item.
         */
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTextView = itemView.findViewById(R.id.studentNameTextView);
            lastMessageTextView = itemView.findViewById(R.id.lastMessageTextView);
            lastMessageTimeTextView = itemView.findViewById(R.id.lastMessageTimeTextView);
        }
    }

    /**
     * Formats a timestamp from ISO format to a user-friendly format.
     *
     * @param timestamp The ISO 8601 timestamp string (e.g., "2024-11-14T10:30:00Z").
     * @return The formatted timestamp (e.g., "Nov 14, 10:30 AM"), or an empty string if parsing fails.
     */
    private String formatTimestamp(String timestamp) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = isoFormat.parse(timestamp);

            SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault());
            displayFormat.setTimeZone(TimeZone.getDefault());
            return displayFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
