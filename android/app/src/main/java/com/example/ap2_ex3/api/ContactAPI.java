package com.example.ap2_ex3.api;

import com.example.ap2_ex3.MainActivity;
import com.example.ap2_ex3.MyPreferences;
import com.example.ap2_ex3.room.AddContactServerResponse;
import com.example.ap2_ex3.room.Contact;
import com.example.ap2_ex3.room.ServerMessageResponse;

import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactAPI {
    Retrofit retrofit;
    ContactServiceAPI contactServiceAPI;

    public ContactAPI() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            final String token = new MyPreferences(MainActivity.context).get("token");
            Request newRequest  = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        }).build();
        String address = new MyPreferences(MainActivity.context).get("serverAddress");

        retrofit = new Retrofit.Builder().
                client(client).
                baseUrl(address).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        contactServiceAPI = retrofit.create(ContactServiceAPI.class);
    }

    public Call<List<Contact>> getContacts() {
        return contactServiceAPI.getContacts();
    }

    public Call<AddContactServerResponse> addContact(String username) {
        return contactServiceAPI.addContact(Map.of( "username", username));
    }

    public Call<Contact> getContact(String id) {
        return contactServiceAPI.getContact(id);
    }

    public Call<List<ServerMessageResponse>> getMessages(String id) {
        return contactServiceAPI.getMessages(id);
    }

    public Call<Void> addMessage(String id, String content) {
        return contactServiceAPI.addMessage(id, Map.of("msg", content));
    }
}
