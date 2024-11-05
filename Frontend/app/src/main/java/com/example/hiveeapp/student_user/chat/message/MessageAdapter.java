package com.example.hiveeapp.student_user.chat.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hiveeapp.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.messageTextView.setText(message.getText());

        // Align message to the left or right based on sender
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.messageTextView.getLayoutParams();
        if (message.isSentByUser()) {
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            holder.messageTextView.setBackgroundResource(R.drawable.background_sent_message);
        } else {
            params.addRule(RelativeLayout.ALIGN_PARENT_START);
            holder.messageTextView.setBackgroundResource(R.drawable.background_received_message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        MessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }
    }
}
