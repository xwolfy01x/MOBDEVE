package com.mobdeve.cactus.mobdevemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobdeve.cactus.mobdevemp.dao.UserDAOSQLImpl;
import com.mobdeve.cactus.mobdevemp.models.User;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private ArrayList<User> userList;
    private Button btn_register3, btn_login3;
    private EditText et_userName2, et_pass2;
    UserDAOSQLImpl databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseAccess = new UserDAOSQLImpl(getApplicationContext());
        init();
    }

    public void init() {
        btn_register3 = (Button) findViewById(R.id.btn_register3);
        btn_login3 = (Button) findViewById(R.id.btn_login3);

        et_userName2 = (EditText) findViewById(R.id.et_userName2);
        et_pass2 = (EditText) findViewById(R.id.et_pass2);

        btn_register3.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        btn_login3.setOnClickListener(v -> {
            userList = databaseAccess.getUsers();
            int found = 0;
            for(int i = 0; i < userList.size(); i++){
                String user = et_userName2.getText().toString();
                String pass = et_pass2.getText().toString();

                if(user.equalsIgnoreCase(userList.get(i).getUsername()) && pass.equalsIgnoreCase(userList.get(i).getPassword())) {
                    SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString("username", userList.get(i).getUsername());
                    found = 1;
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                }
            }
            if (found == 0) Toast.makeText(getApplicationContext(), "Invalid Username or Password!", Toast.LENGTH_SHORT).show();


        });
    }
}