package com.example.recipes_app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;
import com.example.recipes_app.model.UserRecipe;

import java.util.List;

public class RecipesListViewModel extends ViewModel {
    LiveData<List<Recipe>> recipes;
    LiveData<List<Recipe>> tmpRecipes;

    LiveData<List<UserRecipe>> userRecipes;

    public RecipesListViewModel() {
        recipes = Model.instance.getAllRecipes();
        userRecipes = Model.instance.getAllUsersRecipes();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public LiveData<List<UserRecipe>> getUsersRecipes() {
        return userRecipes;
    }

    public LiveData<List<Recipe>> getRecipesOfCategory(String category) {
        //recipes.getValue().clear();
        for(Recipe rec : Model.instance.getAllRecipes().getValue()){
            if(rec.getType().equals(category)){
                recipes.getValue().add(rec);
            }
        }
        return recipes;
    }
    public LiveData<List<Recipe>> getRecipesOfUser(String username) {
        for(UserRecipe rec : Model.instance.getAllUsersRecipes().getValue()){
            if(rec.getUsernameAsId().equals(username)){
                //userRecipes.getValue().add(rec);
            }
        }
        //return userRecipes;
        return recipes;

    }

}
