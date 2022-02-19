package com.example.recipes_app.view.NewRecipe;

import android.graphics.Bitmap;

import androidx.lifecycle.ViewModel;

import com.example.recipes_app.model.ModelRecipe;
import com.example.recipes_app.model.ModelUser;
import com.example.recipes_app.model.Recipe;

import java.util.List;

public class NewRecipeViewModel extends ViewModel {

    public NewRecipeViewModel(){ }

    public String getCurrentUser(){
        return ModelUser.instance.getCurrentUsername();
    }

    public void addRecipe(Recipe recipe1, ModelRecipe.AddRecipeListener listener){
        ModelRecipe.instance.addRecipe(recipe1,listener);
    }

    public void saveImage(Bitmap img, String name, ModelRecipe.SaveImageListener listener){
        ModelRecipe.instance.saveImage(img,name,listener);
    }
    public void saveImageToFile(Bitmap img, String name,ModelRecipe.SaveImageListener listener){
        ModelRecipe.instance.saveImageFile(img,name,listener);
    }

    public List<String> getAllCategories(){
        return ModelRecipe.instance.getAllCategories();
    }
}
