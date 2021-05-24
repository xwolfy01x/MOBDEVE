package com.mobdeve.cactus.mobdevemp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobdeve.cactus.mobdevemp.dao.ProgressDAOSQLImpl;
import com.mobdeve.cactus.mobdevemp.dao.UserDAOSQLImpl;
import com.mobdeve.cactus.mobdevemp.models.Progress;
import com.mobdeve.cactus.mobdevemp.models.User;

import java.util.Timer;
import java.util.TimerTask;

public class MarketActivity extends AppCompatActivity {

    private ImageView back_button;
    private TextView tv_gold2, tv_x1, tv_x10, tv_x50, tv_x100, tv_xn;
    private TextView price1, price2, price3, price4;
    private ConstraintLayout cl1, cl2, cl3, cl4;
    double capPrice, shirtPrice, shortPrice, shoePrice;
    UserDAOSQLImpl userDB;
    ProgressDAOSQLImpl progressDB;
    User user;
    Progress userProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        userDB = new UserDAOSQLImpl(getApplicationContext());
        progressDB = new ProgressDAOSQLImpl(getApplicationContext());

        init();
    }
    public void init() {
        back_button = (ImageView) findViewById(R.id.back_button);
        tv_gold2 = (TextView) findViewById(R.id.tv_gold2);
        tv_x1 = (TextView) findViewById(R.id.tv_x1);
        tv_x10 = (TextView) findViewById(R.id.tv_x10);
        tv_x50 = (TextView) findViewById(R.id.tv_x50);
        tv_x100 = (TextView) findViewById(R.id.tv_x100);
        tv_xn = (TextView) findViewById(R.id.tv_xn);
        cl1 = (ConstraintLayout) findViewById(R.id.cl1);
        cl2 = (ConstraintLayout) findViewById(R.id.cl2);
        cl3 = (ConstraintLayout) findViewById(R.id.cl3);
        cl4 = (ConstraintLayout) findViewById(R.id.cl4);
        price1 = (TextView) findViewById(R.id.textView2);
        price2 = (TextView) findViewById(R.id.textView3);
        price3 = (TextView) findViewById(R.id.textView4);
        price4 = (TextView) findViewById(R.id.textView5);

        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        user = userDB.getUser(sp.getString("username", ""));
        userProgress = progressDB.getOneProgress(user.getUsername());

        back_button.setOnClickListener(v -> {
            finish();
        });

        tv_gold2.setText(String.format("%.2f", userProgress.getGold()));

        tv_x1.setOnClickListener(v-> {
            restoreInactive();
            changePrice(1);
            tv_x1.setTextColor(Color.WHITE);
        });

        tv_x10.setOnClickListener(v-> {
            restoreInactive();
            changePrice(10);
            tv_x10.setTextColor(Color.WHITE);
        });

        tv_x50.setOnClickListener(v-> {
            restoreInactive();
            changePrice(50);
            tv_x50.setTextColor(Color.WHITE);
        });

        tv_x100.setOnClickListener(v-> {
            restoreInactive();
            changePrice(100);
            tv_x100.setTextColor(Color.WHITE);
        });

        tv_xn.setOnClickListener(v-> {
            restoreInactive();

            capPrice = Math.abs(userProgress.getCapshard() - (10+userProgress.getCaplvl()*5)) * 100;
            shirtPrice = Math.abs(userProgress.getShirtshard() - (10+userProgress.getShirtlvl()*5)) * 100;
            shortPrice = Math.abs(userProgress.getShortshard() - (10+userProgress.getShortlvl()*5)) * 100;
            shoePrice = Math.abs(userProgress.getShoeshard() - (10+userProgress.getShoelvl()*5)) * 100;

            price1.setText(Double.toString(capPrice));
            price2.setText(Double.toString(shirtPrice));
            price3.setText(Double.toString(shortPrice));
            price4.setText(Double.toString(shoePrice));

            reflectChanges();
            tv_xn.setTextColor(Color.WHITE);
        });

        cl1.setOnClickListener(v -> {
            userProgress.setGold(userProgress.getGold() - capPrice);
            userProgress.setCapshard(userProgress.getCapshard() + (int) capPrice/100);
            saveData();
            reflectChanges();
        });

        cl2.setOnClickListener(v -> {
            userProgress.setGold(userProgress.getGold() - shirtPrice);
            userProgress.setShirtshard(userProgress.getShirtshard() + (int) shirtPrice/100);
            saveData();
            reflectChanges();
        });

        cl3.setOnClickListener(v -> {
            userProgress.setGold(userProgress.getGold() - shortPrice);
            userProgress.setShortshard(userProgress.getShortshard() + (int) shortPrice/100);
            saveData();
            reflectChanges();
        });

        cl4.setOnClickListener(v -> {
            userProgress.setGold(userProgress.getGold() - shoePrice);
            userProgress.setShoeshard(userProgress.getShoeshard() + (int) shoePrice/100);
            saveData();
            reflectChanges();
        });
    }

    public void restoreInactive() {
        tv_x1.setTextColor(Color.WHITE);
        tv_x10.setTextColor(Color.WHITE);
        tv_x50.setTextColor(Color.WHITE);
        tv_x100.setTextColor(Color.WHITE);
        tv_xn.setTextColor(Color.WHITE);
    }

    public void changePrice(int quantity) {
        price1.setText(Integer.toString(quantity*100));
        price2.setText(Integer.toString(quantity*100));
        price3.setText(Integer.toString(quantity*100));
        price4.setText(Integer.toString(quantity*100));
        reflectChanges();
    }

    public void reflectChanges() {
        if (userProgress.getGold()<capPrice) {
            cl1.setEnabled(false);
            price1.setBackgroundResource(R.drawable.market_rectangle_cannotbuy);
        } else {
            cl1.setEnabled(true);
            price1.setBackgroundResource(R.drawable.market_rectangle_buy2);
        }

        if (userProgress.getGold() < shirtPrice) {
            cl2.setEnabled(false);
            price2.setBackgroundResource(R.drawable.market_rectangle_cannotbuy);
        } else {
            cl2.setEnabled(true);
            price2.setBackgroundResource(R.drawable.market_rectangle_2);
        }

        if (userProgress.getGold() < shortPrice) {
            cl3.setEnabled(false);
            price3.setBackgroundResource(R.drawable.market_rectangle_cannotbuy);
        } else {
            cl3.setEnabled(true);
            price3.setBackgroundResource(R.drawable.market_rectangle_2);
        }

        if (userProgress.getGold() < shoePrice) {
            cl4.setEnabled(false);
            price4.setBackgroundResource(R.drawable.market_rectangle_cannotbuy);
        } else {
            cl4.setEnabled(true);
            price4.setBackgroundResource(R.drawable.market_rectangle_2);
        }
    }

    public void saveData() {
        progressDB.updateProgress(userProgress);
        userDB.updateUser(user);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}