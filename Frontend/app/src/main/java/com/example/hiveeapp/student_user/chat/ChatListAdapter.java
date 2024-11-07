package com.example.hiveeapp.student_user.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hiveeapp.R;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {
    private final List<ChatDto> chatList;
    private final OnChatClickListener onChatClickListener;
    private final Context context;

    public interface OnChatClickListener {
        void onChatClick(ChatDto chat);
    }

    public ChatListAdapter(List<ChatDto> chatList, OnChatClickListener onChatClickListener, Context context) {
        this.chatList = chatList;
        this.onChatClickListener = onChatClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the correct layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_list, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatDto chat = chatList.get(position);
        holder.jobTitleTextView.setText(chat.getJobTitle());

        // Optional: Display last message preview
        if (chat.getLastMessage() != null && !chat.getLastMessage().isEmpty()) {
            holder.lastMessageTextView.setText(chat.getLastMessage());
            holder.lastMessageTextView.setVisibility(View.VISIBLE);
        } else {
            holder.lastMessageTextView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> onChatClickListener.onChatClick(chat));
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitleTextView;
        TextView lastMessageTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitleTextView = itemView.findViewById(R.id.jobTitleTextView);
            lastMessageTextView = itemView.findViewById(R.id.lastMessageTextView);
        }
    }
}
