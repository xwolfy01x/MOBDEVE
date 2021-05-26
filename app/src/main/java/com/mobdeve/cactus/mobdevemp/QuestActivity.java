package com.mobdeve.cactus.mobdevemp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuestActivity extends AppCompatActivity {

    private TextView tv_taskquota2, tv_taskquota3;
    private TextView tv_task2, tv_task3;
    private ImageView iv_quest1, iv_quest2, iv_quest3;
    private ImageView back_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        init();
    }

    public void init() {
        iv_quest1 = (ImageView) findViewById(R.id.iv_quest1);
        iv_quest2 = (ImageView) findViewById(R.id.iv_quest2);
        iv_quest3 = (ImageView) findViewById(R.id.iv_quest3);
        tv_task2 = (TextView) findViewById(R.id.tv_task2);
        tv_task3 = (TextView) findViewById(R.id.tv_task3);
        tv_taskquota2 = (TextView) findViewById(R.id.tv_taskquota2);
        tv_taskquota3 = (TextView) findViewById(R.id.tv_taskquota3);
        back_button = (ImageView) findViewById(R.id.back_button10);

        back_button.setOnClickListener(v -> {
            finish();
        });
    }
}
