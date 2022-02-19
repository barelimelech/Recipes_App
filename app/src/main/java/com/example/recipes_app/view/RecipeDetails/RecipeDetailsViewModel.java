package com.example.recipes_app.view.RecipeDetails;

import androidx.lifecycle.ViewModel;

import com.example.recipes_app.model.ModelRecipe;

public class RecipeDetailsViewModel extends ViewModel {


    public RecipeDetailsViewModel(){}

    public void getRecipeByRecipeName(String recipeName, ModelRecipe.GetRecipeByRecipeId listener){
        ModelRecipe.instance.getRecipeByRecipeId(recipeName,listener);
    }
    public void refreshRecipesList(){
        ModelRecipe.instance.refreshRecipeList();
    }
}
