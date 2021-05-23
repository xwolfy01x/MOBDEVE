package com.mobdeve.cactus.mobdevemp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.mobdeve.cactus.mobdevemp.dao.ProgressDAOSQLImpl;
import com.mobdeve.cactus.mobdevemp.models.Progress;

public class SensorService extends Service implements SensorEventListener {
    private static final String DEBUG_TAG = "MotionLoggerService";
    private SensorManager sensorManager = null;
    private Sensor sensor = null;
    private double MagnitudePrevious;
    private int notifid = 1;
    ProgressDAOSQLImpl progressDatabase;
    private Progress userProgress;

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
        progressDatabase = new ProgressDAOSQLImpl(this);
        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);

        userProgress = progressDatabase.getOneProgress(sp.getString("username", ""));

        if(userProgress.getShoelvl()!=0)
            scheduleNotification(this, userProgress.getShoelvl()*60000, notifid);
        notifid++;
        super.onDestroy();

    }
    public void scheduleNotification(Context context, long delay, int notificationId) {

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("NotificationText", "You've reached your idle time! Come back soon!");
        intent.putExtra("notifid", notificationId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
}

