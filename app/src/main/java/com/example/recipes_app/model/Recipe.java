package com.example.recipes_app.model;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
    String id = "";
    String type = "";


    public Recipe(){}
    public Recipe(String name,String method, String ingredients,String id,String type) {
        this.name = name;
        this.method = method;
        this.ingredients = ingredients;
        this.id = id;
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getId() {
        return id;
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

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("name",name);
        json.put("method",method);
        json.put("ingredients",ingredients);
        json.put("id",id);
        json.put("type",type);

        return json;
    }
    public static Recipe create(Map<String, Object> json) {
        String name = (String) json.get("name");
        String method = (String) json.get("method");
        String ingredients = (String) json.get("ingredients");
        String id = (String) json.get("id");
        String type = (String) json.get("type");


        Recipe recipe = new Recipe(name,method,ingredients,id,type);
        return recipe;
    }
}
