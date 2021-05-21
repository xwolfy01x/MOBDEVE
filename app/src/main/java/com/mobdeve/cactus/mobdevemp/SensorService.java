package com.mobdeve.cactus.mobdevemp;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;

import androidx.annotation.Nullable;

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
}

