package com.example.ap2_ex3.authentication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ap2_ex3.Settings;
import com.example.ap2_ex3.api.UserAPI;
import com.example.ap2_ex3.databinding.ActivityRegisterBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private boolean isValidUsername;
    private boolean isValidPassword;
    private boolean isValidPasswordConfirmation;
    private boolean isValidDisplayName;
    private boolean isValidImage;
    private String previewImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.registerBtnSettings.setOnClickListener(view -> {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        });
        binding.registerBtnSubmit.setOnClickListener(v -> handleSubmit());

        ActivityResultLauncher<String> filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        try {
                            // Retrieve the image as a Bitmap from the selected image URI
                            InputStream inputStream = getContentResolver().openInputStream(result);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                            // Convert the Bitmap to a byte array
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            byte[] imageBytes = byteArrayOutputStream.toByteArray();

                            // Encode the byte array to Base64
                            this.previewImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                            // Set the image URI as the source for the ImageView
                            binding.registerPreviewImg.setImageURI(result);

                            // Update the status
                            binding.registerBtnUploadImg.setError(null);
                            isValidImage = true;
                        } catch (IOException e) {
                            binding.registerBtnUploadImg.setError("Please upload a valid image");
                            isValidImage = false;
                        }
                    }
                });

        binding.registerBtnUploadImg.setOnClickListener((View.OnClickListener) view -> {
            // Launch the file picker activity
            filePickerLauncher.launch("image/*");
        });

        binding.registerToLogin.setOnClickListener(v -> {
            navigateToLoginPage();
        });
    }

    private void handleUsernameChange(String newUsername) {
        // Check validation
        if (newUsername.length() < 2) {
            binding.registerEtUsername.setError("Username must be at least more than 2 characters");
            isValidUsername = false;
        } else {
            binding.registerEtUsername.setError(null);
            isValidUsername = true;
        }
    }

    private void handlePasswordChange(String newPassword) {
        // Check if password is at least 6 characters long
        if (newPassword.length() < 6) {
            binding.registerEtPw.setError("Password must be at least 6 characters");
            isValidPassword = false;
        }
        // Check if the password contains one lowercase char
        else if (!newPassword.matches("(?=.*[a-z]).*")) {
            binding.registerEtPw.setError("Password must contain at least one lowercase letter");
            isValidPassword = false;
        }
        // Check if the password contains one uppercase char
        else if (!newPassword.matches("(?=.*[A-Z]).*")) {
            binding.registerEtPw.setError("Password must contain at least one uppercase letter");
            isValidPassword = false;
        }
        // Check if the password contains one digit
        else if (!newPassword.matches("(?=.*[0-9]).*")) {
            binding.registerEtPw.setError("Password must contain at least one digit");
            isValidPassword = false;
        } else {
            binding.registerEtPw.setError(null);
            isValidPassword = true;
        }
    }

    private void handlePasswordConfirmationChange(String password, String passwordConfirmation) {
        // Check if password confirmation matches the password
        if (!Objects.equals(passwordConfirmation, password)) {
            binding.registerEtPwConfirmation.setError("Password confirmation does not match");
            isValidPasswordConfirmation = false;
        } else {
            binding.registerEtPwConfirmation.setError(null);
            isValidPasswordConfirmation = true;
        }
    }

    private void handleDisplayNameChange(String newDisplayName) {
        // Check validation
        if (newDisplayName.length() < 2) {
            binding.registerEtDisplayName.setError("Display name must be at least more than 2 characters");
            isValidDisplayName = false;
        } else {
            binding.registerEtDisplayName.setError(null);
            isValidDisplayName = true;
        }
    }

    private void handleImageUpload() {
        // Handle image upload logic
    }

    private void handleSubmit() {
        // Validate all fields
        handleUsernameChange(binding.registerEtUsername.getText().toString());
        handlePasswordChange(binding.registerEtPw.getText().toString());
        handlePasswordConfirmationChange(binding.registerEtPw.getText().toString(),
                binding.registerEtPwConfirmation.getText().toString());
        handleDisplayNameChange(binding.registerEtDisplayName.getText().toString());

        if (!isValidImage) {
            binding.registerBtnUploadImg.setError("Please upload an image");
            return;
        }

        if (isValidUsername && isValidPassword
                && isValidPasswordConfirmation && isValidDisplayName) {
            // Registration successful
            String username = binding.registerEtUsername.getText().toString();
            String password = binding.registerEtPw.getText().toString();
            String displayName = binding.registerEtDisplayName.getText().toString();

            // Create new user with the details and add to the users list
            UserAPI userAPI = new UserAPI(Register.this);
            userAPI.register(username, password, displayName, previewImage, new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Continue to the login page
                        navigateToLoginPage();
                    } else {
                        Toast.makeText(Register.this, "Username is taken", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(Register.this, "Error connecting to the server", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void navigateToLoginPage() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}
