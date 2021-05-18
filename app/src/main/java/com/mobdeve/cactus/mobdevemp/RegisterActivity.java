package com.mobdeve.cactus.mobdevemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.mobdeve.cactus.mobdevemp.dao.UserDAOSQLImpl;
import com.mobdeve.cactus.mobdevemp.models.User;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_register2, btn_login2;
    private EditText et_name1, et_userName1, et_pass1;
    UserDAOSQLImpl databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseAccess = new UserDAOSQLImpl(getApplicationContext());
        init();
    }

    public void init() {
        btn_register2 = (Button) findViewById(R.id.btn_register2);
        btn_login2 = (Button) findViewById(R.id.btn_login2);
        et_name1 = (EditText) findViewById(R.id.et_name1);
        et_userName1 = (EditText) findViewById(R.id.et_userName1);
        et_pass1 = (EditText) findViewById(R.id.et_pass1);

        btn_login2.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btn_register2.setOnClickListener(view -> {
            User newUser = new User(et_name1.getText().toString(), et_userName1.getText().toString(), et_pass1.getText().toString());
            databaseAccess.addUser(newUser);
        });
    }
}