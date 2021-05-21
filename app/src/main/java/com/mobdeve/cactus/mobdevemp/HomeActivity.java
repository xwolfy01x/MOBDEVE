package com.mobdeve.cactus.mobdevemp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.mobdeve.cactus.mobdevemp.dao.ProgressDAOSQLImpl;
import com.mobdeve.cactus.mobdevemp.dao.UserDAOSQLImpl;
import com.mobdeve.cactus.mobdevemp.models.Progress;
import com.mobdeve.cactus.mobdevemp.models.User;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {
    private TextView tv_gold, tv_name3, tv_level;
    private TextView tv_cap_lvl, tv_cloth_lvl, tv_shorts_lvl, tv_shoes_lvl;
    private TextView tv_cap_shard, tv_cloth_shard, tv_shorts_shard, tv_shoes_shard;
    private ImageView btn_cap_upg, btn_cloth_upg, btn_shorts_upg, btn_shoes_upg;
    private ProgressBar progressBar;
    private GameView gameView;
    private ViewGroup.LayoutParams lp;
    private ConstraintLayout cl;
    SharedPreferences sp;
    UserDAOSQLImpl userDB;
    ProgressDAOSQLImpl progressDB;
    User user;
    Progress userProgress;
    Timer timer;
    double capRate, shirtRate, shortRate;
    long resumeTime;
    long pauseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userDB = new UserDAOSQLImpl(getApplicationContext());
        progressDB = new ProgressDAOSQLImpl(getApplicationContext());
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);

        //get data from the db
        user = userDB.getUser(sp.getString("username", ""));
        userProgress = progressDB.getOneProgress(user.getUsername());

        init();

        //set the data of the wintermelon
        tv_name3.setText(user.getName());
        resumeTime = System.currentTimeMillis();
        pauseTime = System.currentTimeMillis();

//        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        SensorEventListener stepDetector = new SensorEventListener() {
//            @Override
//            public void onSensorChanged(SensorEvent sensorEvent) {
//                if (sensorEvent!=null) {
//                    float x_acceleration = sensorEvent.values[0];
//                    float y_acceleration = sensorEvent.values[1];
//                    float z_acceleration = sensorEvent.values[2];
//                    double Magnitude = Math.sqrt(x_acceleration*x_acceleration + y_acceleration*y_acceleration + z_acceleration*z_acceleration);
//                    double MagnitudeDelta = Magnitude - MagnitudePrevious;
//                    MagnitudePrevious = Magnitude;
//
//                    if (MagnitudeDelta > 3) {
//                        stepCount++;
//                        if (progressBar.getProgress() + (int) shortRate > progressBar.getMax()) {
//                            user.setCurrentExp(progressBar.getProgress() + (int) shortRate - progressBar.getMax());
//                            progressBar.setProgress(progressBar.getProgress() + (int) shortRate - progressBar.getMax());
//                            user.setLevel(user.getLevel()+1);
//                            progressBar.setMax((int) getProgressMax(user.getLevel()));
//                            refreshData();
//                        } else {
//                            progressBar.setProgress(user.getCurrentExp() + (int) shortRate);
//                            user.setCurrentExp(user.getCurrentExp() + (int) shortRate);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//            }
//        };
//        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void init() {
        //Initialize the chubacabra
        tv_gold = (TextView) findViewById(R.id.tv_gold);
        tv_name3 = (TextView) findViewById(R.id.tv_name3);
        tv_level = (TextView) findViewById(R.id.tv_level);
        tv_cap_lvl = (TextView) findViewById(R.id.tv_cap_lvl);
        tv_cloth_lvl = (TextView) findViewById(R.id.tv_cloth_lvl);
        tv_shorts_lvl = (TextView) findViewById(R.id.tv_shorts_lvl);
        tv_shoes_lvl = (TextView) findViewById(R.id.tv_shoes_lvl);
        tv_cap_shard = (TextView) findViewById(R.id.tv_cap_shard);
        tv_cloth_shard = (TextView) findViewById(R.id.tv_cloth_shard);
        tv_shorts_shard = (TextView) findViewById(R.id.tv_shorts_shard);
        tv_shoes_shard = (TextView) findViewById(R.id.tv_shoes_shard);
        btn_cap_upg = (ImageView) findViewById(R.id.btn_cap_upg);
        btn_cloth_upg = (ImageView) findViewById(R.id.btn_cloth_upg);
        btn_shorts_upg = (ImageView) findViewById(R.id.btn_shorts_upg);
        btn_shoes_upg = (ImageView) findViewById(R.id.btn_shoes_upg);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        gameView = new GameView(this);
        cl = (ConstraintLayout) findViewById(R.id.cl);
        cl.addView(gameView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
        capRate = capFormula(userProgress.getCaplvl());
        shirtRate = shirtFormula(userProgress.getShirtlvl());
        shortRate = shortsFormula(userProgress.getShortlvl());

        cl.setOnClickListener(v -> {
            Double currentGold = Double.parseDouble(tv_gold.getText().toString());
            Double capBonusGold = 5.0;
            if (userProgress.getCaplvl() > 0)
                capBonusGold = capRate;
            tv_gold.setText(Double.toString(currentGold + capBonusGold));
        });

        tv_gold.setText(String.format("%.2f",userProgress.getGold()));

        btn_cap_upg.setOnClickListener(v -> {
            userProgress.setCaplvl(userProgress.getCaplvl()+1);
            capRate = capFormula(userProgress.getCaplvl());
            refreshData();
        });

        btn_cloth_upg.setOnClickListener(v -> {
            userProgress.setShirtlvl(userProgress.getShirtlvl()+1);
            shirtRate = shirtFormula(userProgress.getShirtlvl());
            refreshData();
        });

        btn_shorts_upg.setOnClickListener(v -> {
            userProgress.setShortlvl(userProgress.getShortlvl()+1);
            shortRate = shortsFormula(userProgress.getShortlvl());
            refreshData();
        });

        btn_shoes_upg.setOnClickListener(v -> {
            userProgress.setShoelvl(userProgress.getShoelvl()+1);
            refreshData();
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.mobdeve.cactus.mobdevemp");
        registerReceiver(broadcastReceiver, intentFilter);
        registerService();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (progressBar.getProgress() + (int) shortRate > progressBar.getMax()) {
                user.setCurrentExp(progressBar.getProgress() + (int) shortRate - progressBar.getMax());
                progressBar.setProgress(progressBar.getProgress() + (int) shortRate - progressBar.getMax());
                user.setLevel(user.getLevel()+1);
                progressBar.setMax((int) getProgressMax(user.getLevel()));
                refreshData();
            } else {
                progressBar.setProgress(user.getCurrentExp() + (int) shortRate);
                user.setCurrentExp(user.getCurrentExp() + (int) shortRate);
            }
        }
    };

    public void refreshData() {

        tv_level.setText("Level " + user.getLevel());
        tv_cap_lvl.setText("LVL " + userProgress.getCaplvl());
        tv_cloth_lvl.setText("LVL " + userProgress.getShirtlvl());
        tv_shorts_lvl.setText("LVL " + userProgress.getShortlvl());
        tv_shoes_lvl.setText("LVL " + userProgress.getShoelvl());

        tv_cap_shard.setText(userProgress.getCapshard()+"/"+(5+userProgress.getCaplvl()*5));
        tv_shoes_shard.setText(userProgress.getShoeshard()+"/"+(5+userProgress.getShoelvl()*5));
        tv_cloth_shard.setText(userProgress.getShirtshard()+"/"+(5+userProgress.getShirtlvl()*5));
        tv_shorts_shard.setText(userProgress.getShortshard()+"/"+(5+userProgress.getShortlvl()*5));

        if (userProgress.getCapshard() > (10+userProgress.getCaplvl()*5)) {
            tv_cap_shard.setTextColor(Color.YELLOW);
            btn_cap_upg.setEnabled(true);
            saveData();
            refreshData();
        } else {
            tv_cap_shard.setTextColor(Color.WHITE);
//            btn_cap_upg.setEnabled(false);
        }
        if (userProgress.getShirtshard() > (10+userProgress.getShirtlvl()*5)) {
            tv_cloth_shard.setTextColor(Color.YELLOW);
            btn_cloth_upg.setEnabled(true);
            saveData();
            refreshData();
        } else {
            tv_cloth_shard.setTextColor(Color.WHITE);
//            btn_cloth_upg.setEnabled(false);
        }
        if (userProgress.getShortshard() > (10+userProgress.getShortlvl()*5)) {
            tv_shorts_shard.setTextColor(Color.YELLOW);
            btn_shorts_upg.setEnabled(true);
            saveData();
            refreshData();
        } else {
            tv_shorts_shard.setTextColor(Color.WHITE);
//            btn_shorts_upg.setEnabled(false);
        }
        if (userProgress.getShoeshard() > (10+userProgress.getShoelvl()*5)) {
            tv_shoes_shard.setTextColor(Color.YELLOW);
            btn_shoes_upg.setEnabled(true);
            saveData();
            refreshData();
        } else {
            tv_shoes_shard.setTextColor(Color.WHITE);
            btn_shoes_upg.setEnabled(false);
        }
    }

    public void refreshGold() {
            shirtRate=shirtFormula(userProgress.getShirtlvl());
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Double currentGold = Double.parseDouble(tv_gold.getText().toString());
                if (userProgress.getShirtlvl() != 0) {
                    Double shirtBonusGold = shirtRate;
                    tv_gold.setText(String.format("%.2f",currentGold + shirtBonusGold));
                }
            }
        }, 0, 1000);
    }

    public void cancelGoldTimer() {
        timer.cancel();
    }

    public double capFormula(int level) {
        double baseCoin = 5.0;
        for (int i = 1; i < level; i++)
            baseCoin += baseCoin * 0.05;
        return baseCoin;
    }

    public double shirtFormula(int level) {
        double baseCoin = 1.5;
        for (int i = 1; i < level; i++)
            baseCoin += baseCoin * 0.5;
        return baseCoin;
    }

    public double shortsFormula(int level) {
        double baseExp = 2.0;
        for (int i = 1; i < level; i++)
            baseExp += baseExp * 0.5;
        return baseExp;
    }

    public double getProgressMax(int level) {
        double baseExp = 100;
        for (int i = 1; i < level; i++)
            baseExp += baseExp * 0.5;
        return baseExp;
    }

    public void saveData() {
        progressDB.updateProgress(userProgress);
        userDB.updateUser(user);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
        refreshData();
        refreshGold();
        resumeTime = System.currentTimeMillis();
        if (resumeTime > pauseTime) {
            Double currentGold = userProgress.getGold();
            long diffInSeconds = (resumeTime - pauseTime)/1000;
            Toast.makeText(this, Long.toString(diffInSeconds), Toast.LENGTH_SHORT).show();
            if (userProgress.getShirtlvl()!=0) {
                double value = currentGold + shirtFormula(userProgress.getShirtlvl()) * diffInSeconds;
                tv_gold.setText(String.format("%.2f", value));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
        timer.cancel();
        userProgress.setGold(Double.parseDouble(tv_gold.getText().toString()));
        user.setLevel(user.getLevel());
        user.setCurrentExp(progressBar.getProgress());
        saveData();
        pauseTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        userProgress.setGold(Double.parseDouble(tv_gold.getText().toString()));
        saveData();
        super.onDestroy();
    }

    private void registerService() {
        startService(new Intent(this, SensorService.class));
    }
}
