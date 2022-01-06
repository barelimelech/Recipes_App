package com.example.recipes_app.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {


    @Query("select * from User")
    List<User> getAll();

//    @Query("select username1 from User where username1 = User.username ")
//    User getUser(String username1);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... users);

    @Delete
    void delete(User user);

//    @Query("select * from User")
//    List<Recipe> getAllUserRecipes();
}