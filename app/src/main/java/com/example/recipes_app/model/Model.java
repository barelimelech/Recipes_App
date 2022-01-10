package com.example.recipes_app.model;

import android.content.Context;
import android.graphics.Bitmap;
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
    public enum UserListLoadingState {
        loading,
        loaded
    }
    public enum UserRecipeListLoadingState {
        loading,
        loaded
    }

    MutableLiveData<RecipeListLoadingState> recipeListLoadingState = new MutableLiveData<>();
    MutableLiveData<UserListLoadingState> userListLoadingState = new MutableLiveData<>();
    MutableLiveData<UserRecipeListLoadingState> userRecipeListLoadingState = new MutableLiveData<>();


    public LiveData<RecipeListLoadingState> getRecipeListLoadingState() {
        return recipeListLoadingState;
    }

    public LiveData<UserListLoadingState> getUserListLoadingState() {
        return userListLoadingState;
    }
    ModelFirebase modelFirebase = new ModelFirebase();

    //ModelFirebaseAuth modelFirebaseAuth = new ModelFirebaseAuth();

    private Model() {
        recipeListLoadingState.setValue(RecipeListLoadingState.loaded);
        userListLoadingState.setValue(UserListLoadingState.loaded);
        userRecipeListLoadingState.setValue(UserRecipeListLoadingState.loaded);
    }

    List<String> data = new LinkedList<String>();

//    public interface GetAllRecipesListener{
//        void onComplete(List<Recipe> list);
//    }

//    public void getAllRecipes(GetAllRecipesListener listener){
//        modelFirebase.getAllRecipes(listener);
//    }

    MutableLiveData<List<Recipe>> recipesList = new MutableLiveData<List<Recipe>>();
    MutableLiveData<List<User>> usersList = new MutableLiveData<List<User>>();
    MutableLiveData<List<UserRecipe>> userRecipeList = new MutableLiveData<List<UserRecipe>>();


    public LiveData<List<Recipe>> getAllRecipes() {
        if (recipesList.getValue() == null) {
            refreshRecipeList();
        }
        return recipesList;
    }

    public LiveData<List<User>> getAllUsers() {
        if (usersList.getValue() == null) {
            refreshUserList();
        }
        return usersList;
    }

    public LiveData<List<UserRecipe>> getAllUsersRecipes() {
        if (userRecipeList.getValue() == null) {
            refreshUserRecipeList();
        }
        return userRecipeList;
    }
//
//    public User getUser(String username){
//        List<User> users =AppLocalDb.db.userDao().getAll();
//        for(User u : users){
//            if(u.getUsername().equals(username)){
//                return u;
//            }
//        }
//        return null;
//    }

    public void refreshUserList() {
        userListLoadingState.setValue(UserListLoadingState.loading);

        //get last local update date
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("UserLastUpdateDate", 0);

        //firebase get all updates since lastLocalUpdateDate
        modelFirebase.getAllUsers(lastUpdateDate, new ModelFirebase.GetAllUsersListener() {
            @Override
            public void onComplete(List<User> list) {
                //add all records to the local db
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Long lud = new Long(0);
                        Log.d("TAG", "fb returned " + list.size());
                        for (User user : list) {
                            AppLocalDb.db.userDao().insertAll(user);
                            if (lud < user.getUpdateDate()) {
                                lud = user.getUpdateDate();
                            }
                        }
                        //update last local update date
                        MyApplication.getContext()
                                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                                .edit()
                                .putLong("UserLastUpdateDate", lud)
                                .commit();

                        //return all data to caller
                        List<User> reList = AppLocalDb.db.userDao().getAll();
                        usersList.postValue(reList);
                        userListLoadingState.postValue(UserListLoadingState.loaded);
                        //delete from room
                    }
                });
            }
        });
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
                        //delete from room //TODO
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
    public interface SaveImageListener{
        void onComplete(String url);
    }
    public void saveImage(Bitmap imageBitmap, String imageName, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap,imageName,listener);
    }
    public interface AddRecipeListener {
        void onComplete();
    }

    public interface DeleteRecipeListener {
        void onComplete();
    }
    public interface UpdateRecipeListener {
        void onSuccess();
    }

    public void UpdateRecipeListener(Recipe recipe, Recipe lastRecipe,UpdateRecipeListener listener) {
        modelFirebase.updateRecipe(recipe,lastRecipe,listener);
    }

    public void addRecipe(Recipe recipe, AddRecipeListener listener) {
        modelFirebase.addRecipe(recipe, ()->{
            listener.onComplete();
            refreshRecipeList();
        });
    }

    public void deleteRecipe(Recipe recipe,DeleteRecipeListener listener) {
        modelFirebase.deleteRecipe(recipe, listener);

//        modelFirebase.deleteRecipe(recipeName, ()->{
//            listener.onComplete();
//            refreshRecipeList();
//        });
    }
    public interface GetRecipeByRecipeName {
        void onComplete(Recipe recipe);
    }

    public Recipe getRecipeByRecipeName(String recipeId, GetRecipeByRecipeName listener) {
        modelFirebase.getRecipeByRecipeName(recipeId, listener);
        return null;
    }

//    public interface ConnectWithGoogle{
//        void onComplete();
//    }

//    public void connectWithGoogle(GoogleSignInAccount account , ConnectWithGoogle listener){
//        modelFirebaseAuth.firebaseAuthWithGoogle(account,listener);
//    }

    public List<String> getAllCategories() {

        data.add("Deserts");
        data.add("Breakfast");
        data.add("Lunch");
        data.add("Dinner");
        data.add("Holidays");
        return data;
        //return null;
    }


    //***********************************USER*************************************//

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
        modelFirebase.getUserByUsername(username, listener);
        return null;
    }
    //***********************************UserRecipe*************************************//

    public void refreshUserRecipeList() {
        userRecipeListLoadingState.setValue(UserRecipeListLoadingState.loading);

        //get last local update date
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("UserRecipeLastUpdateDate", 0);

        //firebase get all updates since lastLocalUpdateDate
        modelFirebase.getAllUsersRecipes(lastUpdateDate, new ModelFirebase.GetAllUsersRecipesListener() {
            @Override
            public void onComplete(List<UserRecipe> list) {
                //add all records to the local db
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Long lud = new Long(0);
                        Log.d("TAG", "fb returned " + list.size());
                        for (UserRecipe userRecipe : list) {
                            AppLocalDb.db.userRecipeDao().insertAll(userRecipe);
                            if (lud < userRecipe.getUpdateDate()) {
                                lud = userRecipe.getUpdateDate();
                            }
                        }
                        //update last local update date
                        MyApplication.getContext()
                                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                                .edit()
                                .putLong("UserRecipeLastUpdateDate", lud)
                                .commit();

                        //return all data to caller
                        List<UserRecipe> reList = AppLocalDb.db.userRecipeDao().getAll();
                        userRecipeList.postValue(reList);
                        userRecipeListLoadingState.postValue(UserRecipeListLoadingState.loaded);
                        //delete from room
                    }
                });
            }
        });
    }
    public interface AddUserRecipeListener {
        void onComplete();
    }

    public void addUserRecipe(UserRecipe user, AddUserRecipeListener listener) {
        modelFirebase.addUserRecipe(user, listener);
    }

    public interface GetUserRecipeByUsername {
        void onComplete(UserRecipe user);
    }
    public UserRecipe getUserRecipeByUsername(String username, GetUserRecipeByUsername listener) {
        modelFirebase.getUserRecipeByUsername(username,listener);
        return null;
    }


}
