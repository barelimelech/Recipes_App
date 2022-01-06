package com.example.recipes_app.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserRecipeDao {

    @Query("select * from UserRecipe")
    List<UserRecipe> getAll();

//    @Query("select username from User where username = User.username ")
//    User getUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(UserRecipe... userRecipe);

    @Delete
    void delete(UserRecipe userRecipe);
}