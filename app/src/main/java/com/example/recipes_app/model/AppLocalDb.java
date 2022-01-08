package com.example.recipes_app.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.recipes_app.MyApplication;

@Database(entities = {Recipe.class, User.class, UserRecipe.class}, version = 13)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract RecipeDao recipeDao();
    public abstract UserDao userDao();
    public abstract UserRecipeDao userRecipeDao();
}

public class AppLocalDb{
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}

