package com.example.recipes_app.view.EditRecipe;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipes_app.model.ModelRecipe;
import com.example.recipes_app.model.Recipe;

import java.util.List;

public class EditRecipeViewModel extends ViewModel {

    MutableLiveData<Recipe> recipe;

    public EditRecipeViewModel(){
        recipe = new MutableLiveData<>();
    }

    public MutableLiveData<Recipe> editRecipe(Recipe recipe1, ModelRecipe.EditRecipeListener listener){
        recipe.setValue(recipe1);
        ModelRecipe.instance.editRecipe(recipe.getValue(),listener);
        return recipe;
    }

    public void saveImage(Bitmap img, String name, ModelRecipe.SaveImageListener listener){
        ModelRecipe.instance.saveImage(img,name,listener);
    }

    public void refreshRecipesList(){
        ModelRecipe.instance.refreshRecipeList();
    }

    public void getRecipeByRecipeName(String recipeName, ModelRecipe.GetRecipeByRecipeName listener){
        ModelRecipe.instance.getRecipeByRecipeName(recipeName,listener);
    }
    public List<String> getAllCategories(){
        return ModelRecipe.instance.getAllCategories();
    }

}
