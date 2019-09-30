package com.example.myfirstapplication.auth;

import com.example.myfirstapplication.model.User;

public interface AuthenticationManagerInterface {
    void authResult(boolean isSuccessful, String message, User user);
    void errorException(Exception e);
}
