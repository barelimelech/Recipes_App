package com.example.recipes_app.model;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static final Model instance = new Model();

    Executor executor = Executors.newFixedThreadPool(1);
    Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    ModelFirebase modelFirebase = new ModelFirebase();

    private Model() {
    }

    List<String> data = new LinkedList<String>();

    public interface GetAllRecipesListener{
        void onComplete(List<Recipe> list);
    }

    public void getAllRecipes(GetAllRecipesListener listener){
        modelFirebase.getAllRecipes(listener);
    }

    public interface AddRecipeListener{
        void onComplete();
    }

    public void addRecipe(Recipe recipe, AddRecipeListener listener){
        modelFirebase.addRecipe( recipe,  listener);
    }

    public interface GetRecipeById{
        void onComplete(Recipe recipe);
    }
    public Recipe getRecipeById(String recipeId, GetRecipeById listener) {
        modelFirebase.getRecipeById(recipeId,listener);
        return null;
    }

    public List<String> getAllCategories() {

        data.add("Deserts");
        data.add("Breakfast");
        data.add("example");
        data.add("Lunch");
        data.add("Dinner");
        data.add("Holidays");
        return data;
        //return null;
    }


}
