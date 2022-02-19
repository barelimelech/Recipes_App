package com.example.recipes_app.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipes_app.MyApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ModelRecipe {
    public static final ModelRecipe instance = new ModelRecipe();

    public Executor executor = Executors.newFixedThreadPool(1);
    private ModelFirebase modelFirebase = new ModelFirebase();
    List<String> data = new LinkedList<String>();

    MutableLiveData<List<Recipe>> recipesList = new MutableLiveData<List<Recipe>>();
    MutableLiveData<List<Recipe>> userRecipesList = new MutableLiveData<List<Recipe>>();

    public enum RecipeListLoadingState {
        loading,
        loaded
    }

    MutableLiveData<RecipeListLoadingState> recipeListLoadingState = new MutableLiveData<>();
    public LiveData<RecipeListLoadingState> getRecipeListLoadingState() {
        return recipeListLoadingState;
    }


    private ModelRecipe(){
        recipeListLoadingState.setValue(RecipeListLoadingState.loaded);
        data.add("Desserts");
        data.add("Breakfast");
        data.add("Lunch");
        data.add("Dinner");
        data.add("Holidays");
    }


    public LiveData<List<Recipe>> getAllRecipes() {
        if (recipesList.getValue() == null) {
            refreshRecipeList();
        }
        return recipesList;
    }

    public LiveData<List<Recipe>> getAllUserRecipes() {
        if (userRecipesList.getValue() == null) {
            refreshUserRecipeList();
        }
        return userRecipesList;
    }


    public void refreshRecipeList() {
        recipeListLoadingState.setValue(RecipeListLoadingState.loading);

        //get last local update date
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("RecipeLastUpdateDate", 0);

        //firebase get all updates since lastLocalUpdateDate
        modelFirebase.getAllRecipes(lastUpdateDate, list -> {
            //add all records to the local db
            executor.execute(() -> {
                Long lud = new Long(0);
                for (Recipe recipe : list) {
                    if(recipe.getIsDeleted().equals("true")){
                        AppLocalDb.db.recipeDao().delete(recipe);
                    }else {
                        AppLocalDb.db.recipeDao().insertAll(recipe);

                        if (lud < recipe.getUpdateDate()) {
                            lud = recipe.getUpdateDate();
                        }
                    }
                }

                //update last local update date
                MyApplication.getContext()
                        .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                        .edit()
                        .putLong("RecipeLastUpdateDate", lud)
                        .commit();

                //return all data to caller
                List<Recipe> reList = AppLocalDb.db.recipeDao().getAll();
                Collections.reverse(reList);
                recipesList.postValue(reList);
                recipeListLoadingState.postValue(RecipeListLoadingState.loaded);
            });
        });
    }

    public interface SaveImageListener{
        void onComplete(String url);
    }
    public void saveImage(Bitmap imageBitmap, String imageName, ModelRecipe.SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap,imageName,listener);
    }

    public void saveImageFile(final Bitmap imageBitmap, String name, final SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, name, url -> {
            String localName = getLocalImageFileName(url);
            Log.d("TAG","cach image: " + localName);
            saveImageToFile(imageBitmap,localName);
            listener.onComplete(url);
        });
    }

    public void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageFileName);
            imageFile.createNewFile();
            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            addPictureToGallery(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addPictureToGallery(File imageFile){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        MyApplication.getContext().sendBroadcast(mediaScanIntent);
    }

    private String getLocalImageFileName(String url) {
        String name = URLUtil.guessFileName(url, null, null);
        return name;
    }

    public interface AddRecipeListener {
        void onComplete();
    }

    public interface DeleteRecipeListener {
        void onComplete();
    }

    public void addRecipe(Recipe recipe, ModelRecipe.AddRecipeListener listener) {
        modelFirebase.addRecipe(recipe, ()->{
            listener.onComplete();
            refreshRecipeList();
        });
    }

    public interface EditRecipeListener {
        void onComplete();
    }

    public void editRecipe(Recipe newRecipe, ModelRecipe.EditRecipeListener listener){
        modelFirebase.editRecipe(newRecipe, ()->{
            listener.onComplete();
            refreshRecipeList();
        });
    }

    public void deleteRecipe(Recipe recipe, ModelRecipe.DeleteRecipeListener listener) {
        modelFirebase.deleteRecipe(recipe, ()->{
            listener.onComplete();
            refreshRecipeList();

        });
    }

    public interface GetRecipeByRecipeId {
        void onComplete(Recipe recipe);
    }

    public Recipe getRecipeByRecipeId(String recipeId, ModelRecipe.GetRecipeByRecipeId listener) {
        modelFirebase.getRecipeByRecipeId(recipeId, listener);
        return null;
    }

    public List<String> getAllCategories() { return data; }


    public void refreshUserRecipeList() {
        recipeListLoadingState.setValue(RecipeListLoadingState.loading);

        //get last local update date
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("RecipeLastUpdateDate", 0);

        //firebase get all updates since lastLocalUpdateDate
        modelFirebase.getAllRecipes(lastUpdateDate, new ModelFirebase.GetAllRecipesListener() {
            @Override
            public void onComplete(List<Recipe> list) {
                executor.execute(() -> {
                    Long lud = new Long(0);
                    for (Recipe recipe : list) {
                        if(recipe.getIsDeleted().equals("true") || !recipe.getUsername().equals(getCurrentUsername()) ){
                            AppLocalDb.db.recipeDao().delete(recipe);
                        }else if(recipe.getUsername().equals(getCurrentUsername())) {
                            AppLocalDb.db.recipeDao().insertAll(recipe);

                            if (lud < recipe.getUpdateDate()) {
                                lud = recipe.getUpdateDate();
                            }
                        }

                    }
                    MyApplication.getContext()
                            .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                            .edit()
                            .putLong("RecipeLastUpdateDate", lud)
                            .commit();

                    List<Recipe> reList = AppLocalDb.db.recipeDao().getAll();
                    Collections.reverse(reList);
                    userRecipesList.postValue(reList);
                    recipeListLoadingState.postValue(RecipeListLoadingState.loaded);
                });
            }
        });
    }


    public String getCurrentUsername(){
        return ModelUser.instance.getCurrentUsername();
    }

}
