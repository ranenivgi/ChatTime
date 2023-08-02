package com.example.ap2_ex3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ap2_ex3.adapters.MessagesListAdapter;
import com.example.ap2_ex3.api.ContactAPI;
import com.example.ap2_ex3.databinding.FragmentChatBinding;
import com.example.ap2_ex3.room.Message;
import com.example.ap2_ex3.services.FirebaseSingelton;
import com.example.ap2_ex3.viewmodels.ContactsViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ContactsViewModel viewModel;

    private String chatID;
    private MessagesListAdapter adapter;
    private FragmentChatBinding binding;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    // Scroll down to the last message sent after pressing "send" button
    private void scrollToBottom() {
        if (adapter.getItemCount() > 0) {
            binding.lstMessages.scrollToPosition(adapter.getItemCount() - 1);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ContactsViewModel.class);
        viewModel.reload();
        chatID = null;

        binding.chatBtnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        binding.chatBtnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Settings.class);
            startActivity(intent);
        });

        // Get the arguments passed from ContactsFragment
        Bundle args = getArguments();
        if (args != null) {
            // Retrieve the contact's name and picture from arguments
            String contactName = args.getString("contactName");
            String contactPic = args.getString("contactPic");

            binding.contactName.setText(contactName);
            if (contactPic != null && !contactPic.isEmpty()) {
                byte[] decodedBytes = Base64.decode(contactPic, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                binding.contactLayoutProfilePic.setImageBitmap(decodedBitmap);
            }
        }

        viewModel.getContactIdLiveData().observe(getViewLifecycleOwner(), id -> {
            chatID = id;
            if (chatID != null) {
                // Set contact name
                viewModel.setContactId(chatID);
                RecyclerView messagesRecyclerView = binding.lstMessages;
                adapter = new MessagesListAdapter(getContext());
                messagesRecyclerView.setAdapter(adapter);
                messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                viewModel.getMessages().observe(getViewLifecycleOwner(), messages -> {
                    adapter.setMessages(viewModel.getMessagesWithContact());
                    scrollToBottom();
                });
            }
        });

        if (chatID != null) {
            RecyclerView messagesRecyclerView = binding.lstMessages;
            adapter = new MessagesListAdapter(getContext());
            messagesRecyclerView.setAdapter(adapter);
            messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            viewModel.getMessages().observe(getViewLifecycleOwner(), messages -> {
                adapter.setMessages(viewModel.getMessagesWithContact());
            });
        }

        // Set contact name
        viewModel.setContactId(chatID);

        binding.sendBtn.setOnClickListener(v -> {
            if (chatID == null) {
                return;
            }
            String messageContent = binding.messageInput.getText().toString();
            if (messageContent.length() > 0) {
                // Format timestamp in MM/dd/yyyy HH:mm:ss
                String timestamp = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm", Locale.US));
                }
                String currentUserPic = new MyPreferences(MainActivity.context).get("userPic");
                // Create new message object
                Message newMessage = new Message(messageContent, timestamp, true, currentUserPic, chatID);
                // Insert message into database
                viewModel.insertMessage(newMessage);
                new ContactAPI()
                        .addMessage(newMessage.getChatID(), newMessage.getContent())
                        .enqueue(new Callback<>() {
                            @Override
                            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                            }

                            @Override
                            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                t.printStackTrace();
                            }
                        });
                binding.messageInput.setText("");
            }
        });
        MutableLiveData<Integer> newMessageNotification = FirebaseSingelton.getMessageCounter();
        newMessageNotification.observe(this, message -> {
            new Thread(() -> viewModel.reload()).start();
        });
    }

}