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
import com.example.ap2_ex3.onContactClickListener;
import com.example.ap2_ex3.room.Contact;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ContactViewHolder> {
    static class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final ImageView profilePic;
        private final TextView lastMessage;
        private final TextView lastMessageTime;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contactLayoutName);
            profilePic = itemView.findViewById(R.id.contactLayoutProfilePic);
            lastMessage = itemView.findViewById(R.id.contactLayoutLastMessage);
            lastMessageTime = itemView.findViewById(R.id.contactLayoutLastMessageTime);
        }
    }

    private final LayoutInflater mInflater;
    private List<Contact> contacts;
    private final onContactClickListener listener;

    public ContactsListAdapter(Context context, onContactClickListener listener) {
        mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.contact_layout, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        if (contacts != null) {
            final Contact current = contacts.get(position);
            holder.name.setText(current.getUser().getDisplayName());
            // Decode Profile Pic
            String base64Image = current.getUser().getProfilePic();
            if (base64Image != null && !base64Image.isEmpty()) {
                byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                holder.profilePic.setImageBitmap(decodedBitmap);
            }

            // Handle long last message
            if (current.getLastMessage() != null && !current.getLastMessage().getCreated().equals("")) {
                String lastMessage = current.getLastMessage().getContent();
                if (lastMessage.length() > 12) {
                    lastMessage = lastMessage.substring(0, 12) + "...";
                }
                holder.lastMessage.setText(lastMessage);
            } else {
                holder.lastMessage.setText("");
            }

            // Handle last message date
            if (current.getLastMessage() != null && !current.getLastMessage().getCreated().equals("")) {
                // Format the message time
                SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)", Locale.getDefault());
                Date createdDate;
                try {
                    createdDate = inputFormat.parse(current.getLastMessage().getCreated());
                    SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm\ndd/MM/yyyy", Locale.getDefault());
                    holder.lastMessageTime.setText(outputFormat.format(createdDate).toUpperCase());
                } catch (Exception e) {
                    holder.lastMessageTime.setText("");
                }
            } else {
                holder.lastMessageTime.setText("");
            }

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.setOnContactClick(v, current);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (contacts != null) {
            return contacts.size();
        }
        return 0;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }
}
