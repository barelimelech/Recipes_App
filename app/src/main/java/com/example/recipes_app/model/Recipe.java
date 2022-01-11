package com.example.recipes_app.model;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Recipe {
    final public static String COLLECTION_NAME = "recipes";

    @PrimaryKey
    @NonNull
    String name = "";
    String method= "";
    String ingredients= "";
    String type = "";
    String recipeUrl;
    String username;

    Long updateDate = new Long(0);

    //boolean isDeleted=false;


    public Recipe(){}
    public Recipe(String name,String method, String ingredients,String type,String username) {
        this.name = name;
        this.method = method;
        this.ingredients = ingredients;
        this.type = type;
        this.username = username;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() { return name; }

    public String getUsername() {
        return username;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getMethod() {
        return method;
    }

    public String getType() {
        return type;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("name",name);
        json.put("method",method);
        json.put("ingredients",ingredients);
        json.put("type",type);
        json.put("updateDate", FieldValue.serverTimestamp());
        json.put("recipeUrl", recipeUrl);
        json.put("username", username);

        return json;
    }

    public static Recipe create(Map<String, Object> json) {
        String name = (String) json.get("name");
        String method = (String) json.get("method");
        String ingredients = (String) json.get("ingredients");
        String type = (String) json.get("type");
//        Timestamp ts = (Timestamp)json.get("updateDate");
//        Long updateDate = ts.getSeconds();
        String recipeUrl = (String) json.get("recipeUrl");
        String username = (String) json.get("username");

        Recipe recipe = new Recipe(name,method,ingredients,type,username);
    //    recipe.setUpdateDate(updateDate);
        recipe.setImageUrl(recipeUrl);
        return recipe;
    }

    public Long getUpdateDate() {
        return updateDate;
    }


    public void setImageUrl(String url) {
        recipeUrl = url;
    }
    public String getRecipeUrl() { return recipeUrl; }

}