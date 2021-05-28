package com.mobdeve.cactus.mobdevemp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {
    private String[] randSteps = {"100", "200", "500"};
    private String[] randTaps = {"1000", "500", "2000"};
    private int stepVal, tapVal;
    private TextView tv_gold, tv_name3, tv_level;
    private TextView tv_cap_lvl, tv_cloth_lvl, tv_shorts_lvl, tv_shoes_lvl;
    private TextView tv_cap_shard, tv_cloth_shard, tv_shorts_shard, tv_shoes_shard;
    private ImageView btn_cap_upg, btn_cloth_upg, btn_shorts_upg, btn_shoes_upg;
    private ImageView btn_market, btn_faq, btn_logout, btn_stats, btn_tasks;
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
    double capGoldRate, capExpRate, shirtRate, shortExpRate, shortGoldRate;
    Long shoesCapRate;
    long resumeTime;
    long pauseTime;
    double gold = 0;
    int walkCount, tapCount;
    String[] goldFormat = {"k", "M", "B", "T", "aa", "bb", "cc", "dd", "ee",
                        "ff", "gg", "hh", "ii", "jj", "kk", "ll", "mm", "nn",
                        "oo", "pp", "qq", "rr", "ss", "tt", "uu", "vv", "ww",
                        "xx", "yy", "zz", "AA", "BB", "CC", "DD", "EE", "FF",
                        "GG", "HH", "II", "JJ", "KK", "LL", "MM", "NN", "OO",
                        "PP", "QQ", "RR", "SS", "TT", "UU", "VV", "WW", "XX",
                        "YY", "ZZ"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerService();
        setContentView(R.layout.activity_home);

        userDB = new UserDAOSQLImpl(getApplicationContext());
        progressDB = new ProgressDAOSQLImpl(getApplicationContext());
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);

        init();

        resumeTime = System.currentTimeMillis();
        pauseTime = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        SharedPreferences.Editor editor = sp.edit();
        String lastLogin = calendar.get(Calendar.MONTH)+1 + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.YEAR);
        String ll = sp.getString(user.getUsername() + " LastLogin", "");
        if (ll.equalsIgnoreCase(lastLogin)) {
            Log.d("UPDATE", "I AM SAME");
            walkCount = Integer.parseInt(sp.getString(user.getUsername() + " walkCount", "0"));
            tapCount = Integer.parseInt(sp.getString(user.getUsername() + " tapCount", "0"));
        } else {
            Log.d("UPDATE", "I AM NEW/NOT THE SAME");
            editor.putString(user.getUsername() + " LastLogin", lastLogin);
            editor.putString(user.getUsername() + " walkCount", "0");
            editor.putString(user.getUsername() + " tapCount", "0");
            walkCount = 0;
            tapCount = 0;
            tapVal = Integer.parseInt(randTaps[randomVal()]);
            editor.putString(user.getUsername() + " tapVal", String.valueOf(tapVal));
            editor.putString(user.getUsername() + " stepVal", String.valueOf(stepVal));
            stepVal = Integer.parseInt(randSteps[randomVal()]);
            editor.putString(user.getUsername() + " quest1", "false");
            editor.putString(user.getUsername() + " quest2", "false");
            editor.putString(user.getUsername() + " quest3", "false");
            editor.apply();
        }

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
        btn_logout = (ImageView) findViewById(R.id.btn_logout);
        btn_stats = (ImageView) findViewById(R.id.btn_stats);
        btn_faq = (ImageView) findViewById(R.id.btn_faq);
        btn_market = (ImageView) findViewById(R.id.btn_market);
        btn_tasks = (ImageView) findViewById(R.id.btn_task);

        user = userDB.getUser(sp.getString("username", ""));
        userProgress = progressDB.getOneProgress(user.getUsername());
        tv_name3.setText(user.getName());
        gameView = new GameView(this);
        cl = (ConstraintLayout) findViewById(R.id.cl);
        cl.addView(gameView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
        capExpRate = capExpFormula(userProgress.getCaplvl());
        capGoldRate = capGoldFormula(userProgress.getCaplvl());
        shirtRate = shirtFormula(userProgress.getShirtlvl());
        shortGoldRate = shortsGoldFormula(userProgress.getShortlvl());
        shortExpRate = shortsExpFormula(userProgress.getShortlvl());

        cl.setOnClickListener(v -> {
            Double capBonusGold = 5.0;
            if (userProgress.getCaplvl() > 0)
                capBonusGold = capGoldRate;
            if (progressBar.getProgress() + (int) capExpRate > progressBar.getMax()) {
                user.setCurrentExp(progressBar.getProgress() + (int) capExpRate - progressBar.getMax());
                progressBar.setProgress(progressBar.getProgress() + (int) capExpRate - progressBar.getMax());
                user.setLevel(user.getLevel()+1);
                progressBar.setMax((int) getProgressMax(user.getLevel()));
                refreshData();
            } else {
                progressBar.setProgress(user.getCurrentExp() + (int) capExpRate);
                user.setCurrentExp(user.getCurrentExp() + (int) capExpRate);
            }
            gold +=capBonusGold;
            uiSetGold(gold);
            tapCount++;
        });

        //initialize gold
        gold = userProgress.getGold();
        uiSetGold(gold);

        //initialize exp
        progressBar.setMax((int) getProgressMax(user.getLevel()));
        progressBar.setProgress(user.getCurrentExp());

        //upgrade listeners
        btn_cap_upg.setOnClickListener(v -> {
            userProgress.setCapshard(userProgress.getCapshard() - (5+userProgress.getCaplvl()*5));
            userProgress.setCaplvl(userProgress.getCaplvl()+1);
            capExpRate = capExpFormula(userProgress.getCaplvl());
            capGoldRate = capGoldFormula(userProgress.getCaplvl());
            saveData();
            refreshData();
        });

        btn_cloth_upg.setOnClickListener(v -> {
            userProgress.setShirtshard(userProgress.getShirtshard() - (5+userProgress.getShirtlvl()*5));
            userProgress.setShirtlvl(userProgress.getShirtlvl()+1);
            shirtRate = shirtFormula(userProgress.getShirtlvl());
            saveData();
            refreshData();
        });

        btn_shorts_upg.setOnClickListener(v -> {
            userProgress.setShortshard(userProgress.getShortshard() - (5+userProgress.getShortlvl()*5));
            userProgress.setShortlvl(userProgress.getShortlvl()+1);
            shortGoldRate = shortsGoldFormula(userProgress.getShortlvl());
            shortExpRate = shortsExpFormula(userProgress.getShortlvl());
            saveData();
            refreshData();
        });

        btn_shoes_upg.setOnClickListener(v -> {
            userProgress.setShoeshard(userProgress.getShoeshard() - (5+userProgress.getShoelvl()*5));
            userProgress.setShoelvl(userProgress.getShoelvl()+1);
            shoesCapRate = shoesCapFormula((userProgress.getShoelvl()));
            saveData();
            refreshData();
        });

        btn_logout.setOnClickListener(v -> {
            gameView.pause();
            stopService();
            Intent intent = new Intent(this, MainActivity.class);
            Toast.makeText(this, "Logging Out! Please wait!", Toast.LENGTH_SHORT).show();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.remove("username");
                    editor.apply();
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        });

        btn_market.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MarketActivity.class);
            intent.putExtra("gold", gold);
            intent.putExtra("user", user);
            intent.putExtra("userProgress", userProgress);
            startActivity(intent);
        });

        btn_stats.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("userProgress", userProgress);
            startActivity(intent);
        });

        btn_tasks.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), QuestActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("userProgress", userProgress);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(user.getUsername() + " tapCount", Integer.toString(tapCount));
            editor.putString(user.getUsername() + " walkCount", Integer.toString(walkCount));
            editor.apply();
            startActivity(intent);
        });

        btn_faq.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
            startActivity(intent);
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.mobdeve.cactus.mobdevemp");
        intentFilter.addAction("com.mobdeve.marketbuy");
        intentFilter.addAction("com.mobdeve.quest");
        registerReceiver(broadcastReceiver, intentFilter);

        refreshData();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("com.mobdeve.cactus.mobdevemp")) {
                if (userProgress.getShortlvl() > 0) {
                    gold +=shortGoldRate;
                    uiSetGold(gold);
                    if (progressBar.getProgress() + (int) shortExpRate > progressBar.getMax()) {
                        user.setCurrentExp(progressBar.getProgress() + (int) shortExpRate - progressBar.getMax());
                        progressBar.setProgress(progressBar.getProgress() + (int) shortExpRate - progressBar.getMax());
                        user.setLevel(user.getLevel()+1);
                        progressBar.setMax((int) getProgressMax(user.getLevel()));
                        refreshData();
                    } else {
                        progressBar.setProgress(user.getCurrentExp() + (int) shortExpRate);
                        user.setCurrentExp(user.getCurrentExp() + (int) shortExpRate);
                    }
                }
                walkCount++;
            }
            else if (intent.getAction().equalsIgnoreCase("com.mobdeve.marketbuy")) {
                Bundle bundle = intent.getExtras();
                userProgress.setCapshard(userProgress.getCapshard() + bundle.getInt("capShard"));
                userProgress.setShirtshard(userProgress.getShirtshard() + bundle.getInt("shirtShard"));
                userProgress.setShortshard(userProgress.getShortshard() + bundle.getInt("shortShard"));
                userProgress.setShoeshard(userProgress.getShoeshard() + bundle.getInt("shoeShard"));
                userProgress.setCapshard(userProgress.getCapshard() + bundle.getInt("capShard2"));
                userProgress.setShirtshard(userProgress.getShirtshard() + bundle.getInt("shirtShard2"));
                userProgress.setShortshard(userProgress.getShortshard() + bundle.getInt("shortShard2"));
                userProgress.setShoeshard(userProgress.getShoeshard() + bundle.getInt("shoeShard2"));
                double value = (bundle.getInt("capShard") + bundle.getInt("shirtShard") + bundle.getInt("shortShard") + bundle.getInt("shoeShard"))*100;
                gold -= value;
                uiSetGold(gold);
                refreshData();
            }
            else if(intent.getAction().equalsIgnoreCase("com.mobdeve.quest")){
                Bundle bundle = intent.getExtras();
                userProgress.setCapshard(userProgress.getCapshard() + bundle.getInt("capShard2"));
                userProgress.setShirtshard(userProgress.getShirtshard() + bundle.getInt("shirtShard2"));
                userProgress.setShortshard(userProgress.getShortshard() + bundle.getInt("shortShard2"));
                userProgress.setShoeshard(userProgress.getShoeshard() + bundle.getInt("shoeShard2"));
                refreshData();
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

        if (userProgress.getCapshard() >= (5+userProgress.getCaplvl()*5)) {
            tv_cap_shard.setTextColor(Color.YELLOW);
            btn_cap_upg.setBackgroundResource(getResources().getIdentifier("upgrade", "drawable", getPackageName()));
            btn_cap_upg.setEnabled(true);
        } else {
            tv_cap_shard.setTextColor(Color.WHITE);
            btn_cap_upg.setBackgroundResource(getResources().getIdentifier("upgrade2", "drawable", getPackageName()));
            btn_cap_upg.setEnabled(false);
        }
        if (userProgress.getShirtshard() >= (5+userProgress.getShirtlvl()*5)) {
            tv_cloth_shard.setTextColor(Color.YELLOW);
            btn_cloth_upg.setBackgroundResource(getResources().getIdentifier("upgrade", "drawable", getPackageName()));
            btn_cloth_upg.setEnabled(true);
        } else {
            tv_cloth_shard.setTextColor(Color.WHITE);
            btn_cloth_upg.setBackgroundResource(getResources().getIdentifier("upgrade2", "drawable", getPackageName()));
            btn_cloth_upg.setEnabled(false);
        }
        if (userProgress.getShortshard() >= (5+userProgress.getShortlvl()*5)) {
            tv_shorts_shard.setTextColor(Color.YELLOW);
            btn_shorts_upg.setBackgroundResource(getResources().getIdentifier("upgrade", "drawable", getPackageName()));
            btn_shorts_upg.setEnabled(true);
        } else {
            tv_shorts_shard.setTextColor(Color.WHITE);
            btn_shorts_upg.setBackgroundResource(getResources().getIdentifier("upgrade2", "drawable", getPackageName()));
            btn_shorts_upg.setEnabled(false);
        }
        if (userProgress.getShoeshard() >= (5+userProgress.getShoelvl()*5)) {
            tv_shoes_shard.setTextColor(Color.YELLOW);
            btn_shoes_upg.setBackgroundResource(getResources().getIdentifier("upgrade", "drawable", getPackageName()));
            btn_shoes_upg.setEnabled(true);
        } else {
            tv_shoes_shard.setTextColor(Color.WHITE);
            btn_shoes_upg.setBackgroundResource(getResources().getIdentifier("upgrade2", "drawable", getPackageName()));
            btn_shoes_upg.setEnabled(false);
        }
    }

    public void refreshGold() {
        shirtRate=shirtFormula(userProgress.getShirtlvl());
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (userProgress.getShirtlvl() != 0) {
                    Double shirtBonusGold = shirtRate;
                    gold +=shirtBonusGold;
                    uiSetGold(gold);
                }
            }
        }, 0, 1000);
    }

    public double capExpFormula(int level) {
        double baseExp = 1;
        for (int i = 1; i < level; i++)
            baseExp += baseExp * 0.05;
        return baseExp;
    }

    public double capGoldFormula(int level) {
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

    public double shortsExpFormula(int level) {
        double baseExp = 2.0;
        for (int i = 1; i < level; i++)
            baseExp += baseExp * 0.5;
        return baseExp;
    }
    public double shortsGoldFormula(int level) {
        double baseGold = 10.0;
        for (int i = 1; i < level; i++)
            baseGold += baseGold * 0.5;
        return baseGold;
    }

    public Long shoesCapFormula(int level) {
        Long l = Long.valueOf(level);
        return l*1000;
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

    public void uiSetGold(double gold) {
        int ctr = -1;
        double tempGold = gold;
        while (tempGold>1000) {
            ctr++;
            tempGold/=1000;
        }
        if (ctr!=-1) tv_gold.setText(String.format("%.2f", tempGold) + goldFormat[ctr]);
        else tv_gold.setText(String.format("%.2f", gold));
    }

    public int randomVal(){
        int min, max;
        min = 0;
        max = randSteps.length - 1;
        return (int) ((Math.random() * (max - min)) + min);
    }

    private void registerService() {
        Intent serviceIntent = new Intent(this, SensorService.class);
        stopService(serviceIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else startService(serviceIntent);
    }

    private void stopService() {
        Intent intent = new Intent();
        intent.setAction("com.mobdeve.cactus.mobdevemp.destroyService");
        sendBroadcast(intent);
        Intent serviceIntent = new Intent(this, SensorService.class);
        stopService(serviceIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.setVisibility(View.VISIBLE);
        gameView.resume();
        SharedPreferences.Editor editor = sp.edit();
        userProgress.setCapshard(userProgress.getCapshard() + Integer.parseInt(sp.getString(user.getUsername() + " capShard", "0")));
        userProgress.setShirtshard(userProgress.getShirtshard() + Integer.parseInt(sp.getString(user.getUsername() + " shirtShard", "0")));
        userProgress.setShortshard(userProgress.getShortshard() + Integer.parseInt(sp.getString(user.getUsername() + " shortShard", "0")));
        userProgress.setShoeshard(userProgress.getShoeshard() + Integer.parseInt(sp.getString(user.getUsername() + " shoeShard", "0")));
        userProgress.setCapshard(userProgress.getCapshard() + Integer.parseInt(sp.getString(user.getUsername() + " capShard2", "0")));
        userProgress.setShirtshard(userProgress.getShirtshard() + Integer.parseInt(sp.getString(user.getUsername() + " shirtShard2", "0")));
        userProgress.setShortshard(userProgress.getShortshard() + Integer.parseInt(sp.getString(user.getUsername() + " shortShard2", "0")));
        userProgress.setShoeshard(userProgress.getShoeshard() + Integer.parseInt(sp.getString(user.getUsername() + " shoeShard2", "0")));
        editor.remove(user.getUsername() + " capShard");
        editor.remove(user.getUsername() + " shirtShard");
        editor.remove(user.getUsername() + " shortShard");
        editor.remove(user.getUsername() + " shoeShard");
        editor.remove(user.getUsername() + " capShard2");
        editor.remove(user.getUsername() + " shirtShard2");
        editor.remove(user.getUsername() + " shortShard2");
        editor.remove(user.getUsername() + " shoeShard2");
        editor.apply();
        refreshData();
        refreshGold();
        resumeTime = System.currentTimeMillis();
        if (resumeTime > pauseTime) {
            long diffInSeconds = (resumeTime - pauseTime)/1000;
            if (userProgress.getShirtlvl()!=0) {
                gold +=  shirtFormula(userProgress.getShirtlvl()) * diffInSeconds;
                uiSetGold(gold);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
        timer.cancel();
        userProgress.setGold(gold);
        user.setLevel(user.getLevel());
        user.setCurrentExp(progressBar.getProgress());
        saveData();
        pauseTime = System.currentTimeMillis();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        userProgress.setGold(gold);
        user.setLevel(user.getLevel());
        user.setCurrentExp(progressBar.getProgress());
        saveData();
        timer.cancel();
        Intent intent = new Intent();
        intent.putExtra("gold", userProgress.getGold());
        intent.putExtra("rate", shortsGoldFormula(userProgress.getShortlvl()));
        intent.setAction("com.mobdeve.cactus.mobdevemp.service");
        sendBroadcast(intent);
        unregisterReceiver(broadcastReceiver);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(user.getUsername() + " tapCount", Integer.toString(tapCount));
        editor.putString(user.getUsername() + " walkCount", Integer.toString(walkCount));
        editor.apply();
        super.onDestroy();
    }
}
