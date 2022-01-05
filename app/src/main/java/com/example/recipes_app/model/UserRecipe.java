package com.example.recipes_app.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class UserRecipe {

    final public static String COLLECTION_NAME = "userRecipe";

    @PrimaryKey
    @NonNull
    String recipeNameAsId;

    String usernameAsId;

    Long updateDate = new Long(0);

    public UserRecipe(){}
    public UserRecipe(String usernameAsId,String recipeNameAsId){
        this.recipeNameAsId = recipeNameAsId;
        this.usernameAsId = usernameAsId;
    }

    public String getRecipeNameAsId() {
        return recipeNameAsId;
    }

    public String getUsernameAsId() {
        return usernameAsId;
    }

    public void setRecipeNameAsId(String recipeNameAsId) {
        this.recipeNameAsId = recipeNameAsId;
    }

    public void setUsernameAsId(String usernameAsId) {
        this.usernameAsId = usernameAsId;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("usernameAsId",usernameAsId);
        json.put("recipeNameAsId",recipeNameAsId);
        json.put("updateDate", FieldValue.serverTimestamp());

        return json;
    }
    public static UserRecipe create(Map<String, Object> json) {
        String usernameAsId = (String) json.get("usernameAsId");
        String recipeNameAsId = (String) json.get("recipeNameAsId");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();


        UserRecipe userRecipe = new UserRecipe(usernameAsId,recipeNameAsId);
        userRecipe.setUpdateDate(updateDate);
        return userRecipe;
    }


    //TODO:....
    public Long getUpdateDate() {
        return updateDate;
    }

}
