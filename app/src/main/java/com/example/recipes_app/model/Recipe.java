package com.example.recipes_app.model;

public class Recipe {
    String name = "";
    String id = "";
    String method;
    String ingredients;


    public Recipe(){}
    public Recipe(String name, String id, String method, String ingredients) {
        this.name = name;
        this.id = id;
        this.ingredients = ingredients;
        this.method = method;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }


    public String getIngredients() {
        return ingredients;
    }

    public String getMethod() {
        return method;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
