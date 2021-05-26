package com.mobdeve.cactus.mobdevemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobdeve.cactus.mobdevemp.dao.ProgressDAOSQLImpl;
import com.mobdeve.cactus.mobdevemp.dao.UserDAOSQLImpl;
import com.mobdeve.cactus.mobdevemp.models.Progress;
import com.mobdeve.cactus.mobdevemp.models.User;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_register2, btn_login2;
    private EditText et_name1, et_userName1, et_pass1;
    SharedPreferences sp;
    UserDAOSQLImpl databaseAccess;
    ProgressDAOSQLImpl progressDB;
    ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseAccess = new UserDAOSQLImpl(getApplicationContext());
        progressDB = new ProgressDAOSQLImpl(getApplicationContext());
        init();
    }

    public void init() {
        btn_register2 = (Button) findViewById(R.id.btn_register2);
        btn_login2 = (Button) findViewById(R.id.btn_login2);
        et_name1 = (EditText) findViewById(R.id.et_name1);
        et_userName1 = (EditText) findViewById(R.id.et_userName1);
        et_pass1 = (EditText) findViewById(R.id.et_pass1);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        btn_login2.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btn_register2.setOnClickListener(view -> {
            userList = databaseAccess.getUsers();
            sp = getSharedPreferences("user", Context.MODE_PRIVATE);
            if (userList.size() == 0) {
                ed.putString("username", et_userName1.getText().toString());
                ed.apply();
                User newUser = new User(et_name1.getText().toString(), et_userName1.getText().toString(), et_pass1.getText().toString());
                databaseAccess.addUser(newUser);
                Progress newProgress = new Progress(et_userName1.getText().toString());
                progressDB.initializeProgress(newProgress);
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                int found = 0;
                for(int i = 0; i < userList.size();i++){
                    String toCheck = et_userName1.getText().toString();
                    String temp = userList.get(i).getUsername();
                    if(toCheck.equalsIgnoreCase(temp)){
                        found = 1;
                        break;
                    }
                }
                if (found == 0) {
                    ed.putString("username", et_userName1.getText().toString());
                    ed.apply();
                    User newUser = new User(et_name1.getText().toString(), et_userName1.getText().toString(), et_pass1.getText().toString());
                    databaseAccess.addUser(newUser);
                    Progress newProgress = new Progress(et_userName1.getText().toString());
                    progressDB.initializeProgress(newProgress);
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else Toast.makeText(getApplicationContext(), "Username already exists!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}