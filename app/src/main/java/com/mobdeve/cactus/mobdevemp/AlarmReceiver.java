package com.mobdeve.cactus.mobdevemp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver{
    public static String NOTIFICATION_ID = "100";
    public static String NOTIFICATION = "notification";
    public static String NOTIFICATION_CHANNEL_ID = "my_channel";

    @Override
    public void onReceive(final Context context, Intent intent) {
        int notificationId = intent.getIntExtra("notifid", 0);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.footsteps_white)
                .setContentTitle("Idle Walk")
                .setContentText("You've reached your idle time! Come back soon!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("You've reached your idle time! Come back soon!"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(notificationId, notification);
        Log.d("Hi", "I went here");
    }
}
