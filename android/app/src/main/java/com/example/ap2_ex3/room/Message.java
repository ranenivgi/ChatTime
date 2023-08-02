package com.example.ap2_ex3.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Message {
    @PrimaryKey(autoGenerate = true)
    private long localID;

    private String created;
    private String content;
    private boolean sent;
    private String senderPic;
    private String chatID;

    public Message(String content, String created , boolean sent, String senderPic, String chatID) {
        this.created = created;
        this.content = content;
        this.sent = sent;
        this.senderPic = senderPic;
        this.chatID = chatID;
    }

    public String getSenderPic() {
        return senderPic;
    }

    public void setSenderPic(String senderPic) {
        this.senderPic = senderPic;
    }

    public long getLocalID() {
        return localID;
    }

    public void setLocalID(long localID) {
        this.localID = localID;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
