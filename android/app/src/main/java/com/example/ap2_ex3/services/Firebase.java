package com.example.ap2_ex3.services;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.MutableLiveData;

import com.example.ap2_ex3.MainActivity;
import com.example.ap2_ex3.MyPreferences;
import com.example.ap2_ex3.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Firebase extends FirebaseMessagingService {
    private int notificationId = 1;

    public Firebase() {
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", "Messages", importance);
            channel.setDescription("Default channel");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        MutableLiveData<Integer> messageID = FirebaseSingelton.getMessageCounter();
        final Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        if (remoteMessage.getNotification() != null) {
            String username = new MyPreferences(MainActivity.context).get("username");
            if (!username.equals(remoteMessage.getData().get("receiver"))) {
                return;
            }
            if("add-contact".equals(remoteMessage.getData().get("action"))) {
                messageID.postValue(notificationId);
                return;
            }
            createChannel();
            String text = remoteMessage.getNotification().getBody();
            if (text.length() > 20) {
                text = "New message from " + remoteMessage.getNotification().getTitle();
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(text)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(remoteMessage.getNotification().getBody()))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission isn't granted", Toast.LENGTH_SHORT).show();
                return;
            }
            notificationManager.notify(notificationId, builder.build());
            messageID.postValue(notificationId);
            ++notificationId;
        }
    }
}