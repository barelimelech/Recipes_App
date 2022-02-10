package com.example.recipes_app.view.EditRecipe;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipes_app.model.Model;
import com.example.recipes_app.model.Recipe;

public class EditRecipeViewModel extends ViewModel {

    MutableLiveData<Recipe> recipe;

    public EditRecipeViewModel(){
        recipe = new MutableLiveData<>();
    }

    public MutableLiveData<Recipe> editRecipe(Recipe recipe1, Model.EditRecipeListener listener){
        recipe.setValue(recipe1);
        Model.instance.editRecipe(recipe.getValue(),listener);
        return recipe;
    }

    public void saveImage(Bitmap img, String name, Model.SaveImageListener listener){
        Model.instance.saveImage(img,name,listener);
    }

    public void refreshRecipesList(){
        Model.instance.refreshRecipeList();
    }

//    public void logout(){
//        String currentUserEmail = Model.instance.getCurrentUserEmail();
//        Model.instance.getFirebaseAuth().signOut();
//        Model.instance.logout(currentUserEmail
//    }

    public void getRecipeByRecipeName(String recipeName, Model.GetRecipeByRecipeName listener){
        Model.instance.getRecipeByRecipeName(recipeName,listener);
    }
}
