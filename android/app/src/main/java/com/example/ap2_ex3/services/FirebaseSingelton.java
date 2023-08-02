package com.example.ap2_ex3.services;

import androidx.lifecycle.MutableLiveData;

public class FirebaseSingelton {
    public static MutableLiveData<Integer> messageCounter;

    public static synchronized MutableLiveData<Integer> getMessageCounter() {
        if (messageCounter == null) {
            messageCounter = new MutableLiveData<>();
        }
        return messageCounter;
    }
}
