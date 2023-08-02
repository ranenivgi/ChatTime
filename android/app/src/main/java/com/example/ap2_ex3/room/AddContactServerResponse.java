package com.example.ap2_ex3.room;

public class AddContactServerResponse {
    private String id;
    private Contact.User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Contact.User getUser() {
        return user;
    }

    public void setUser(Contact.User user) {
        this.user = user;
    }
}
