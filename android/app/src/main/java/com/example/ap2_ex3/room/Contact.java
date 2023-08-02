package com.example.ap2_ex3.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
public class Contact {
    @PrimaryKey
    @NonNull
    private String id;

    @TypeConverters(MessageConverter.class)
    private Message lastMessage;
    @TypeConverters(UserConverter.class)
    private User user;

    public Contact(String id, User user, Message lastMessage) {
        this.id = id;
        this.lastMessage = lastMessage;
        this.user = user;
    }


    public static class User {
        private String username;
        private String displayName;
        private String profilePic;

        public String getUsername() {
            return username;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getProfilePic() {
            return profilePic;
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}
