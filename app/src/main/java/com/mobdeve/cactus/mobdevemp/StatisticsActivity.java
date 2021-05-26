package com.mobdeve.cactus.mobdevemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobdeve.cactus.mobdevemp.models.Progress;
import com.mobdeve.cactus.mobdevemp.models.User;


public class StatisticsActivity extends AppCompatActivity {

    private ImageView btn_toBack;
    private TextView tv_plvl, tv_gwalk, tv_gtap, tv_gps, tv_xptap, tv_xpwalk, tv_time;
    User user;
    Progress userProgress;
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
        setContentView(R.layout.activity_statistics);

        init();
    }

    private void init(){
        btn_toBack = (ImageView) findViewById(R.id.to_back1);
        tv_plvl = (TextView) findViewById(R.id.tv_plvl);
        tv_gwalk = (TextView) findViewById(R.id.tv_gwalk);
        tv_gps = (TextView) findViewById(R.id.tv_gps);
        tv_gtap = (TextView) findViewById(R.id.tv_gtap);
        tv_xptap = (TextView) findViewById(R.id.tv_xptap);
        tv_xpwalk = (TextView) findViewById(R.id.tv_xpwalk);
        tv_time = (TextView) findViewById(R.id.tv_time);

        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
        userProgress = (Progress) bundle.getSerializable("userProgress");

        tv_plvl.setText(Integer.toString(user.getLevel()));
        tv_gtap.setText(getGold(capGoldFormula(userProgress.getCaplvl())) + " per tap");
        tv_gps.setText(getGold(shirtFormula(userProgress.getShirtlvl())) + " per second");
        tv_gwalk.setText(getGold(shortsGoldFormula(userProgress.getShortlvl())) + " per step");
        tv_xptap.setText((int) capExpFormula(userProgress.getCaplvl()) + " per tap");
        tv_xpwalk.setText((int) shortsExpFormula(userProgress.getShortlvl()) + " per step");
        tv_time.setText(60000*userProgress.getShoelvl() + " minutes");

        btn_toBack.setOnClickListener(v -> {
            finish();
        });
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
        if (level == 0) return 0;
        for (int i = 1; i < level; i++)
            baseCoin += baseCoin * 0.5;
        return baseCoin;
    }

    public double shortsExpFormula(int level) {
        double baseExp = 2.0;
        if (level == 0) return 0;
        for (int i = 1; i < level; i++)
            baseExp += baseExp * 0.5;
        return baseExp;
    }
    public double shortsGoldFormula(int level) {
        double baseGold = 10.0;
        if (level == 0) return 0;
        for (int i = 1; i < level; i++)
            baseGold += baseGold * 0.5;
        return baseGold;
    }

    public String getGold(double gold) {
        int ctr = -1;
        double tempGold = gold;
        while (tempGold>1000) {
            ctr++;
            tempGold/=1000;
        }
        if (ctr!=-1) return  String.format("%.2f", tempGold) + goldFormat[ctr];
        else return String.format("%.2f", tempGold);
    }
}