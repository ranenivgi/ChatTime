package com.example.ap2_ex3;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.example.ap2_ex3.authentication.Register;
import com.example.ap2_ex3.databinding.ActivityMainBinding;
import com.example.ap2_ex3.room.AppDB;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private AppDB db;
    private ActivityMainBinding binding;
    private ChatTimeContext chatTimeContext;
    public static Context context;

    private void initDB() {
        // Create Room database
        db = Room.databaseBuilder(getApplicationContext(),
                AppDB.class, AppDB.DATABASE_NAME).allowMainThreadQueries().build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;
        initDB();
        MyPreferences myPreferences = new MyPreferences(context);
        String mode = myPreferences.get("mode");
        if(mode.equals("light")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (mode.equals("dark")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        // Check if the user is already logged in
        boolean isLoggedIn = (myPreferences.get("username") != null) && !(Objects.equals(myPreferences.get("username"), ""));

        if (!isLoggedIn) {
            // User is not registered, navigate to RegisterActivity
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
            finish();
        } else {
            // User is logged in, continue with navigation setup
            setupNavigation();
        }
    }

    private void setupNavigation() {
        FrameLayout navHostContainer = findViewById(R.id.nav_host_fragment);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        NavInflater navInflater = navController.getNavInflater();
        NavGraph navGraph = navInflater.inflate(R.navigation.nav_graph);
        navController.setGraph(navGraph);
    }

    public NavController getNavController() {
        return navController;
    }
}