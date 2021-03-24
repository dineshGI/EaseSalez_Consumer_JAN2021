package com.gieasesales.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.gieasesales.R;
import com.gieasesales.activity.MainActivity;
import com.gieasesales.utils.Util;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String token) {

        Log.e("IFFRAN", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //Displaying data in log
        //It is optional
        // Log.e("from",  remoteMessage.getData());

        Util.Logcat.e("data" + remoteMessage.getData());
        Util.Logcat.e("body" + "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Util.Logcat.e("Title" + "Notification Message Title: " + remoteMessage.getNotification().getTitle());

       // Intent playbackServiceIntent = new Intent(this, BackgroundAudioService.class);
       // startService(playbackServiceIntent);
        //Calling method to generate notification
        //sendNotification(remoteMessage.getNotification().getBody());

        sendNotificationNew(remoteMessage.getNotification().getBody());

        //MyNotificationManager.getInstance(this).displayNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

    }

    private void sendNotificationNew(String remoteMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentIntent(pendingIntent)
                .setContentText(remoteMessage);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }

    private void sendNotification(String messageBody) {
        //mp.stop();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri defaultSoundUri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.alarm);
        /*MediaPlayer player = MediaPlayer.create(this, defaultSoundUri);
        player.setLooping(true);
        player.start();*/
        Log.e("messageBody", messageBody);
        Log.e("messageBody", messageBody);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setOnlyAlertOnce(false)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        //  .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(123, notificationBuilder.build());
    }
}