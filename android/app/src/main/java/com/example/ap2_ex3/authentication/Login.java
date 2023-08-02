package com.example.ap2_ex3.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ap2_ex3.MainActivity;
import com.example.ap2_ex3.MyPreferences;
import com.example.ap2_ex3.Settings;
import com.example.ap2_ex3.api.UserAPI;
import com.example.ap2_ex3.databinding.ActivityLoginBinding;
import com.example.ap2_ex3.room.Contact;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// ch3fPQjfCJA:APA91bHlXE3yanbQHQ4A_wAvFWc1LLUNt3dyEJ9LAD5zSrXagvYdKSkon87Voa7nP49RT1a5p9JFeg7A98MYA5vnW-SroPg6pKVgUfgdqY4OpAV5KQdhRRKiH3X3o_fEpBG1rDs_idDq

public class Login extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private boolean isValidUsername;
    private boolean isValidPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.loginBtnSettings.setOnClickListener(view -> {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        });
        binding.loginBtnSubmit.setOnClickListener(v -> handleSubmit());

        binding.loginToRegister.setOnClickListener(v -> {
            navigateToRegisterPage();
        });
    }

    private void navigateToRegisterPage() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
        finish();
    }

    private void handleUsernameChange(String newUsername) {
        // Check validation
        if (newUsername.length() < 2) {
            binding.loginEtUsername.setError("Username must be at least more than 2 characters");
            isValidUsername = false;
        } else {
            binding.loginEtUsername.setError(null);
            isValidUsername = true;
        }
    }

    private void handlePasswordChange(String newPassword) {
        // Check if password is at least 6 characters long
        if (newPassword.length() < 6) {
            binding.loginEtPw.setError("Password must be at least 6 characters");
            isValidPassword = false;
        }
        // Check if the password contains one lowercase char
        else if (!newPassword.matches("(?=.*[a-z]).*")) {
            binding.loginEtPw.setError("Password must contain at least one lowercase letter");
            isValidPassword = false;
        }
        // Check if the password contains one uppercase char
        else if (!newPassword.matches("(?=.*[A-Z]).*")) {
            binding.loginEtPw.setError("Password must contain at least one uppercase letter");
            isValidPassword = false;
        }
        // Check if the password contains one digit
        else if (!newPassword.matches("(?=.*[0-9]).*")) {
            binding.loginEtPw.setError("Password must contain at least one digit");
            isValidPassword = false;
        } else {
            binding.loginEtPw.setError(null);
            isValidPassword = true;
        }
    }

    private void handleSubmit() {
        // Validate all fields
        handleUsernameChange(binding.loginEtUsername.getText().toString());
        handlePasswordChange(binding.loginEtPw.getText().toString());


        if (isValidUsername && isValidPassword) {
            // Registration successful
            String username = binding.loginEtUsername.getText().toString();
            String password = binding.loginEtPw.getText().toString();

            // Get the firebase token
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
                String firebaseToken = instanceIdResult.getToken();

                // Create new user with the details and add to the users list
                UserAPI userAPI = new UserAPI(Login.this);
                userAPI.getToken(username, password, firebaseToken, new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                assert response.body() != null;
                                String token = response.body().string();
                                MyPreferences myPreferences = new MyPreferences(MainActivity.context);
                                myPreferences.set("token", token);
                                myPreferences.set("username", username);

                                userAPI.getUser(username, new Callback<>() {
                                    @Override
                                    public void onResponse(@NonNull Call<Contact.User> call, @NonNull Response<Contact.User> response) {
                                        if (response.isSuccessful()) {
                                            try {
                                                Contact.User user = response.body();
                                                MyPreferences myPreferences = new MyPreferences(MainActivity.context);
                                                assert user != null;
                                                myPreferences.set("userPic", user.getProfilePic());
                                                myPreferences.set("userDisplayName", user.getDisplayName());
                                                // Continue to the chat page using nav
                                                navigateToMainPage();
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                        } else {
                                            Toast.makeText(Login.this, "Invalid username or password. Please try again", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<Contact.User> call, @NonNull Throwable t) {
                                        Toast.makeText(Login.this, "Error connecting to the server", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            Toast.makeText(Login.this, "Invalid username or password. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Toast.makeText(Login.this, "Error connecting to the server", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }

    private void navigateToMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

