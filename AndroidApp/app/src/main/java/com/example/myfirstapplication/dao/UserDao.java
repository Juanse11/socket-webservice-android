package com.example.myfirstapplication.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myfirstapplication.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("select * from User")
    List<User> getAll();

    @Query("select * from User  WHERE Id IN (:Ids)")
    List<User> loadAllByIds(int[] Ids);

    @Query("select * from User  WHERE email = :emails")
    List<User> getUserbyEmail(String emails);

    @Query("select * from User  WHERE username = :userName")
    List<User> getUserbyUserName(String userName);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}