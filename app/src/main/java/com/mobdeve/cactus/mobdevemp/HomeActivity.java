package com.mobdeve.cactus.mobdevemp;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

public class HomeActivity extends AppCompatActivity {
    private TextView tv_gold;
    private SurfaceView sv_runningman;
    private GameView gameView;
    private ViewGroup.LayoutParams lp;
    private ConstraintLayout cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tv_gold = (TextView) findViewById(R.id.tv_gold);
        gameView = new GameView(this);
        cl = (ConstraintLayout) findViewById(R.id.cl);
        cl.addView(gameView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
    }


    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }
}
