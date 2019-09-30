package com.example.myfirstapplication.auth;

import com.example.myfirstapplication.model.User;

public class LoggedInUserManager {
    public static User loggedInUser;

    public static User getUser(){
        return loggedInUser;
    }

    public static void setUser(User user){
        loggedInUser = user;
    }

}
