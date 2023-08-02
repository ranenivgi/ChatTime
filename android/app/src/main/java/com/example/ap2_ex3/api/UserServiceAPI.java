package com.example.ap2_ex3.api;

import com.example.ap2_ex3.room.Contact;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserServiceAPI {
    @POST("/api/Users")
    Call<Void> register(@Body Map<String, String> user);

    @POST("/api/Tokens")
    Call<ResponseBody> getToken(@Body Map<String, String> user);

    @GET("/api/Users/{username}")
    Call<Contact.User> getUser(@Path("username") String username, @Header("Authorization") String token);
}
