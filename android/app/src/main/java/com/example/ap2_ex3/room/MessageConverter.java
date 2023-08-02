package com.example.ap2_ex3.room;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

public class MessageConverter {
    @TypeConverter
    public static Message fromJsonString(String value) {
        return new Gson().fromJson(value, Message.class);
    }

    @TypeConverter
    public static String toJsonString(Message message) {
        return new Gson().toJson(message);
    }
}