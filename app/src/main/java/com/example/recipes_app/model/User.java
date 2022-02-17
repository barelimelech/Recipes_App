package com.example.recipes_app.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User {

    final public static String COLLECTION_NAME = "users";

    @PrimaryKey
    @NonNull
    String fullName;
    String email;
    String phone;
    String uId;
    String isConnected = "false";
    String userUrl;

    Long updateDate = new Long(0);

    @Ignore
    public User(){}

    public User(String fullName, String phone, String email,String uId){
        this.fullName = fullName;
        this.email = email;
        this.uId = uId;
        this.phone = phone;
    }


    @NonNull
    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getIsConnected() {
        return isConnected;
    }

    public String getUId() {
        return uId;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsConnected(String isConnected) {
        this.isConnected = isConnected;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("fullName",fullName);
        json.put("phone",phone);
        json.put("email",email);
        json.put("uId",uId);
        json.put("updateDate", FieldValue.serverTimestamp());
        json.put("isConnected",isConnected);
        json.put("userUrl", userUrl);

        return json;
    }
    public static User create(Map<String, Object> json) {
        String fullName = (String) json.get("fullName");
        String email = (String) json.get("email");
        String uId = (String) json.get("uId");
        String isConnected = (String) json.get("isConnected");
        String phone = (String) json.get("phone");
        String userUrl = (String) json.get("userUrl");

        User user = new User(fullName,phone,email,uId);
        user.setIsConnected(isConnected);
        user.setUserUrl(userUrl);
        return user;
    }

    public Long getUpdateDate() {
        return updateDate;
    }
}