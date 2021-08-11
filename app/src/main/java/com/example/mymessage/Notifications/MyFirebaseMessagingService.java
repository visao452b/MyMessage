package com.example.mymessage.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.mymessage.Activity.MainActivity;
import com.example.mymessage.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Map;

import static com.example.mymessage.Notifications.MyApplication.CHANNEL_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = MyFirebaseMessagingService.class.getName();

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        if (notification == null){
//            return;
//        }
//        String strTitle = notification.getTitle();
//        String strMessage = notification.getBody();
        // Data message
        Map<String, String> stringMap = remoteMessage.getData();
//        if (stringMap == null){
//            return;
//        }
        String strTitle = stringMap.get("name");
        String strMessage = stringMap.get("message");

        sendNotification(strTitle, strMessage);
    }

    private void sendNotification(String strTitle, String strMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(strTitle)
                .setContentText(strMessage)
                .setSmallIcon(R.drawable.ic_send)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        Notification notification = noBuilder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null){
            notificationManager.notify(getNotificationId(), notification);
        }
    }

    private int getNotificationId(){
        return (int) new Date().getTime();
    }

    @Override
    public void onNewToken(@NonNull @NotNull String s) {
        super.onNewToken(s);
        Log.e(TAG, s);
    }
}
