package com.example.ap2_ex3.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ap2_ex3.R;
import com.example.ap2_ex3.room.Message;

import java.util.List;

public class MessagesListAdapter extends RecyclerView.Adapter<MessagesListAdapter.MessageViewHolder>  {
    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView profilePic;
        private final TextView content;
        private final TextView sentTime;
        private final View layout;
        private final View messageLayout;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.messageLayoutProfilePic);
            content = itemView.findViewById(R.id.messageContent);
            sentTime = itemView.findViewById(R.id.messageDate);
            layout = itemView.findViewById(R.id.messageLayout);
            messageLayout = itemView.findViewById(R.id.textLinearLayout);
        }
    }
    private final LayoutInflater mInflater;
    private List<Message> messages;

    public MessagesListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MessagesListAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.message_layout, parent, false);
        return new MessagesListAdapter.MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesListAdapter.MessageViewHolder holder, int position) {
        if (messages != null) {
            final Message current = messages.get(position);
            holder.content.setText(current.getContent());
            holder.sentTime.setText(current.getCreated());

            // Decode Profile Pic
            String base64Image = current.getSenderPic();
            if (base64Image != null && !base64Image.isEmpty()) {
                byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                holder.profilePic.setImageBitmap(decodedBitmap);
            }

            if (current.isSent()) {
                holder.messageLayout.setBackgroundResource(R.drawable.message_sent_shape);
                holder.layout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            } else {
                holder.messageLayout.setBackgroundResource(R.drawable.message_received_shape);
                holder.layout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (messages != null) {
            return messages.size();
        }
        return 0;
    }

    public List<Message> getMessages() {
        return messages;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }
}
