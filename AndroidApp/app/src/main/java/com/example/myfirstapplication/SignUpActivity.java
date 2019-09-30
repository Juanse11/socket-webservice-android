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

public class SignUpActivity extends Activity implements AuthenticationManagerInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        ((TextView) findViewById(R.id.login_text_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void authResult(boolean isSuccessful, String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void errorException(Exception e) {

    }

    public void onClick(View view) {
        String username = ((EditText) findViewById(R.id.signup_username)).getText().toString();
        String password = ((EditText) findViewById(R.id.signup_password)).getText().toString();
        String email = ((EditText) findViewById(R.id.signup_email)).getText().toString();
        AuthenticationManager authManager = new AuthenticationManager(this, this);
        authManager.signUpUser(username,email,password);
        Intent intetToBecalled = new Intent(this, MainActivity.class);
        startActivity(intetToBecalled);
    }
}
