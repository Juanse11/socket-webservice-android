package com.example.myfirstapplication.auth;

import android.content.Context;
import android.os.AsyncTask;

import com.example.myfirstapplication.database.AppDatabase;
import com.example.myfirstapplication.database.DatabaseManager;
import com.example.myfirstapplication.model.User;

import java.util.List;

public class AuthenticationManager implements AuthenticationManagerInterface {
    AppDatabase appDatabase;
    AuthenticationManagerInterface caller;
    Context context;

    public AuthenticationManager(Context context, AuthenticationManagerInterface caller) {
        this.context = context;
        this.appDatabase = DatabaseManager.getInstance(context);
        this.caller = caller;
    }

    public void logInUser(final String username, final String password){
        try{

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    User userFound=appDatabase.UserDao().getUserByUserName(username);
                    if (userFound != null){
                        if (userFound.password.equals(password)){
                            caller.authResult(true, "You are logged in succesfully");
                        }else{
                            caller.authResult(false, "Error. Check your credentials and try again.");
                        }
                    } else{
                        caller.authResult(false, "Error. The user doesn't exist.");
                    }
                }
            });

        }catch (Exception e){
            caller.authResult(false, e.getMessage());
        }
    }

    public void signUpUser(String username, String email, String password){
        try{
            final User newUser = new User();
            newUser.username = username;
            newUser.password = password;
            newUser.email = email;
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase.UserDao().insertAll(newUser);
                }
            });
            caller.authResult(true, "Succes. You have signed up correctly");
        }catch (Exception e){
            caller.authResult(false, e.getMessage());
        }

    }

    @Override
    public void authResult(boolean isSuccessful, String message) {

    }

    @Override
    public void errorException(Exception e) {

    }
}
