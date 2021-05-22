package com.mobdeve.cactus.mobdevemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;


public class StatisticsActivity extends AppCompatActivity {

    private ImageView btn_toBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        init();
    }

    private void init(){
//        btn_toBack = (ImageView) findViewById(R.id.to_back1);

        btn_toBack.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(i);
        });
    }
}