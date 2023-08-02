package com.example.ap2_ex3;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Room;

import com.example.ap2_ex3.authentication.Login;
import com.example.ap2_ex3.room.AppDB;
import com.example.ap2_ex3.room.ContactsDao;
import com.example.ap2_ex3.room.MessagesDao;

public class Settings extends AppCompatActivity {

    private ContactsDao contactsDao;
    private MessagesDao messagesDao;
    private MyPreferences myPreferences;
    private EditText serverAddressET;
    private boolean back = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        RadioGroup radioGroup = findViewById(R.id.idRGgroup);
        Button logoutButton = findViewById(R.id.settings_btnLogout);
        Button submitButton = findViewById(R.id.settings_btnSubmit);
        ImageButton backButton = findViewById(R.id.settings_btnBack);
        serverAddressET = findViewById(R.id.settings_ETServerAddress);


        myPreferences = new MyPreferences(MainActivity.context);

        AppDB db = Room.databaseBuilder(MainActivity.context,
                AppDB.class, AppDB.DATABASE_NAME).allowMainThreadQueries().build();
        contactsDao = db.contactDao();
        messagesDao = db.messagesDao();

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // on radio button check change

            if (checkedId == R.id.settings_RBLight) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                myPreferences.set("mode", "light");
            } else if (checkedId == R.id.settings_RBDark) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                myPreferences.set("mode", "dark");
            }
        });

        logoutButton.setOnClickListener(v -> {
            handleLogout();
        });

        final boolean[] isValidAddress = {false};
        serverAddressET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before the text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called when the text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called after the text has been changed
                String newText = s.toString();
                isValidAddress[0] = newText.matches("^(?:[1-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(?:\\.(?:[0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])){3}:(?:102[4-9]|10[3-9][0-9]|1[1-9][0-9]{2}|[2-9][0-9]{3}|[1-3][0-9]{4}|4[0-8][0-9]{3}|491[0-4][0-9]|4915[0-1])$");
            }
        });

        submitButton.setOnClickListener(v -> {
            if (!isValidAddress[0]) {
                serverAddressET.setError("Please enter a valid address");
                return;
            }
            String formattedAddress = "http://" + serverAddressET.getText().toString() + "/api/";
            myPreferences.set("serverAddress", formattedAddress);
            serverAddressET.setText("");
            handleLogout();
        });

        backButton.setOnClickListener(v -> {
            back = true;
            onBackPressed();
        });
    }

    @Override
    public void onBackPressed() {
        if (back) {
            super.onBackPressed();
        }
    }

    private void navigateToLoginPage() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    private void handleLogout() {
        myPreferences.set("token", null);
        myPreferences.set("username", null);
        myPreferences.set("userPic", null);
        myPreferences.set("userDisplayName", null);

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
            myPreferences.set("mode", "light");
        } else if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            myPreferences.set("mode", "dark");
        } else {
            myPreferences.set("mode", "light");
        }

        contactsDao.deleteContacts();
        messagesDao.deleteAllMsg();

        navigateToLoginPage();
    }
}