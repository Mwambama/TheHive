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

public class EmployerChatListAdapter extends RecyclerView.Adapter<EmployerChatListAdapter.ChatViewHolder> {

    private final List<EmployerChatDto> chatList;
    private final OnChatClickListener onChatClickListener;
    private final Context context;

    public interface OnChatClickListener {
        void onChatClick(EmployerChatDto chat);
    }

    public EmployerChatListAdapter(List<EmployerChatDto> chatList, OnChatClickListener onChatClickListener, Context context) {
        this.chatList = chatList;
        this.onChatClickListener = onChatClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_employer, parent, false);
        return new ChatViewHolder(view);
    }

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

        // Optional: Display last message time
        if (chat.getLastMessageTime() != null && !chat.getLastMessageTime().isEmpty()) {
            holder.lastMessageTimeTextView.setText(formatTimestamp(chat.getLastMessageTime()));
            holder.lastMessageTimeTextView.setVisibility(View.VISIBLE);
        } else {
            holder.lastMessageTimeTextView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> onChatClickListener.onChatClick(chat));
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView studentNameTextView;
        TextView lastMessageTextView;
        TextView lastMessageTimeTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTextView = itemView.findViewById(R.id.studentNameTextView);
            lastMessageTextView = itemView.findViewById(R.id.lastMessageTextView);
            lastMessageTimeTextView = itemView.findViewById(R.id.lastMessageTimeTextView);
        }
    }

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
