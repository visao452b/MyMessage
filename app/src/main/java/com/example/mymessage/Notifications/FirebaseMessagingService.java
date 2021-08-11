package com.example.mymessage.Notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.example.mymessage.Activity.MainActivity;
import com.example.mymessage.R;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Map;

import static com.example.mymessage.Notifications.MyApplication.CHANNEL_ID;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    public static final String TAG = MyFirebaseMessagingService.class.getName();
    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // playing audio and vibration when user se reques
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.setLooping(false);
        }

        // vibration
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 300, 300, 300};
        v.vibrate(pattern, -1);


        int resourceImage = getResources().getIdentifier(remoteMessage.getNotification().getIcon(), "drawable", getPackageName());


        Map<String, String> stringMap = remoteMessage.getData();
//        String strTitle = stringMap.get("name");
        String strTitle = remoteMessage.getNotification().getTitle();
        String strMessage = remoteMessage.getNotification().getBody();

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

    //     NotificationManager mNotificationManager;


//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//
//
//// playing audio and vibration when user se reques
//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//        r.play();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            r.setLooping(false);
//        }
//
//        // vibration
//        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        long[] pattern = {100, 300, 300, 300};
//        v.vibrate(pattern, -1);
//
//
//        int resourceImage = getResources().getIdentifier(remoteMessage.getNotification().getIcon(), "drawable", getPackageName());
//
//
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////            builder.setSmallIcon(R.drawable.icontrans);
//            builder.setSmallIcon(resourceImage);
//        } else {
////            builder.setSmallIcon(R.drawable.icon_kritikar);
//            builder.setSmallIcon(resourceImage);
//        }
//
//
//
//        Intent resultIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        builder.setContentTitle(remoteMessage.getNotification().getTitle());
//        builder.setContentText(remoteMessage.getNotification().getBody());
//        builder.setContentIntent(pendingIntent);
//        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()));
//        builder.setAutoCancel(true);
//        builder.setPriority(Notification.PRIORITY_MAX);
//
//        mNotificationManager =
//                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            String channelId = "Your_channel_id";
//            NotificationChannel channel = new NotificationChannel(
//                    channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_HIGH);
//            mNotificationManager.createNotificationChannel(channel);
//            builder.setChannelId(channelId);
//        }
//
//
//
//// notificationId is a unique int for each notification that you must define
//        mNotificationManager.notify(100, builder.build());
//
//
//    }

}


