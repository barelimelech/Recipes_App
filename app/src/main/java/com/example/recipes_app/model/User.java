package com.example.recipes_app.model;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

public class User {

    final public static String COLLECTION_NAME = "users";

    @PrimaryKey
    @NonNull
    String username;
    String password;
    String birthday;
    Long updateDate = new Long(0);

    public User(){}

    public User(String username, String password, String birthday){
        this.username = username;
        this.password = password;
        this.birthday = birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }


    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("username",username);
        json.put("password",password);
        json.put("birthday",birthday);
        json.put("updateDate", FieldValue.serverTimestamp());

        return json;
    }
    public static User create(Map<String, Object> json) {
        String username = (String) json.get("username");
        String password = (String) json.get("password");
        String birthday = (String) json.get("birthday");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();


        User user = new User(username,password,birthday);
        user.setUpdateDate(updateDate);
        return user;
    }


    //TODO:....
    public Long getUpdateDate() {
        return updateDate;
    }
}
