package com.example.recipes_app.ui.RecipesList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;

import java.util.List;

public class RecipesListViewModel extends ViewModel {
    LiveData<List<Recipe>> recipes;
    LiveData<List<Recipe>> tmpRecipes;


    public RecipesListViewModel() {
        recipes = Model.instance.getAllRecipes();

    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public void deleteRecipe(Recipe recipe){
        
    }



}
