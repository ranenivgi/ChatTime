package com.example.ap2_ex3;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {
    private final SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public MyPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void set(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String get(String key) {
        if (!sharedPreferences.contains("serverAddress")) {
            set("serverAddress", "http://10.0.2.2:12345/api/");
        }
        return sharedPreferences.getString(key, "");
    }
}