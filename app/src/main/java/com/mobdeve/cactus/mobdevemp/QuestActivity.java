package com.mobdeve.cactus.mobdevemp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.cactus.mobdevemp.models.Progress;
import com.mobdeve.cactus.mobdevemp.models.User;

import java.util.Calendar;

public class QuestActivity extends AppCompatActivity {
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
    private TextView tv_taskquota2, tv_taskquota3;
    private TextView tv_task2, tv_task3;
    private ImageView iv_quest1, iv_quest2, iv_quest3;
    private ImageView back_button;
    private String quest1_collected, quest2_collected, quest3_collected;
    private TextView complete1, complete2, complete3;
    private TextView completed1, completed2, completed3;
    int capShard2, shirtShard2, shortShard2, shoeShard2;
    private SharedPreferences sp;
    private SharedPreferences.Editor ed;

    User user;
    Progress userProgress;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);
        init();
    }

    public void init() {
        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
        userProgress = (Progress) bundle.getSerializable("userProgress");
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        ed = sp.edit();
        iv_quest1 = (ImageView) findViewById(R.id.iv_quest1);
        iv_quest2 = (ImageView) findViewById(R.id.iv_quest2);
        iv_quest3 = (ImageView) findViewById(R.id.iv_quest3);
        tv_task2 = (TextView) findViewById(R.id.tv_task2);
        tv_task3 = (TextView) findViewById(R.id.tv_task3);
        tv_taskquota2 = (TextView) findViewById(R.id.tv_taskquota2);
        tv_taskquota3 = (TextView) findViewById(R.id.tv_taskquota3);
        completed1 = (TextView) findViewById(R.id.tv_showComplete1);
        completed2 = (TextView) findViewById(R.id.tv_showComplete2);
        completed3 = (TextView) findViewById(R.id.tv_showComplete3);
        complete1 = (TextView) findViewById(R.id.tv_questComplete1);
        complete2 = (TextView) findViewById(R.id.tv_questComplete2);
        complete3 = (TextView) findViewById(R.id.tv_questComplete3);
        back_button = (ImageView) findViewById(R.id.back_button10);
        capShard2 = 0;
        shirtShard2 = 0;
        shortShard2 = 0;
        shoeShard2 = 0;
        quest1_collected = sp.getString(user.getUsername() + " quest1", "false");
        quest2_collected = sp.getString(user.getUsername() + " quest2", "false");
        quest3_collected = sp.getString(user.getUsername() + " quest3", "false");
        checkResetTime();
        checkCompleted();
        checkQuests();

        iv_quest1.setOnClickListener(v -> {
            if(quest1_collected.equalsIgnoreCase("true")){
                Toast.makeText(getApplicationContext(), "You have completed this quest today!", Toast.LENGTH_SHORT).show();
            } else {
                claimLootBox();
                complete1.setVisibility(View.GONE);
                completed1.setVisibility(View.VISIBLE);
                iv_quest1.setEnabled(false);
                quest1_collected = "true";
                Toast.makeText(this, "Quest Completed! Received a lootbox!", Toast.LENGTH_SHORT).show();
                ed.putString(user.getUsername() + " quest1", "true");
                ed.apply();
            }
        });

        iv_quest2.setOnClickListener(v -> {
            if(quest2_collected.equalsIgnoreCase("true")){
                Toast.makeText(getApplicationContext(), "You have completed this quest today!", Toast.LENGTH_SHORT).show();
            }else {
                claimLootBox();
                complete2.setVisibility(View.GONE);
                complete2.setVisibility(View.VISIBLE);
                iv_quest2.setEnabled(false);
                quest2_collected = "true";
                Toast.makeText(this, "Quest Completed! Received a lootbox!", Toast.LENGTH_SHORT).show();
                ed.putString(user.getUsername() + " quest2", "true");
                ed.apply();
            }
        });

        iv_quest3.setOnClickListener(v -> {
            if(quest3_collected.equalsIgnoreCase("true")){
                Toast.makeText(getApplicationContext(), "You have completed this quest today!", Toast.LENGTH_SHORT).show();
            }else {
                claimLootBox();
                complete3.setVisibility(View.GONE);
                complete3.setVisibility(View.VISIBLE);
                iv_quest3.setEnabled(false);
                quest3_collected = "true";
                Toast.makeText(this, "Quest Completed! Received a lootbox!", Toast.LENGTH_SHORT).show();
                ed.putString(user.getUsername() + " quest3", "true");
                ed.apply();
            }
        });
        back_button.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction("com.mobdeve.quest");
            intent.putExtra("capShard2", capShard2);
            intent.putExtra("shirtShard2", shirtShard2);
            intent.putExtra("shortShard2", shortShard2);
            intent.putExtra("shoeShard2", shoeShard2);
            sendBroadcast(intent);
            capShard2 = 0;
            shirtShard2 = 0;
            shortShard2 = 0;
            shoeShard2 = 0;
            finish();
        });
    }

    public void checkQuests(){
        String taps = sp.getString(user.getUsername() + " tapCount", "0");
        String step = sp.getString(user.getUsername() + " walkCount", "0");

        int tempTap = Integer.parseInt(taps);
        int tempWalk = Integer.parseInt(step);

        if(tempTap >= 1000 && quest2_collected.equalsIgnoreCase("true")){
            iv_quest2.setEnabled(true);
            complete2.setVisibility(View.VISIBLE);
        } else {
            iv_quest2.setEnabled(false);
            complete2.setVisibility(View.GONE);
        }

        if(tempWalk >= 100 && quest3_collected.equalsIgnoreCase("true")){
            iv_quest3.setEnabled(true);
            complete3.setVisibility(View.VISIBLE);
        } else {
            iv_quest3.setEnabled(false);
            complete3.setVisibility(View.GONE);
        }

        tv_taskquota2.setText(tempTap + "/1000");
        tv_taskquota3.setText(tempWalk+"/100");
    }

    public void checkCompleted(){
        String check1,check2,check3;
        check1 = sp.getString(user.getUsername() + " quest1", "false");
        check2 = sp.getString(user.getUsername() + " quest2", "false");
        check3 = sp.getString(user.getUsername() + " quest3", "false");

        if(check1.equalsIgnoreCase("true")){
            complete1.setVisibility(View.GONE);
            completed1.setVisibility(View.VISIBLE);
        }
        if(check2.equalsIgnoreCase("true")){
            complete2.setVisibility(View.GONE);
            completed2.setVisibility(View.VISIBLE);
        }
        if(check3.equalsIgnoreCase("true")){
            complete3.setVisibility(View.GONE);
            completed3.setVisibility(View.VISIBLE);
        }
    }

    public void claimLootBox(){
        String[] fields = lootBoxRewards[randomNumber()].split("-");
        if(fields[1].equalsIgnoreCase("Cap Shard")) capShard2 += Integer.parseInt(fields[0]) * user.getLevel();
        else if(fields[1].equalsIgnoreCase("Shirt Shard")) shirtShard2 += Integer.parseInt(fields[0]) * user.getLevel();
        else if(fields[1].equalsIgnoreCase("Short Shard")) shortShard2 += Integer.parseInt(fields[0]) * user.getLevel();
        else shoeShard2 += Integer.parseInt(fields[0]) * user.getLevel();
    }

    public int randomNumber(){
        int min, max;
        min = 0;
        max = lootBoxRewards.length - 1;
        return (int) ((Math.random() * (max - min)) + min);
    }

    public void checkResetTime(){ ;
        //if time in shared preferences has passed 00:00, reset everything
        iv_quest2.setEnabled(false);
        iv_quest3.setEnabled(false);
        complete1.setVisibility(View.VISIBLE);
        completed1.setVisibility(View.GONE);
        complete2.setVisibility(View.VISIBLE);
        completed2.setVisibility(View.GONE);
        complete3.setVisibility(View.VISIBLE);
        completed3.setVisibility(View.GONE);
    }
}

