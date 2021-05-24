package com.mobdeve.cactus.mobdevemp.dao;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobdeve.cactus.mobdevemp.models.User;

import java.io.Serializable;
import java.util.ArrayList;

public class UserDAOSQLImpl implements UserDAO, Serializable {

    private SQLiteDatabase database;
    private UserDatabase userDatabase;

    public UserDAOSQLImpl(Context context) {
        userDatabase = new UserDatabase(context);
    }

    @Override
    public ArrayList<User> getUsers() {
        ArrayList<User> result = new ArrayList<User>();
        database = userDatabase.getReadableDatabase();

        String[] columns = {UserDatabase.USER_NAME, UserDatabase.USER_USERNAME, UserDatabase.USER_PASSWORD, UserDatabase.USER_LEVEL, UserDatabase.USER_CURREXP};

        Cursor cursor = database.query(
                UserDatabase.TABLEUSER,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            User temp = new User(cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("username")), cursor.getString(cursor.getColumnIndex("password")));
            temp.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
            temp.setCurrentExp(cursor.getInt(cursor.getColumnIndex("current_exp")));
            result.add(temp);
            cursor.moveToNext();
        }

        cursor.close();
        database.close();
        return result;
    }

    public User getUser(String userName) {
        database = userDatabase.getReadableDatabase();
        String QUERY = "SELECT * FROM " + UserDatabase.TABLEUSER + " WHERE " + UserDatabase.USER_USERNAME + " = ?";

        Cursor cursor = database.rawQuery(QUERY, new String[] { userName });
        User user = null;
        while (cursor != null) {
            cursor.moveToFirst();
            User temp = new User(cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("username")), cursor.getString(cursor.getColumnIndex("password")));
            temp.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
            temp.setCurrentExp(cursor.getInt(cursor.getColumnIndex("current_exp")));
            return temp;
        }
        cursor.close();
        database.close();
        return user;
    }

    public void addUser(User newUser) {
        database = userDatabase.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(UserDatabase.USER_NAME, newUser.getName());
        values.put(UserDatabase.USER_USERNAME, newUser.getUsername());
        values.put(UserDatabase.USER_PASSWORD, newUser.getPassword());
        values.put(UserDatabase.USER_LEVEL, newUser.getLevel());
        values.put(UserDatabase.USER_CURREXP, newUser.getCurrentExp());

        long entry = database.insert(UserDatabase.TABLEUSER, null, values);
        database.close();
    }

    public void updateUser(User existingUser) {
        database = userDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(UserDatabase.USER_LEVEL, existingUser.getLevel());
        values.put(UserDatabase.USER_CURREXP, existingUser.getCurrentExp());

        long entry = database.update(UserDatabase.TABLEUSER, values, UserDatabase.USER_USERNAME + " = ?", new String[] {existingUser.getUsername()});
        database.close();
    }
}