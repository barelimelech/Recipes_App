package com.example.recipes_app.view.RecipesList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipes_app.model.ModelRecipe;
import com.example.recipes_app.model.ModelUser;
import com.example.recipes_app.model.Recipe;

import java.util.List;

public class RecipesListViewModel extends ViewModel {
    LiveData<List<Recipe>> recipes;
    LiveData<List<Recipe>> userRecipes;

    public RecipesListViewModel() {
        recipes = ModelRecipe.instance.getAllRecipes();
        userRecipes = ModelRecipe.instance.getAllUserRecipes();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public void deleteRecipe(Recipe recipe, ModelRecipe.DeleteRecipeListener listener) {
        ModelRecipe.instance.deleteRecipe(recipe,listener);
    }

    public LiveData<List<Recipe>> getRecipesByUsername() {
        recipes = userRecipes;
        return userRecipes;
    }

    public void refreshUserRecipesList(){
        ModelRecipe.instance.refreshUserRecipeList();
    }
    public void refreshRecipesList(){
        ModelRecipe.instance.refreshRecipeList();
    }

    public String getCurrentUser(){
        return ModelUser.instance.getCurrentUsername();
    }

}
