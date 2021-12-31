package com.example.recipes_app.model;

import java.util.HashMap;
import java.util.Map;

public class User {

    final public static String COLLECTION_NAME = "users";

    String username;
    String password;
    String birthday;

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


    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("username",username);
        json.put("password",password);
        json.put("birthday",birthday);

        return json;
    }
    public static User create(Map<String, Object> json) {
        String username = (String) json.get("username");
        String password = (String) json.get("password");
        String birthday = (String) json.get("birthday");


        User user = new User(username,password,birthday);
        return user;
    }
}
