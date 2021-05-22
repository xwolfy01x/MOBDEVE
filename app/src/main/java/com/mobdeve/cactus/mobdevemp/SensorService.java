package com.mobdeve.cactus.mobdevemp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class SensorService extends Service implements SensorEventListener {
    private static final String DEBUG_TAG = "MotionLoggerService";
    private SensorManager sensorManager = null;
    private Sensor sensor = null;
    private double MagnitudePrevious;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        new SensorEventTask().execute(sensorEvent);
    }
    class SensorEventTask extends AsyncTask<SensorEvent, Void, Void> {
        @Override
        protected Void doInBackground(SensorEvent... sensorEvents) {
            SensorEvent sensorEvent = sensorEvents[0];
            if (sensorEvent!=null) {
                float x_acceleration = sensorEvent.values[0];
                float y_acceleration = sensorEvent.values[1];
                float z_acceleration = sensorEvent.values[2];
                double Magnitude = Math.sqrt(x_acceleration*x_acceleration + y_acceleration*y_acceleration + z_acceleration*z_acceleration);
                double MagnitudeDelta = Magnitude - MagnitudePrevious;
                MagnitudePrevious = Magnitude;
                if (MagnitudeDelta > 3) {
                    Intent intent = new Intent();
                    intent.setAction("com.mobdeve.cactus.mobdevemp");
                    sendBroadcast(intent);
                }
            }
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scheduleNotification(this, 1000, 100);
    }
    public void scheduleNotification(Context context, long delay, int notificationId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.footsteps_white)
                .setContentTitle("Idle Walk")
                .setContentText("You've reached your idle time! Come back soon!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("You've reached your idle time! Come back soon!"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("NotificationText", "You've reached your idle time! Come back soon!");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, AlarmReceiver.class);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent2);
    }
}

