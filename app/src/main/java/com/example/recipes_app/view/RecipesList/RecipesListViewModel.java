package com.example.recipes_app.view.RecipesList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipesListViewModel extends ViewModel {
    LiveData<List<Recipe>> recipes;
    LiveData<List<Recipe>> userRecipes;

    List<Recipe> tmpRecipes;
    List<Recipe> recipesL;


    public RecipesListViewModel() {
        recipes = Model.instance.getAllRecipes();
        userRecipes = Model.instance.getAllUserRecipes();
        tmpRecipes = new ArrayList<>();

    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }


    public void deleteRecipe(Recipe recipe, Model.DeleteRecipeListener listener) {
        Model.instance.deleteRecipe(recipe,listener);
    }





    public LiveData<List<Recipe>> getRecipesByUsername() {
        recipes = userRecipes;
        return userRecipes;
    }

    public void refreshUserRecipesList(){
        Model.instance.refreshUserRecipeList();
    }
    public void refreshRecipesList(){
        Model.instance.refreshRecipeList();
    }


}
