package com.example.recipes_app.view.NewRecipe;

import android.graphics.Bitmap;

import androidx.lifecycle.ViewModel;

import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;

public class NewRecipeViewModel extends ViewModel {

    public NewRecipeViewModel(){
    }

    public String getCurrentUser(){
        return Model.instance.getCurrentUsername();
    }

    public void addRecipe(Recipe recipe1, Model.AddRecipeListener listener){
        Model.instance.addRecipe(recipe1,listener);
    }

    public void saveImage(Bitmap img, String name, Model.SaveImageListener listener){
        Model.instance.saveImage(img,name,listener);
    }
}
