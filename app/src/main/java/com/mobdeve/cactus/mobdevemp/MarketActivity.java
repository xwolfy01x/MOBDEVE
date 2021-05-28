package com.mobdeve.cactus.mobdevemp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobdeve.cactus.mobdevemp.dao.ProgressDAOSQLImpl;
import com.mobdeve.cactus.mobdevemp.dao.UserDAOSQLImpl;
import com.mobdeve.cactus.mobdevemp.models.Progress;
import com.mobdeve.cactus.mobdevemp.models.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MarketActivity extends AppCompatActivity {

    private String[] lootBoxRewards = {
            "10-Cap Shards",
            "10-Shirt Shards",
            "10-Short Shards",
            "10-Shoe Shards",
            "20-Cap Shards",
            "20-Shirt Shards",
            "20-Short Shards",
            "20-Shoe Shards",
            "50-Cap Shards",
            "50-Shirt Shards",
            "50-Short Shards",
            "50-Shoe Shards",
    };
    private ImageView back_button;
    private TextView tv_gold2, tv_x1, tv_x10, tv_x50, tv_x100, tv_xn, tv_lootbox;
    private TextView price1, price2, price3, price4;
    private ConstraintLayout cl1, cl2, cl3, cl4;
    double capPrice, shirtPrice, shortPrice, shoePrice;
    UserDAOSQLImpl userDB;
    ProgressDAOSQLImpl progressDB;
    User user;
    double gold;
    Progress userProgress;
    String recDate;
    String currDate;
    int capShard, shirtShard, shortShard, shoeShard;
    int capShard2, shirtShard2, shortShard2, shoeShard2;
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
        setContentView(R.layout.activity_market);

        init();

        capPrice = 100;
        shirtPrice = 100;
        shortPrice = 100;
        shoePrice = 100;

        changePrice();
        reflectChanges();
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
        tv_lootbox = (TextView) findViewById(R.id.tv_lootBox);

        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);

        capShard = 0;
        shirtShard = 0;
        shortShard = 0;
        shoeShard = 0;
        capShard2 = 0;
        shirtShard2 = 0;
        shortShard2 = 0;
        shoeShard2 = 0;

        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
        userProgress = (Progress) bundle.getSerializable("userProgress");

        back_button.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction("com.mobdeve.marketbuy");
            intent.putExtra("capShard", capShard);
            intent.putExtra("shirtShard", shirtShard);
            intent.putExtra("shortShard", shortShard);
            intent.putExtra("shoeShard", shoeShard);
            intent.putExtra("capShard2", capShard2);
            intent.putExtra("shirtShard2", shirtShard2);
            intent.putExtra("shortShard2", shortShard2);
            intent.putExtra("shoeShard2", shoeShard2);
            sendBroadcast(intent);
            capShard = 0;
            shirtShard = 0;
            shortShard = 0;
            shoeShard = 0;
            capShard2 = 0;
            shirtShard2 = 0;
            shortShard2 = 0;
            shoeShard2 = 0;
            finish();
        });

        gold = getIntent().getExtras().getDouble("gold");
        uiSetGold(gold);

        tv_x1.setOnClickListener(v-> {
            restoreInactive();

            capPrice = 100;
            shirtPrice = 100;
            shortPrice = 100;
            shoePrice = 100;

            changePrice();
            tv_x1.setTextColor(Color.BLACK);
        });

        tv_x10.setOnClickListener(v-> {
            restoreInactive();

            capPrice = 10 * 100;
            shirtPrice = 10 * 100;
            shortPrice = 10 * 100;
            shoePrice = 10 * 100;

            changePrice();
            tv_x10.setTextColor(Color.BLACK);
        });

        tv_x50.setOnClickListener(v-> {
            restoreInactive();

            capPrice = 50 * 100;
            shirtPrice = 50 * 100;
            shortPrice = 50 * 100;
            shoePrice = 50 * 100;

            price1.setText(Double.toString(capPrice));
            price2.setText(Double.toString(shirtPrice));
            price3.setText(Double.toString(shortPrice));
            price4.setText(Double.toString(shoePrice));

            changePrice();
            tv_x50.setTextColor(Color.BLACK);
        });

        tv_x100.setOnClickListener(v-> {
            restoreInactive();

            capPrice = 100 * 100;
            shirtPrice = 100 * 100;
            shortPrice = 100 * 100;
            shoePrice = 100 * 100;

            changePrice();
            tv_x100.setTextColor(Color.BLACK);
        });

        tv_xn.setOnClickListener(v-> {
            restoreInactive();

            capPrice = Math.abs(userProgress.getCapshard() - (5+userProgress.getCaplvl()*5)) * 100;
            shirtPrice = Math.abs(userProgress.getShirtshard() - (5+userProgress.getShirtlvl()*5)) * 100;
            shortPrice = Math.abs(userProgress.getShortshard() - (5+userProgress.getShortlvl()*5)) * 100;
            shoePrice = Math.abs(userProgress.getShoeshard() - (5+userProgress.getShoelvl()*5)) * 100;

            changePrice();
            tv_xn.setTextColor(Color.BLACK);
        });

        price1.setOnClickListener(v -> {
            gold -= capPrice;
            capShard += (int) capPrice/100;
            uiSetGold(gold);
            reflectChanges();
        });

        price2.setOnClickListener(v -> {
            gold -= shirtPrice;
            shirtShard += (int) shirtPrice/100;
            uiSetGold(gold);
            reflectChanges();
        });

        price3.setOnClickListener(v -> {
            gold -= shortPrice;
            shortShard += (int) shortPrice/100;
            uiSetGold(gold);
            reflectChanges();
        });

        price4.setOnClickListener(v -> {
            gold -= shoePrice;
            shoeShard += (int) shoePrice/100;
            uiSetGold(gold);
            reflectChanges();
        });

        tv_lootbox.setOnClickListener(v -> {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            SharedPreferences.Editor ed = sp.edit();
            Date date = Calendar.getInstance().getTime();
            //get stored date and current date
            recDate = sp.getString(userProgress.getUsername() + " date", "");
            if(recDate.length() == 0) {
                //claimLootBox();
                recDate = dateFormat.format(date);
                ed.putString(userProgress.getUsername() + " date", recDate);
                ed.apply();
                claimLootBox();
                Toast.makeText(getApplicationContext(), "Loot box claimed!", Toast.LENGTH_SHORT).show();
            } else{
                currDate = dateFormat.format(date);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                try{
                    Date d1 = sdf.parse(currDate);
                    Date d2 = sdf.parse(recDate);

                    long diff = d1.getTime() - d2.getTime();
                    long diff_hrs = diff / (60 * 60 * 1000) % 24;

                    if(diff_hrs >= 24){
                        claimLootBox();
                        ed.putString(userProgress.getUsername() + " date", currDate);
                        ed.apply();
                        Toast.makeText(getApplicationContext(), "Loot box claimed!", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getApplicationContext(), "You have already claimed today!", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void claimLootBox(){
        String[] fields = lootBoxRewards[randomNumber()].split("-");
        if(fields[1].equalsIgnoreCase("Cap Shard")) capShard2 += Integer.parseInt(fields[0]) * user.getLevel();
        else if(fields[1].equalsIgnoreCase("Shirt Shard")) shirtShard2 += Integer.parseInt(fields[0]) * user.getLevel();
        else if(fields[1].equalsIgnoreCase("Short Shard")) shortShard2 += Integer.parseInt(fields[0]) * user.getLevel();
        else shortShard2 += Integer.parseInt(fields[0]) * user.getLevel();
    }

    public int randomNumber(){
        int min, max;
        min = 0;
        max = lootBoxRewards.length - 1;
        return (int) ((Math.random() * (max - min)) + min);
    }

    public void restoreInactive() {
        tv_x1.setTextColor(Color.WHITE);
        tv_x10.setTextColor(Color.WHITE);
        tv_x50.setTextColor(Color.WHITE);
        tv_x100.setTextColor(Color.WHITE);
        tv_xn.setTextColor(Color.WHITE);
    }

    public void changePrice() {
        price1.setText(String.format("%.2f",capPrice));
        price2.setText(String.format("%.2f",shirtPrice));
        price3.setText(String.format("%.2f",shortPrice));
        price4.setText(String.format("%.2f",shoePrice));
        reflectChanges();
    }

    public void reflectChanges() {
        if (gold < capPrice) {
            price1.setEnabled(false);
            price1.setBackgroundResource(R.drawable.market_rectangle_cannotbuy);
        } else {
            price1.setEnabled(true);
            price1.setBackgroundResource(R.drawable.market_rectangle_buy2);
        }

        if (gold < shirtPrice) {
            price2.setEnabled(false);
            price2.setBackgroundResource(R.drawable.market_rectangle_cannotbuy);
        } else {
            price2.setEnabled(true);
            price2.setBackgroundResource(R.drawable.market_rectangle_buy2);
        }

        if (gold < shortPrice) {
            price3.setEnabled(false);
            price3.setBackgroundResource(R.drawable.market_rectangle_cannotbuy);
        } else {
            price3.setEnabled(true);
            price3.setBackgroundResource(R.drawable.market_rectangle_buy2);
        }

        if (gold < shoePrice) {
            price4.setEnabled(false);
            price4.setBackgroundResource(R.drawable.market_rectangle_cannotbuy);
        } else {
            price4.setEnabled(true);
            price4.setBackgroundResource(R.drawable.market_rectangle_buy2);
        }
    }

    public void uiSetGold(double gold) {
        int ctr = -1;
        double tempGold = gold;
        while (tempGold>1000) {
            ctr++;
            tempGold/=1000;
        }
        if (ctr!=-1) tv_gold2.setText(String.format("%.2f", tempGold) + goldFormat[ctr]);
        else tv_gold2.setText(String.format("%.2f", gold));
    }

    @Override
    protected void onDestroy() {
        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(user.getUsername() + " capShard", Integer.toString(capShard));
        ed.putString(user.getUsername() + " shirtShard", Integer.toString(shirtShard));
        ed.putString(user.getUsername() + " shortShard", Integer.toString(shortShard));
        ed.putString(user.getUsername() + " shoeShard", Integer.toString(shoeShard));
        ed.putString(user.getUsername() + " capShard2", Integer.toString(capShard2));
        ed.putString(user.getUsername() + " shirtShard2", Integer.toString(shirtShard2));
        ed.putString(user.getUsername() + " shortShard2", Integer.toString(shortShard2));
        ed.putString(user.getUsername() + " shoeShard2", Integer.toString(shoeShard2));
        ed.apply();
        super.onDestroy();
    }
}