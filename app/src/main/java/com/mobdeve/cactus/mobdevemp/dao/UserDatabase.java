package com.mobdeve.cactus.mobdevemp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLEUSER = "table_user";
    public static final String USER_ID = "id";
    public static final String USER_NAME = "name";
    public static final String USER_USERNAME = "username";
    public static final String USER_PASSWORD = "password";

    private static final String CREATE_USER_TABLE =
            "create table " + TABLEUSER +
                    "("
                    + USER_ID + " integer primary key autoincrement, "
                    + USER_NAME + " text, "
                    + USER_USERNAME + " text, "
                    + USER_PASSWORD + " text ); ";

    public UserDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(" DROP TABLE IF EXISTS " + TABLEUSER);
        onCreate(db);
    }

}
