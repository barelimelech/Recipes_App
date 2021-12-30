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


    public Recipe(){}
    public Recipe(String name,String method, String ingredients,String id) {
        this.name = name;
        this.method = method;
        this.ingredients = ingredients;
        this.id = id;
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




    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("name",name);
        json.put("method",method);
        json.put("ingredients",ingredients);
        json.put("id",id);

        return json;
    }
    public static Recipe create(Map<String, Object> json) {
        String name = (String) json.get("name");
        String method = (String) json.get("method");
        String ingredients = (String) json.get("ingredients");
        String id = (String) json.get("id");


        Recipe recipe = new Recipe(name,method,ingredients,id);
        return recipe;
    }
}
