package com.example.recipes_app.view.RecipesList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;
import com.example.recipes_app.view.MyAccount.UsersListViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecipesListViewModel extends ViewModel {
    LiveData<List<Recipe>> recipes;
    List<Recipe> tmpRecipes;
    List<Recipe> recipesL;
    public UsersListViewModel usersListViewModel;


    public RecipesListViewModel() {
        recipes = Model.instance.getAllRecipes();
        tmpRecipes = new ArrayList<>();

    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }


    public void deleteRecipe(Recipe recipe, Model.DeleteRecipeListener listener) {

        Model.instance.deleteRecipe(recipe,listener);
    }

    public List<Recipe> getAllUR(){
        recipesL = usersListViewModel.getAllUserRecipes(Model.instance.getCurrentUsername());
        return recipesL;
    }



}
