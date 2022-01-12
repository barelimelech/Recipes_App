package com.example.recipes_app.ui.RecipesList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipesListViewModel extends ViewModel {
    LiveData<List<Recipe>> recipes;
    List<Recipe> tmpRecipes;


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


//    public void deleteRecipe(Recipe recipe, Model.DeleteRecipeListener listener) {
//        for(Recipe rec:recipes.getValue()){
//            if(!rec.equals(recipe)){
//                tmpRecipes.add(rec);
//            }
//        }
//        recipes.getValue().addAll(tmpRecipes);
//    }

//    public void deleteRecipe(Recipe recipe){
//        for(Recipe rec:recipes.getValue()){
//            if(!rec.isDeleted()){
//                tmpRecipes.getValue().add(rec);
//            }
//        }
//        recipes = tmpRecipes;
//    }



}
