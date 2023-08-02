package com.example.ap2_ex3.api;

import com.example.ap2_ex3.room.AddContactServerResponse;
import com.example.ap2_ex3.room.Contact;
import com.example.ap2_ex3.room.ServerMessageResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ContactServiceAPI {
    @GET("/api/Chats")
    Call<List<Contact>> getContacts();

    @POST("/api/Chats")
    Call<AddContactServerResponse> addContact(@Body Map<String, String> username);

    @GET("/api/Chats/{id}")
    Call<Contact> getContact(@Path("id") String id);

    @GET("/api/Chats/{id}/Messages")
    Call<List<ServerMessageResponse>> getMessages(@Path("id") String id);

    @POST("/api/Chats/{id}/Messages")
    Call<Void> addMessage(@Path("id") String id, @Body Map<String, String> msg);
}
