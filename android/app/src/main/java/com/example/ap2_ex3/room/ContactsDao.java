package com.example.ap2_ex3.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactsDao {
    @Query("SELECT * FROM contact")
    List<Contact> index();

    @Query("SELECT * FROM contact WHERE id = :id")
    Contact get(String id);

    @Insert
    void insert(Contact... contacts);

    @Update
    void update(Contact... contacts);

    @Query("DELETE FROM contact")
    void deleteContacts();


    @Query("SELECT user FROM Contact WHERE id = :chatID")
    String getProfilePicByChatID(String chatID);
}
