package com.example.ap2_ex3.repositories;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.ap2_ex3.MainActivity;
import com.example.ap2_ex3.MyPreferences;
import com.example.ap2_ex3.api.ContactAPI;
import com.example.ap2_ex3.room.AddContactServerResponse;
import com.example.ap2_ex3.room.AppDB;
import com.example.ap2_ex3.room.Contact;
import com.example.ap2_ex3.room.ContactsDao;
import com.example.ap2_ex3.room.Message;
import com.example.ap2_ex3.room.MessagesDao;
import com.example.ap2_ex3.room.ServerMessageResponse;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsRepository {
    private final ContactsDao contactsDao;
    private final MessagesDao messagesDao;
    private final ContactListData contactListData;
    private final MessageListData messageListData;

    public ContactsRepository() {
        // Create Room database
        AppDB db = Room.databaseBuilder(MainActivity.context,
                AppDB.class, AppDB.DATABASE_NAME).allowMainThreadQueries().build();
        contactsDao = db.contactDao();
        messagesDao = db.messagesDao();
        contactListData = new ContactListData();
        messageListData = new MessageListData();
    }

    public void insertContact(Contact contact) {
        contactsDao.insert(contact);
        List<Contact> contactsList = contactListData.getValue();
        if (contactsList == null) {
            contactsList = new LinkedList<>();
        }
        contactsList.add(contact);
        contactListData.setValue(contactsList);
    }

    class ContactListData extends MutableLiveData<List<Contact>> {
        public ContactListData() {
            super();
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
                List<Contact> contacts = contactsDao.index();
                postValue(contacts);
            }).start();
        }
    }

    class MessageListData extends MutableLiveData<List<Message>> {
        public MessageListData() {
            super();
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
                List<Message> messages = messagesDao.get();
                postValue(messages);
            }).start();
        }
    }

    public void insertMessage(Message message) {
        messagesDao.insert(message);
        List<Message> messageList = messageListData.getValue();
        if (messageList == null) {
            messageList = new LinkedList<>();
        }
        messageList.add(message);
        messageListData.setValue(messageList);
    }


    public void deleteContacts() {
        contactsDao.deleteContacts();
        contactListData.setValue(null);
    }

    private void deleteMessages(String id) {
        messagesDao.delete(id);
        messageListData.setValue(messagesDao.get());
    }

    public void addContact(String username, Context context) {
        ContactAPI contactAPI = new ContactAPI();
        contactAPI.addContact(username).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AddContactServerResponse> call, @NonNull Response<AddContactServerResponse> response) {
                if (response.isSuccessful()) {
                } else {
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddContactServerResponse> call, Throwable t) {
                Toast.makeText(context, "Error connecting to server", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void reload() {
        ContactAPI contactAPI = new ContactAPI();
        contactAPI.getContacts().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Contact>> call, @NonNull Response<List<Contact>> response) {
                if (response.isSuccessful()) {
                    deleteContacts();
                    List<Contact> contacts = response.body();
                    if (contacts != null && !contacts.isEmpty()) {
                        for (Contact contact : contacts) {
                            insertContact(contact);

                            contactAPI.getMessages(contact.getId())
                                    .enqueue(new Callback<>() {
                                                 @Override
                                                 public void onResponse(@NonNull Call<List<ServerMessageResponse>> call, @NonNull Response<List<ServerMessageResponse>> response) {
                                                     if (response.isSuccessful()) {
                                                         deleteMessages(contact.getId());
                                                         List<ServerMessageResponse> messages = response.body();
                                                         if (messages == null || messages.isEmpty()) {
                                                             return;
                                                         }
                                                         Collections.reverse(messages);
                                                         String formattedTime = "";
                                                         for (ServerMessageResponse message : messages) {
                                                             if (message.getContent() != null) {
                                                                 // Format the message time
                                                                 SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)", Locale.getDefault());
                                                                 Date createdDate;
                                                                 try {
                                                                     createdDate = inputFormat.parse(message.getCreated());
                                                                     SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                                                     formattedTime = outputFormat.format(createdDate).toUpperCase();
                                                                 } catch (Exception e) {
                                                                     formattedTime = "";
                                                                 }
                                                             }

                                                             String pic;
                                                             String currentUserPic = new MyPreferences(MainActivity.context).get("userPic");
                                                             if (message.isMessageSent()) {
                                                                 pic = currentUserPic;
                                                             } else {
                                                                 pic = contact.getUser().getProfilePic();
                                                             }

                                                             Message newMessage = new Message(message.getContent(), formattedTime, message.isMessageSent(), pic, contact.getId());
                                                             insertMessage(newMessage);
                                                         }
                                                     }
                                                 }


                                                 @Override
                                                 public void onFailure(@NonNull Call<List<ServerMessageResponse>> call, @NonNull Throwable t) {
                                                     t.printStackTrace();
                                                 }
                                             }
                                    );
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Contact>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // Get contact by id
    public Contact getContact(String id) {
        return contactsDao.get(id);
    }

    // Get contacts list
    public MutableLiveData<List<Contact>> getContacts() {
        return contactListData;
    }

    // Get all messages
    public MutableLiveData<List<Message>> getMessages() {
        return messageListData;
    }


}