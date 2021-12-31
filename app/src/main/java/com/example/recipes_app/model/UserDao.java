//package com.example.recipes_app.model;
//
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.OnConflictStrategy;
//import androidx.room.Query;
//
//import java.util.List;
//
//public class UserDao {
//
//
//    @Query("select * from User")
//    List<User> getAll();
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertAll(User... recipes);
//
//    @Delete
//    void delete(User recipe);
//}
