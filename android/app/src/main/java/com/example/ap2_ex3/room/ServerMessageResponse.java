package com.example.ap2_ex3.room;

import android.content.Context;

import com.example.ap2_ex3.MainActivity;
import com.example.ap2_ex3.MyPreferences;

import java.util.Objects;

public class ServerMessageResponse {
    private String id;
    private String content;
    private String created;
    private User sender;
    private final Context context;

    public ServerMessageResponse(Context context) {
        this.context = context;
    }

    public static class User {
        private String username;
        private String password;
        private String displayName;
        private String profilePic;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getProfilePic() {
            return profilePic;
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated() {
        return created;
    }

    public boolean isMessageSent() {
        return Objects.equals(sender.username, new MyPreferences(MainActivity.context).get("username"));
    }
    public String getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

}
