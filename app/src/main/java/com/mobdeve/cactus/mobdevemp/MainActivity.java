package com.mobdeve.cactus.mobdevemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btn_toLogin;
    private Button btn_toRegister;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        String check = sp.getString("username", null);
        if(check!= null){
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
            finish();
        } else {
            init();
        }
    }
    public void init() {
        btn_toLogin = (Button) findViewById(R.id.login);
        btn_toRegister = (Button) findViewById(R.id.register);

        btn_toLogin.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        });

        btn_toRegister.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(i);
        });
    }
}