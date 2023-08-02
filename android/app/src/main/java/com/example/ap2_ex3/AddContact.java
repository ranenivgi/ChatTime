package com.example.ap2_ex3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ap2_ex3.databinding.ActivityAddContactBinding;
import com.example.ap2_ex3.viewmodels.ContactsViewModel;

public class AddContact extends AppCompatActivity {
    private ActivityAddContactBinding binding;
    private ContactsViewModel viewModel;
    private boolean back = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addContactBtnBack.setOnClickListener(v -> {
            back = true;
            onBackPressed();
            });

        viewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
        binding.addContactBtnSubmit.setOnClickListener(v -> {
            String username = binding.addContactEtUsername.getText().toString();
            if (username.isEmpty()) {
                binding.addContactEtUsername.setError("You must enter a username");
                return;
            }
            viewModel.addContact(username, AddContact.this);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        if (back) {
            super.onBackPressed();
        }
    }
}