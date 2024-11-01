package com.example.hiveeapp.student_user.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.setting.StudentApi;
import com.example.hiveeapp.student_user.swipe.JobPosting;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatDto chat = chatList.get(position);

        // Assuming you have a way to get the job title based on employerId from JobPosting
        StudentApi.getJobPostingsByEmployerId(context, chat.getEmployerId(),
                jobPostings -> {
                    if (!jobPostings.isEmpty()) {
                        // Display the title of the first job found for this employer
                        JobPosting job = jobPostings.get(0);
                        holder.jobTitleTextView.setText(job.getTitle());
                    }
                },
                error -> {
                    holder.jobTitleTextView.setText("Job Title Not Available");
                });

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
