package com.example.recipes_app.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.recipes_app.MyApplication;

@Database(entities = {Recipe.class}, version = 2)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract RecipeDao studentDao();
}

public class AppLocalDb{
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}

