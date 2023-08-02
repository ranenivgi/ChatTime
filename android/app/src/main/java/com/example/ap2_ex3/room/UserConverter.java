package com.example.ap2_ex3.room;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

public class UserConverter {
    @TypeConverter
    public static Contact.User fromJsonString(String value) {
        return new Gson().fromJson(value, Contact.User.class);
    }

    @TypeConverter
    public static String toJsonString(Contact.User user) {
        return new Gson().toJson(user);
    }
}