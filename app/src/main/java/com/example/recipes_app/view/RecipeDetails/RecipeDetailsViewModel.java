package com.example.recipes_app.view.RecipeDetails;

import androidx.lifecycle.ViewModel;

import com.example.recipes_app.model.Model;

public class RecipeDetailsViewModel extends ViewModel {


    public RecipeDetailsViewModel(){}

    public void getRecipeByRecipeName(String recipeName, Model.GetRecipeByRecipeName listener){
        Model.instance.getRecipeByRecipeName(recipeName,listener);
    }
    public void refreshRecipesList(){
        Model.instance.refreshRecipeList();
    }
}
