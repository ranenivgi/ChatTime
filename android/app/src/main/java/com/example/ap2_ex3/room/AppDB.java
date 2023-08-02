package com.example.ap2_ex3.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class, Message.class}, version = 1)
public abstract class AppDB extends RoomDatabase {
    public static final String DATABASE_NAME = "ChatTime.db";
    public abstract com.example.ap2_ex3.room.ContactsDao contactDao();
    public abstract com.example.ap2_ex3.room.MessagesDao messagesDao();

}
