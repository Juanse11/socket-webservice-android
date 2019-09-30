package com.example.myfirstapplication.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseManager {

    public static AppDatabase appDatabase;

    public static AppDatabase getInstance(final Context context) {
        if (appDatabase == null) {
            synchronized (AppDatabase.class) {
                if (appDatabase == null) {
                    appDatabase = Room.databaseBuilder(context,
                            AppDatabase.class, "gps-database").
                            fallbackToDestructiveMigration().build();
                }
            }
        }
        return appDatabase;
    }
}
