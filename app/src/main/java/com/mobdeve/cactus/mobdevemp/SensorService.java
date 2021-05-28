package com.mobdeve.cactus.mobdevemp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.mobdeve.cactus.mobdevemp.dao.UserDAOSQLImpl;
import com.mobdeve.cactus.mobdevemp.models.Progress;
import com.mobdeve.cactus.mobdevemp.models.User;

import java.util.Timer;
import java.util.TimerTask;

public class SensorService extends Service implements SensorEventListener {
    private static final String CHANNEL_ID = "com.mobdeve.cactus.mobdevemp";
    private SensorManager sensorManager = null;
    private Sensor sensor = null;
    private double MagnitudePrevious;
    private int notifid = 1;
    double gold;
    double rate;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ProgressDAOSQLImpl progressDatabase;
    private Progress userProgress;
    BroadcastReceiver broadcastReceiver;
    SensorEventTask se;
    Timer timer;
    private boolean isRunning = false;
    private int walkCount;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!isRunning) {
            sp = getSharedPreferences("user", Context.MODE_PRIVATE);
            Intent notificationIntent = new Intent(this, HomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            final Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Idle Walk Background Running")
                    .setContentText("Idle Walk is tracking steps until cap is reached!")
                    .setSmallIcon(R.drawable.footsteps_white)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setChannelId(CHANNEL_ID)
                    .build();

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                notificationChannel = new NotificationChannel(CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            startForeground(5, notification);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.mobdeve.cactus.mobdevemp.service");
            intentFilter.addAction("com.mobdeve.cactus.mobdevemp.destroyService");

            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

            gold = 0;
            rate = 0;
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equalsIgnoreCase("com.mobdeve.cactus.mobdevemp.service")) {
                        timer = new Timer();
                        gold = intent.getDoubleExtra("gold", 0);
                        rate = intent.getDoubleExtra("rate", 0);
                        progressDatabase = new ProgressDAOSQLImpl(getApplicationContext());
                        userProgress = progressDatabase.getOneProgress(sp.getString("username", ""));
                        if (userProgress.getShoelvl()!=0) {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    nm.cancel(5);
                                    isRunning = false;
                                    stopForeground(true);
                                    scheduleNotification(context.getApplicationContext(), 1000, 1);
                                    stopSelf();
                                }
                            }, userProgress.getShoelvl() * 60000);
                        } else {
                            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            nm.cancel(5);
                            isRunning = false;
                            stopForeground(true);
                            stopSelf();
                        }
                    }
                    else if (intent.getAction().equalsIgnoreCase("com.mobdeve.cactus.mobdevemp.destroyService")) {
                        isRunning = false;
                        stopForeground(true);
                        stopSelf();
                    }
                }
            };
            registerReceiver(broadcastReceiver, intentFilter);
            isRunning = true;
        }
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(timer!=null)
            timer.cancel();
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
        se = new SensorEventTask();
        se.execute(sensorEvent);
    }

    class SensorEventTask extends AsyncTask<SensorEvent, Void, Void> {
        @Override
        protected Void doInBackground(SensorEvent... sensorEvents) {
            SensorEvent sensorEvent = sensorEvents[0];
            if (sensorEvent!=null && isRunning) {
                float x_acceleration = sensorEvent.values[0];
                float y_acceleration = sensorEvent.values[1];
                float z_acceleration = sensorEvent.values[2];
                double Magnitude = Math.sqrt(x_acceleration*x_acceleration + y_acceleration*y_acceleration + z_acceleration*z_acceleration);
                double MagnitudeDelta = Magnitude - MagnitudePrevious;
                MagnitudePrevious = Magnitude;
                if (MagnitudeDelta > 6) {
                    Intent intent = new Intent();
                    Log.d("Service", String.format("%.2f", gold));
                    intent.setAction("com.mobdeve.cactus.mobdevemp");
                    sendBroadcast(intent);
                    gold += rate;
                    walkCount++;
                }
            }
            return null;
        }
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        progressDatabase = new ProgressDAOSQLImpl(getApplicationContext());
        userProgress = progressDatabase.getOneProgress(sp.getString("username", ""));
        if(userProgress.getShoelvl()!=0) {
            userProgress.setGold(gold);
            progressDatabase.updateProgress(userProgress);
        }
        se.cancel(true);
        unregisterReceiver(broadcastReceiver);
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

