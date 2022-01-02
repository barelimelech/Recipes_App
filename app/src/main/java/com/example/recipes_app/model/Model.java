package com.example.recipes_app.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipes_app.MyApplication;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static final Model instance = new Model();

    Executor executor = Executors.newFixedThreadPool(1);
    Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    public enum RecipeListLoadingState {
        loading,
        loaded
    }

    MutableLiveData<RecipeListLoadingState> recipeListLoadingState = new MutableLiveData<>();
    //MutableLiveData<RecipeListLoadingState> userListLoadingState = new MutableLiveData<>();


    public LiveData<RecipeListLoadingState> getRecipeListLoadingState() {
        return recipeListLoadingState;
    }

    ModelFirebase modelFirebase = new ModelFirebase();

    private Model() {
        recipeListLoadingState.setValue(RecipeListLoadingState.loaded);
    }

    List<String> data = new LinkedList<String>();

//    public interface GetAllRecipesListener{
//        void onComplete(List<Recipe> list);
//    }

//    public void getAllRecipes(GetAllRecipesListener listener){
//        modelFirebase.getAllRecipes(listener);
//    }

    MutableLiveData<List<Recipe>> recipesList = new MutableLiveData<List<Recipe>>();

    public LiveData<List<Recipe>> getAll() {
        if (recipesList.getValue() == null) {
            refreshRecipeList();
        }
        return recipesList;
    }

    public void refreshRecipeList() {
        recipeListLoadingState.setValue(RecipeListLoadingState.loading);

        //get last local update date
        //צריך לשבת בRecipe
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("RecipeLastUpdateDate", 0);

        //firebase get all updates since lastLocalUpdateDate
        modelFirebase.getAllRecipes(lastUpdateDate, new ModelFirebase.GetAllRecipesListener() {
            @Override
            public void onComplete(List<Recipe> list) {
                //add all records to the local db
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Long lud = new Long(0);
                        Log.d("TAG", "fb returned " + list.size());
                        for (Recipe recipe : list) {
                            AppLocalDb.db.recipeDao().insertAll(recipe);
                            if (lud < recipe.getUpdateDate()) {
                                lud = recipe.getUpdateDate();
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
                        recipesList.postValue(reList);
                        recipeListLoadingState.postValue(RecipeListLoadingState.loaded);
                        //delete from room
                    }
                });
            }
        });


//        modelFirebase.getAllRecipes(lastUpdateDate, new ModelFirebase.GetAllRecipesListener() {
//            @Override
//            public void onComplete(List<Recipe> list) {
//                recipesList.setValue(list);
//                recipeListLoadingState.setValue(RecipeListLoadingState.loaded);
//            }
//        });
    }

    public interface AddRecipeListener {
        void onComplete();
    }

    public void addRecipe(Recipe recipe, AddRecipeListener listener) {
        modelFirebase.addRecipe(recipe, listener);
    }

    public interface GetRecipeById {
        void onComplete(Recipe recipe);
    }
    public Recipe getRecipeById(String recipeId, GetRecipeById listener) {
        modelFirebase.getRecipeById(recipeId,listener);
        return null;
    }

    public List<String> getAllCategories() {

        data.add("Deserts");
        data.add("Breakfast");
        data.add("example");
        data.add("Lunch");
        data.add("Dinner");
        data.add("Holidays");
        return data;
        //return null;
    }

    public interface AddUserListener {
        void onComplete();
    }

    public void addUser(User user, AddUserListener listener) {
        modelFirebase.addUser(user, listener);
    }

    public interface GetUserByUsername {
        void onComplete(User user);
    }
    public User getUserByUsername(String username, GetUserByUsername listener) {
        modelFirebase.getUserByUsername(username,listener);
        return null;
    }


}
