package com.mobdeve.cactus.mobdevemp.dao;

import com.mobdeve.cactus.mobdevemp.models.User;

import java.util.ArrayList;

public interface UserDAO {
    ArrayList<User> getUsers();
    void addUser(User newUser);
}
