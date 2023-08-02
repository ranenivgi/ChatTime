package com.example.ap2_ex3.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MessagesDao {
    @Query("SELECT * FROM message")
    List<Message> index();

    @Query("SELECT * FROM message")
    List<Message> get();

    @Query("SELECT * FROM message WHERE chatID = :id ORDER BY localID")
    List<Message> getContactMessages(String id);

    @Insert
    void insert(Message... messages);

    @Update
    void update(Message... messages);

    @Query("DELETE FROM message WHERE chatID = :id")
    void delete(String id);

    @Query("DELETE FROM message")
    void deleteAllMsg();

}
