package com.example.ap2_ex3.api;

import android.content.Context;

import com.example.ap2_ex3.MainActivity;
import com.example.ap2_ex3.MyPreferences;
import com.example.ap2_ex3.R;
import com.example.ap2_ex3.room.Contact;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    Retrofit retrofit;
    com.example.ap2_ex3.api.UserServiceAPI userServiceAPI;
    private Context context;

    public UserAPI(Context context) {
        this.context = context;
        // Get server address from settings
        String address = new MyPreferences(MainActivity.context).get("serverAddress");

        retrofit = new Retrofit.Builder().
                baseUrl(address).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        userServiceAPI = retrofit.create(UserServiceAPI.class);
    }

    public void register(String username, String password, String displayName, String profilePic, Callback<Void> callback) {
        Map<String, String> userMap = new HashMap<>();
        userMap.put("username", username);
        userMap.put("password", password);
        userMap.put("displayName", displayName);
        userMap.put("profilePic", profilePic);
        Call<Void> call = userServiceAPI.register(userMap);
        call.enqueue(callback);
    }

    public void getToken(String username, String password, String firebaseToken, Callback<ResponseBody> callback) {
        Map<String, String> userMap = new HashMap<>();
        userMap.put("username", username);
        userMap.put("password", password);
        userMap.put("firebaseToken", firebaseToken);
        userMap.put("deviceToken", "true");
        Call<ResponseBody> call = userServiceAPI.getToken(userMap);
        call.enqueue(callback);
    }

    public void getUser(String username, Callback<Contact.User> callback) {
        String token = new MyPreferences(MainActivity.context).get("token");
        String header = "Bearer " + token;
        Call<Contact.User> call = userServiceAPI.getUser(username, header);
        call.enqueue(callback);
    }
}