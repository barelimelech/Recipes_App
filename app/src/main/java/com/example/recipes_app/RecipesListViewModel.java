package com.example.recipes_app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;

import java.util.List;

public class RecipesListViewModel extends ViewModel {
    LiveData<List<Recipe>> recipes;

    public RecipesListViewModel() {
        recipes = Model.instance.getAll();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }
}
