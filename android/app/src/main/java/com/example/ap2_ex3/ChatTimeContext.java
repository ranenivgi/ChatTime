package com.example.ap2_ex3;

import android.app.Application;
import android.content.Context;

public class ChatTimeContext extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
