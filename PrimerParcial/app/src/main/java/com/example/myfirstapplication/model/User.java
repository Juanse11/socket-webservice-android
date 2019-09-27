package com.example.myfirstapplication.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "user_name")
    public String userName;

    @ColumnInfo(name = "email")
    public String userEmail;

    @ColumnInfo(name = "password")
    public String password;
}