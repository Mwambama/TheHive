package com.example.hiveeapp.employer_user.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hiveeapp.R;

import java.util.List;

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        EmployerChatDto chat = chatList.get(position);
        holder.jobTitleTextView.setText(chat.getJobTitle());
        holder.itemView.setOnClickListener(v -> onChatClickListener.onChatClick(chat));
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitleTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitleTextView = itemView.findViewById(R.id.jobTitleTextView);
        }
    }
}
