package com.example.ap2_ex3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ap2_ex3.adapters.ContactsListAdapter;
import com.example.ap2_ex3.databinding.FragmentContactsBinding;
import com.example.ap2_ex3.room.Contact;
import com.example.ap2_ex3.services.FirebaseSingelton;
import com.example.ap2_ex3.viewmodels.ContactsViewModel;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment implements onContactClickListener, SwipeRefreshLayout.OnRefreshListener {

    private FragmentContactsBinding binding;
    private ContactsViewModel viewModel;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ContactsListAdapter contactsListAdapter;

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentContactsBinding.inflate(inflater, container, false);
        binding.addContact.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddContact.class);
            addContactResult.launch(intent);
        });

        binding.contactListBtnSettings.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), Settings.class);
            startActivity(intent);
        });

        viewModel = new ViewModelProvider(requireActivity()).get(ContactsViewModel.class);
        String username = new MyPreferences(MainActivity.context).get("userDisplayName");
        binding.userName.setText(username);

        String profilePic = new MyPreferences(MainActivity.context).get("userPic");
        if (profilePic != null && !profilePic.isEmpty()) {
            byte[] decodedBytes = Base64.decode(profilePic, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            binding.userLayoutProfilePic.setImageBitmap(decodedBitmap);
        }
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed in this case
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Get the search query
                String query = s.toString().trim();

                // Filter the contacts based on the query
                List<Contact> filteredContacts = new ArrayList<>();

                // Update the contacts list with the current database contacts
                List<Contact> contacts = viewModel.getContacts().getValue();

                if (query.isEmpty() && contacts != null) {
                    // If the query is empty, show all contacts
                    filteredContacts.addAll(contacts);
                } else {
                    if (contacts != null) {
                        for (Contact contact : contacts) {
                            if (contact.getUser().getDisplayName().toLowerCase().startsWith(query.toLowerCase())) {
                                filteredContacts.add(contact);
                            }
                        }
                    }
                }

                // Update the contact list in the adapter
                contactsListAdapter.setContacts(filteredContacts);
                contactsListAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed in this case
            }
        };
        binding.searchEt.addTextChangedListener(textWatcher);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = binding.swipeRefreshLayout;
        mSwipeRefreshLayout.setOnRefreshListener(this);

        RecyclerView contactsList = binding.lstContacts;
        contactsListAdapter = new ContactsListAdapter(getContext(), this);
        contactsList.setAdapter(contactsListAdapter);
        contactsList.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));
        MutableLiveData<Integer> newMessageNotification = FirebaseSingelton.getMessageCounter();
        newMessageNotification.observe(this, message -> onRefresh());
        viewModel.getContacts().observe(getViewLifecycleOwner(), contacts -> {
            contactsListAdapter.setContacts(contacts);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void setOnContactClick(View v, Contact contact) {
        // Set current contact
        viewModel.setLiveContactId(contact.getId());
        // Create a bundle to hold the arguments
        Bundle args = new Bundle();
        args.putString("contactName", contact.getUser().getDisplayName());
        args.putString("contactPic", contact.getUser().getProfilePic());
        NavHostFragment.findNavController(ContactsFragment.this)
                .navigate(R.id.action_first_to_second, args);
    }

    @Override
    public void onRefresh() {
        new Thread(() -> {
            viewModel.reload();
            mSwipeRefreshLayout.setRefreshing(false);
        }).start();
    }

    ActivityResultLauncher<Intent> addContactResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                        new Thread(() -> viewModel.reload()).start();
                }
            });

    @Override
    public void onResume() {
        // Get contacts list
        new Thread(() -> viewModel.reload()).start();
        super.onResume();
    }
}