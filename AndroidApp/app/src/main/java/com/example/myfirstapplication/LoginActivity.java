package com.example.myfirstapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapplication.auth.AuthenticationManager;
import com.example.myfirstapplication.auth.AuthenticationManagerInterface;
import com.example.myfirstapplication.auth.LoggedInUserManager;
import com.example.myfirstapplication.model.User;

public class LoginActivity extends Activity implements AuthenticationManagerInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);

        ((TextView) findViewById(R.id.signup_text_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onClick(View view) {
        String username = ((EditText) findViewById(R.id.login_user_name)).getText().toString();
        String password = ((EditText) findViewById(R.id.login_password)).getText().toString();
        AuthenticationManager authManager = new AuthenticationManager(this, this);
        authManager.logInUser(username, password);

    }

    @Override
    public void authResult(boolean isSuccessful, final String message, User user) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
        if (isSuccessful){
            LoggedInUserManager.setUser(user);
            Intent intetToBecalled = new Intent(this, MainActivity.class);
            startActivity(intetToBecalled);
        }else{

        }
    }

    @Override
    public void errorException(Exception e) {

    }
}
