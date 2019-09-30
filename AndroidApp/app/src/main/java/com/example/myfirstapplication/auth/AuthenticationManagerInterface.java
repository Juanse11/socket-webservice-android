package com.example.myfirstapplication.auth;

public interface AuthenticationManagerInterface {
    void authResult(boolean isSuccessful, String message);
    void errorException(Exception e);
}
